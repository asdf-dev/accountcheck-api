package accountcheck

import grails.testing.mixin.integration.Integration
import io.micronaut.http.HttpStatus
import rest.RestBackendSpec

@Integration
class SpecRestCheck extends RestBackendSpec {

    String resursePath = "api/spec"

    def "we can get spec"() {
        when:
            def response = httpGET()

        then:
            response.status == HttpStatus.OK
            response.body().contains("openapi: 3.0.3")
    }

}
