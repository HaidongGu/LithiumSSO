package lithium.apac.apacsso

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.ui.RegistrationCode
import grails.plugin.springsecurity.authentication.dao.NullSaltSource

class SSORegisterController extends grails.plugin.springsecurity.ui.RegisterController {

	def mailService
	def messageSource
	def saltSource

	def index() {
		def copy = [:] + (flash.chainedParams ?: [:])
		copy.remove 'controller'
		copy.remove 'action'
		def newcommand = new SSORegisterCommand(copy)

        if (params.referer) {
            newcommand.referer = params.referer
            log.info 'save referer ' + params.referer
        } else {
        	log.warn 'no referer for SSORegisterController'
        }

		[command: newcommand]
	}

	def register(SSORegisterCommand command) {

		if (command.hasErrors()) {
			//command.errors.each {
        	//	log.info it
    		//}
			render view: 'index', model: [command: command]
			return
		}

		String salt = saltSource instanceof NullSaltSource ? null : command.username
		def user = lookupUserClass().newInstance(email: command.email, username: command.username, screenname: command.screenname, 
													registrationSource: 'SSO', accountLocked: true, enabled: true)

		RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
		if (registrationCode == null || registrationCode.hasErrors()) {
			// null means problem creating the user
			flash.error = message(code: 'spring.security.ui.register.miscError')
			flash.chainedParams = params
			redirect action: 'index'
			return
		}

		String url = generateLink('verifyRegistration', [t: registrationCode.token]) 

		if (command.referer!=null && command.referer!="null" && command.referer!="") {
			url = url + "&referer=" + command.referer
		}

		def conf = SpringSecurityUtils.securityConfig
		def body = conf.ui.register.emailBody
		if (body.contains('$')) {
			body = evaluate(body, [user: user, url: url])
		}

		try {
			mailService.sendMail {
				to command.email
				from conf.ui.register.emailFrom
				subject conf.ui.register.emailSubject
				html body.toString()
			}
		} catch (Exception e) {
			log.error(e)
		}


		render view: 'index', model: [emailSent: true]
		//APAC login the current user
		//need to accountLocked=false
        //if (command.referer) {
        //	springSecurityService.reauthenticate command.username
        //    redirect url: '/SSOLogin?referer=' + command.referer
        //} else {
        //	render 'no referer found'
        //}		
	}

	def verifyRegistration() {

		def conf = SpringSecurityUtils.securityConfig
		String defaultTargetUrl = conf.successHandler.defaultTargetUrl

		String token = params.t

		def registrationCode = token ? RegistrationCode.findByToken(token) : null
		if (!registrationCode) {
			flash.error = message(code: 'spring.security.ui.register.badCode')
			redirect uri: defaultTargetUrl
			return
		}

		def user
		// TODO to ui service
		RegistrationCode.withTransaction { status ->
			String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
			user = lookupUserClass().findWhere((usernameFieldName): registrationCode.username)
			if (!user) {
				return
			}
			user.accountLocked = false
			user.verifiedDate = new Date()
			user.save(flush:true)
			def UserRole = lookupUserRoleClass()
			def Role = lookupRoleClass()
			for (roleName in conf.ui.register.defaultRoleNames) {
				UserRole.create user, Role.findByAuthority(roleName)
			}
			registrationCode.delete()
		}

		if (!user) {
			flash.error = message(code: 'spring.security.ui.register.badCode')
			redirect uri: defaultTargetUrl
			return
		}

		springSecurityService.reauthenticate user.username

		flash.message = message(code: 'spring.security.ui.register.complete')

        if (params.referer) {
            redirect url: '/SSOLogin?referer=' + params.referer
        } else {
        	redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl //original URL
        }		
	}


	protected String generateLink(String action, linkParams) {
		createLink(base: "$request.scheme://$request.serverName:$request.serverPort$request.contextPath",
				controller: 'SSORegister', action: action,
				params: linkParams)
	}

}

class SSORegisterCommand {
	String username
	String email
	String password
	String password2
	String screenname
	String referer

	def grailsApplication

	static constraints = {
		username blank: false, validator: { value, command ->
			if (value) {
				def User = command.grailsApplication.getDomainClass(
					SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
				if (User.findByUsername(value)) {
					return 'SSORegisterCommand.username.unique'
				}
			}
		}
		screenname blank: false, size: 3..15, validator: { value, command ->
			if (value) {
				def User = command.grailsApplication.getDomainClass(
					SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
				if (User.findByScreenname(value)) {
					return 'SSORegisterCommand.screenname.unique'
				}
			}
		}
		email blank: false, email: true
		password blank: false, validator: SSORegisterController.passwordValidator
		password2 validator: SSORegisterController.password2Validator
	}
}
