package accountcheck

import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer
import com.github.tomakehurst.wiremock.junit.WireMockRule
import grails.testing.mixin.integration.Integration
import org.junit.Rule
import rest.RestBackendSpec
import spock.lang.Shared

import javax.inject.Inject

import static accountcheck.secrets.secrets.steamToken
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

@Integration
class AccountControllerRestSpec extends RestBackendSpec {

    String resursePath

    @Inject
    FaceitService faceitService

    @Inject
    SteamService steamService

    @Shared
    String path = "http://127.0.0.1:8989/"


    @Rule
    WireMockRule wireMockRule = new WireMockRule(options().port(8989).extensions(new ResponseTemplateTransformer(false)))


    def setup() {
        resursePath = "accountcheck"

        faceitService.SEARCH_FOR_PLAYERS = path
        faceitService.PLAYER_STATS = path

        steamService.STEAM_API_URL_PROFILE
        steamService.STEAM_API_URL_GAME

        wireMockRule.resetAll()
        wireMockRule.start()
    }

    //setup wiremock for:
    // green no faceit.
    // faceit up no steam,
    // steam ok faceit down

    //forkerte kald
    // forkert request format

    def "green path"() {
        given:
            String requestJson = """{"steam64":[76561197967742127]}"""


            wireMockRule.stubFor(get(urlEqualTo("/?nickname=76561197967742127"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitSearchOk.json"))))

        and:

            wireMockRule.stubFor(get(urlEqualTo("/?steamids=76561197967742127&key=${steamToken()}"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/steam/SteamPublic.json"))))

        and:

            wireMockRule.stubFor(get(urlEqualTo("//6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json charset=utf-8")
                            .withBody(getFile("src/test/resources/faceit/FaceitStatsOK.json"))))

        when:
            def response = httpPost(requestJson)
            Object returnBody = parseResponseBodyToJson(response)

        then:
            returnBody.data.size() == 1

        and:
            with(returnBody.data[0].Player.faceit) {
                playerId == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                country == "DK"
                status == "AVAILABLE"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
//                    "games" == "{ArrayList@18875}  size = 1"
//                    "faceitStats" == "{LazyMap@18877}  size = 3"
                faceitUrl == "https://www.faceit.com/en/players/voodoo-csgo"
            }
            with(returnBody.data[0].Player.steam) {
                steamId == "76561197967742127"
                locCountryCode == null
                profileUrl == "https://steamcommunity.com/id/itsmem9k/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/73/73031901f28894dde7e8fa6a9481c14e897f298b.jpg"
                avatarMedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/73/73031901f28894dde7e8fa6a9481c14e897f298b_medium.jpg"
                avatarFull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/73/73031901f28894dde7e8fa6a9481c14e897f298b_full.jpg"
                lastLogoff == 1630073666
                accountAge == null
                communityVisibilityState == 3
                gameCount == 187
            }
    }

    String getFile(String path) {
        new File(path).text
    }
}
