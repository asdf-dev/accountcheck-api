package accountcheck

import grails.testing.mixin.integration.Integration
import rest.RestBackendSpec

@Integration
class HealthCheckRestSpec extends RestBackendSpec {


    String resursePath


    def setup() {
        resursePath = "health"
    }


    def "we can call healthCheck"() {
        when:
            def response = httpGET()
            Object returnBody = parseResponseBodyToJson(response)


        then:
            response.status.code == 200

        and:
            returnBody.with {
                TimeAlive instanceof String
            }
    }

}
