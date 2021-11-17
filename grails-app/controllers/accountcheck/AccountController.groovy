package accountcheck

import org.springframework.http.HttpStatus


class AccountController {
    static responseFormats = ['json']

    AccountService accountService
    def grailsCacheAdminService

    def players() {

        String plainTextRequest = request.reader.text

        def players = accountService.findPlayers(plainTextRequest)


        render(view: "index", model: [players: players])
    }

    def clear() {
        if (!request.getHeader("x-i-am-admin")) {
            render(HttpStatus.FORBIDDEN)
            return
        }
        grailsCacheAdminService.clearCache("faceitplayer")
        grailsCacheAdminService.clearCache("faceitstats")
        grailsCacheAdminService.clearCache("steamprofile")
        grailsCacheAdminService.clearCache("steamgames")

        render(HttpStatus.NO_CONTENT)
    }

    //todo we cant render errors
//    private renderErrors(Errors errors) {
//        render(template: "/errors/errors", model: [errors: errors])
//    }
}
