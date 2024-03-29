package accountcheck

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import grails.testing.services.ServiceUnitTest
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

class FaceitServiceSpec extends Specification implements ServiceUnitTest<FaceitService> {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(options().port(8989).extensions(new ResponseTemplateTransformer(false)))

    @Shared
    String path = "http://127.0.0.1:8989/"

    def setup() {
        service.SEARCH_FOR_PLAYERS = path
        service.PLAYER_STATS = path

        wireMockRule.resetAll()
        wireMockRule.start()
    }

    def "ok kald search faceit player"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?nickname=$steamid"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitSearchOk.json"))))

        when:
            def response = service.searchForPlayer(steamid)

        then:
            with(response.items[0]) {
                player_id == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                status == "AVAILABLE"
                country == "DK"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                with(games[0]) {
                    name == "csgo"
                    skill_level == "5"
                }
            }
    }

    def "ok kald faceit search player banned wiremock"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?nickname=$steamid"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitSearchOkBannedPlayer.json"))))

        when:
            def response = service.searchForPlayer(steamid)

        then:
            with(response.items[0]) {
                player_id == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                status == "banned"
                country == "DK"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                with(games[0]) {
                    name == "csgo"
                    skill_level == "5"
                }
            }
    }

    def "faceit search kald not found wiremock"() {
        given:
            String steamid = "0"

            wireMockRule.stubFor(get(urlEqualTo("/?nickname=$steamid"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitSearchOkEmpty.json"))))

        when:
            def response = service.searchForPlayer(steamid)

        then:
            response.items.empty

    }

    def "faceit search 404 kald wiremock"() {
        given:
            String steamid = "404"

            wireMockRule.stubFor(get(urlEqualTo("/?nickname=$steamid"))
                    .willReturn(aResponse()
                            .withStatus(404)))

        when:
            def response = service.searchForPlayer(steamid)

        then:
            response.message == "Not Found"
    }

    def "faceit search 408 timeout wiremock"() {
        given:
            String steamid = "408"

            wireMockRule.stubFor(get(urlEqualTo("/?nickname=$steamid"))
                    .willReturn(aResponse()
                            .withStatus(408)))

        when:
            def response = service.searchForPlayer(steamid)

        then:
            response.message == "Request Timeout"
    }

    //stats
    def "faceit search for player ok"() {
        given:
            String facieId = "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"

            wireMockRule.stubFor(get(urlEqualTo("//6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitStatsOK.json"))))

        when:
            def response = service.playerStats(facieId)

        then:
            def lifetime = response.lifetime


            lifetime.get("Average K/D Ratio") == "1.14"
            lifetime.get("Matches") == "114"
            lifetime.get("Average Headshots %") == "31"
    }

    def "faceit search for player ok not found"() {
        given:
            String facieId = "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"

            wireMockRule.stubFor(get(urlEqualTo("//6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitStatsOKEmpty.json"))))

        when:
            def response = service.playerStats(facieId)

        then:
            response.errors[0].message == "The resource was not found."
    }

    def "faceit search for player not found"() {
        given:
            String facieId = "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"

            wireMockRule.stubFor(get(urlEqualTo("//6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo"))
                    .willReturn(aResponse()
                            .withStatus(404)))

        when:
            def response = service.playerStats(facieId)

        then:
            response.message == "Not Found"
    }

    def "faceit search for player time out"() {
        given:
            String facieId = "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"

            wireMockRule.stubFor(get(urlEqualTo("//6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo"))
                    .willReturn(aResponse()
                            .withStatus(408)))

        when:
            def response = service.playerStats(facieId)

        then:
            response.message == "Request Timeout"

    }

    String getFile(String path) {
        new File(path).text
    }
}
