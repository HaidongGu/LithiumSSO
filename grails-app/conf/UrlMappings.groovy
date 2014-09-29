class UrlMappings {

	static mappings = {
		"/register/$action?/$id?(.${format})?" (controller: "SSORegister")

        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
