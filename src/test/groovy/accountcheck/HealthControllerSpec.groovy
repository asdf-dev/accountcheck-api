package accountcheck

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class HealthControllerSpec extends Specification implements ControllerUnitTest<HealthController> {

    @Unroll
    def "we can convert time correct #givenTime is #expectedTime "() {

        given:
            givenTime.toLong()

        when:
            def result = controller.timeToHours(givenTime)

        then:
            result == expectedTime

        where:
            givenTime | expectedTime
            60000     | "00:01:00" // 1min
            1800000   | "00:30:00" // 30min
            3600000   | "01:00:00" //  1 time
            604800000 | "168:00:00" // 1 week
    }
}