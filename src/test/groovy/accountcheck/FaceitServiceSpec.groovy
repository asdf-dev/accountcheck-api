package accountcheck

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class FaceitServiceSpec extends Specification implements ServiceUnitTest<FaceitService>{


    //setup wiremock

    def "test ok kald wiremock"() {
        expect:"fix me"
            true == false
    }


    def "test 404 kald wiremock"() {
        expect:"fix me"
            true == false
    }


    def "test 408 timeout wiremock"() {
        expect:"fix me"
            true == false
    }
}
