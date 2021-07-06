package accountcheck


import accountcheck.model.RequestList
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors

class AccountController {
    static responseFormats = ['json']

    AccountService accountService

    private static final String UNPROCESSABLE_ENTITY = HttpStatus.UNPROCESSABLE_ENTITY

    def players(RequestList req) {

        if (req.hasErrors() || !req.steam64) {
            if (!req.steam64) {
                req.errors.reject(UNPROCESSABLE_ENTITY, "list cannot be empty")
                renderErrors(req.errors)
                return
            }
            req.errors.reject(UNPROCESSABLE_ENTITY, req.errors.allErrors.first().defaultMessage)
            renderErrors(req.errors)
            return
        }

        //call account service

        render "hello world"
    }


    private renderErrors(Errors errors) {
        render(template: "/errors/errors", model: [errors: errors])
    }
}
