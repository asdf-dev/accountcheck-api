package accountcheck.views

import grails.plugin.json.view.test.JsonRenderResult
import grails.plugin.json.view.test.JsonViewTest
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals

class HealthCheckViewSpec extends Specification implements JsonViewTest {


    def "greenv view healthcheck"() {

        given:
            def expected = '{"TimeAlive":"01:00:00"}'
        when:
            JsonRenderResult renderResult = render(view: "health/health", model: [upTime: "01:00:00"])
            def actucal = renderResult.jsonText

        then:
            assertJsonEquals(expected, actucal)
    }


}
