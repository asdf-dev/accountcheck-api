package accountcheck

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class SpecControllerSpec extends Specification implements ControllerUnitTest<SpecController> {

    void "We can get a file"() {
        when:
            def result = controller.spec

        then:
            result.class == File
            result.text.contains "openapi: 3.0.3"
    }
}