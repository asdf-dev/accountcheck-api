package accountcheck

import accountcheck.model.RequestList
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class AccountControllerSpec extends Specification implements ControllerUnitTest<AccountController> {

    def "we can bind #givenRequest status #givenStatus"() {
        given:
            RequestList requestList = new RequestList()

            requestList.steam64 = givenRequest

        when:
            controller.players(requestList)

        then:
            response.status == givenStatus

        where:

            givenRequest    | givenStatus
            [123]           | 200
            [123, 123]      | 200
            [123, 123, 123] | 200
    }
}