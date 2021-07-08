package accountcheck

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import grails.testing.services.ServiceUnitTest
import org.junit.Rule
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.matching
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import static accountcheck.secrets.secrets.steamToken


class SteamServiceSpec extends Specification implements ServiceUnitTest<SteamService> {

    @Rule
    WireMockRule wireMockRule = new WireMockRule(options().port(8989).extensions(new ResponseTemplateTransformer(false)))

    @Shared
    String path = "http://127.0.0.1:8989/"

    def setup() {
        service.STEAM_API_URL_PROFILE = path
        service.STEAM_API_URL_GAME = path

        wireMockRule.resetAll()
        wireMockRule.start()
    }


    def "ok kald steamProfile Public"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/steam/SteamPublic.json"))))

        when:
            def response = service.steamProfile(steamid)

        then:
            with(response.response.players[0]) {
                steamid == "123"
                communityvisibilitystate == 3
                profilestate == 1
                personaname == "★ m9K"
                commentpermission == 2
                profileurl == "https://steamcommunity.com/id/itsmem9k/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21.jpg"
                avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_medium.jpg"
                avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_full.jpg"
                avatarhash == "0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21"
                lastlogoff == 1625517135
                personastate == 0
                primaryclanid == "103582791429521408"
                timecreated == 1090952696
                personastateflags == 0
                loccountrycode == "DK"
            }
    }

    def "ok kald steamProfile Public not found"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/steam/SteamNotFound.json"))))

        when:
            def response = service.steamProfile(steamid)

        then:
            response.response.players.empty
    }

    def "ok kald steamProfile Private Profile"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/steam/SteamPrivateProfile.json"))))

        when:
            def response = service.steamProfile(steamid)

        then:
            with(response.response.players[0]) {
                steamid == "123"
                communityvisibilitystate == 1
                profilestate == 1
                personaname == "masturbinh0"
                profileurl == "https://steamcommunity.com/profiles/76561199087210787/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/89/89b245bde76b74676170f151d74e215140f360a7.jpg"
                avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/89/89b245bde76b74676170f151d74e215140f360a7_medium.jpg"
                avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/89/89b245bde76b74676170f151d74e215140f360a7_full.jpg"
                avatarhash == "89b245bde76b74676170f151d74e215140f360a7"
                lastlogoff == 1621801348
                personastate == 0
                personastateflags == 0
            }
    }

    def "404 steamProfile"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(404)))

        when:
            def response = service.steamProfile(steamid)

        then:
            response.message == "Not Found"

    }

    def "timeout  steamProfile"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(408)))

        when:
            def response = service.steamProfile(steamid)

        then:
            response.message == "Request Timeout"


    }

    def "404 steamGame"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamid=123&format=json&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(404)))

        when:
            def response = service.steamGames(steamid)

        then:
            response.message == "Not Found"

    }

    def "timeout  steamGame"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamid=123&format=json&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(408)))

        when:
            def response = service.steamGames(steamid)

        then:
            response.message == "Request Timeout"

    }


    def "internal server error  steamGame"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamid=123&format=json&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(500)))

        when:
            def response = service.steamGames(steamid)

        then:
            response.message == "Internal Server Error"

    }

    def "ok kald steamGame Public"() {
        given:
            String steamid = "123"

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=123&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/steam/SteamPublicGames.json"))))

        when:
            def response = service.steamProfile(steamid)

        then:
            with(response.response) {
                game_count == 2
                games.size == 2
            }
    }

    String getFile(String path) {
        new File(path).text
    }

}
