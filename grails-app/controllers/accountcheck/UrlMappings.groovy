package accountcheck

class UrlMappings {

    static mappings = {
//        delete "/$controller/$id(.$format)?"(action:"delete")
//        get "/$controller(.$format)?"(action:"index")
//        get "/$controller/$id(.$format)?"(action:"_player")
//        post "/$controller(.$format)?"(action:"save")
//        put "/$controller/$id(.$format)?"(action:"update")
//        patch "/$controller/$id(.$format)?"(action:"patch")

        get "/api/spec"(controller: 'spec', action: 'showSpec')

        post "/accountcheck"(controller: "account", action: 'players')

        post "/cc"(controller: "account", action: "clear")


        get "/health"(controller: "health", action: 'health')

        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
