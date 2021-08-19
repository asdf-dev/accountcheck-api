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
                steamGames(_) >> {
                    return jsonSlurper.parseText(getFile("src/test/resources/steam/SteamPublicGames.json"))

                }
            }
        when:
            def response = service.findPlayers(requestList)

        then:
            response[0].with {
                    it.faceit.player_id == "more stuff 2 add"
                    it.faceit.nickname == "voodoo-csgo"
                    it.faceit.country == "DK"
                    it.faceit.status == "AVAILABLE"
                    it.faceit.verified == false
                    it.faceit.avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                    it.faceit.games.size == 1

                    it.steam.steamid ==
                    it.steam.loccountrycode == "DK"
                    it.steam.profileurl == "https://steamcommunity.com/id/itsmem9k/"
                    it.steam.avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21.jpg"
                    it.steam.avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_medium.jpg"
                    it.steam.avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_full.jpg"
            }
    }

    String getFile(String path) {
        new File(path).text
    }

}
