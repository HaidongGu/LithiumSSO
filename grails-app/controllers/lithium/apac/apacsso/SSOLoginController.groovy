package lithium.apac.apacsso

import javax.servlet.http.Cookie

class SSOLoginController {

    def lithiumSSOService

    def index() { 
        log.info "accessing index......."

        def ssoCookie = lithiumSSOService.getSSOCookie(request)
        response.addCookie(ssoCookie)

        if (params.referer) {
            redirect(url: params.referer )
        } else {
        	render "cookie generated as " + ssoCookie.value
        }
    }
}
