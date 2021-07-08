package accountcheck

import accountcheck.model.RequestList
import grails.testing.services.ServiceUnitTest
import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

class AccountServiceSpec extends Specification implements ServiceUnitTest<AccountService> {

/**
 * mock faceitservice & steamservice
 * mock med ingen fundet steam/faceitSearch/faceitStats
 * mock med fejl fra services
 */


    @Inject
    PlayerDataBinder playerDataBinder


    @Shared
    JsonSlurper jsonSlurper

    def setup() {
        jsonSlurper = new JsonSlurper()
    }

    def "we can bind green scenario"() {
        given: "make request list"
            RequestList requestList = new RequestList()
            requestList.steam64 = ["1"]


        and: "mock facie service"

            service.faceitService = Mock(FaceitService) {
                searchForPlayer(_) >> {
                    return jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitSearchOk.json"))
                }
                playerStats(_) >> {
                    return jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitStatsOK.json"))
                }
            }

        and: "mock steam service"
            service.steamService = Mock(SteamService) {
                steamProfile(_) >> {
                    return jsonSlurper.parseText(getFile("src/test/resources/steam/SteamPublic.json"))
                }
            }
        when:
            def response = service.findPlayers(requestList)

        then:
            response[0].with {
                player_id == "more stuff 2 add"
            }
    }

    String getFile(String path) {
        new File(path).text
    }

}
