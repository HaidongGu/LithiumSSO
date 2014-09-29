package lithium.apac.apacsso

import grails.transaction.Transactional
import javax.annotation.PostConstruct
import javax.servlet.http.Cookie

@Transactional
class LithiumSSOService {

    lithium.user.sso.LithiumSSOClient ssoClient
    def springSecurityService
    def grailsApplication

    @PostConstruct
    void init() {
        log.info "Initializing"
        def conf = grailsApplication.config.lithium.apac.apacsso
        def ssokey = new lithium.util.security.Key(conf.ssokey) 
        ssoClient = lithium.user.sso.LithiumSSOClient.getInstance(ssokey.getRaw(), conf.clientid, conf.domain, conf.serverid)
    }

    private String deNull(String s) {
        if (s==null) {
            return ""
        } else {
            return s
        }
    }

    Cookie getSSOCookie(request) {

        def curUser = springSecurityService.getCurrentUser()

    	String ssoid =  curUser.username  
        String screenname = curUser.screenname
        String email = curUser.email

        String settingString = "profile.name_first=Haidong3|profile.name_last=Gu"
        String reqUserAgent = deNull( request.getHeader("user-agent") )
        String reqReferer = deNull( request.getHeader("referer") )

        String reqRemoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR")
        if ( reqRemoteAddr == null ) {
        	reqRemoteAddr = request.getRemoteAddr()
        }

        String ssoCookieValue = ssoClient.getLithiumCookieValue(ssoid, screenname, email, settingString, reqUserAgent, reqReferer, reqRemoteAddr);
        Cookie ssoCookie = ssoClient.getLithiumCookie( ssoCookieValue )
        ssoCookie.setSecure(false)

        return ssoCookie
    }

}
