package lithium.apac.apacsso

import javax.servlet.http.HttpServletResponse

import org.springframework.security.access.annotation.Secured

import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.security.core.context.SecurityContextHolder

@Secured('permitAll')
class SSOLogoutController {

	
	def logoutHandlers

	def index() {

		def auth = SecurityContextHolder.context.authentication
	    if (auth) {
	        logoutHandlers.each  { handler->
	            handler.logout(request,response,auth)
	        }
	    }


        if (params.referer) {
            redirect(url: params.referer )
        } else {
        	render " SSO Logout with out param value for referer"
        }

	    /*
		def logoutUrl = SpringSecurityUtils.securityConfig.logout.filterProcessesUrl
        if (params.referer) {
            logoutUrl = logoutUrl + "?spring-security-redirect=" + params.referer
        }
        log.info "logout url = " + logoutUrl

        redirect uri: logoutUrl	
        */
	}

	def redirectBack() {
		render "redirectBack" 
	}
}