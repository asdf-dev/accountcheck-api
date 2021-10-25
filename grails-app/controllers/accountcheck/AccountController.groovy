package accountcheck


import org.springframework.validation.Errors

class AccountController {
    static responseFormats = ['json']

    AccountService accountService


    def players() {

        String plainTextRequest = request.reader.text

        def players = accountService.findPlayers(plainTextRequest)


        render(view: "index", model: [players: players])
    }


    //todo we cant render errors
//    private renderErrors(Errors errors) {
//        render(template: "/errors/errors", model: [errors: errors])
//    }
}
