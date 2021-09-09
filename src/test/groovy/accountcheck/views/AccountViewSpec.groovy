package accountcheck.views

import accountcheck.model.Faceit
import accountcheck.model.FaceitStats
import accountcheck.model.Games
import accountcheck.model.Player
import accountcheck.model.Steam
import grails.plugin.json.view.test.JsonRenderResult
import grails.plugin.json.view.test.JsonViewTest
import spock.lang.Specification

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals

class AccountViewSpec extends Specification implements JsonViewTest {


    def "green view accountcheck"() {

        given:
            FaceitStats faceitStats = new FaceitStats(
                    Average_Headshots: "34%",
                    Matches: 203,
                    avg_kd_ratio: "0.4"
            )
            Games games = new Games(
                    name: "csgo",
                    skill_level: 1
            )
            Faceit faceit = new Faceit(
                    player_id: "playerid",
                    nickname: "nickname",
                    country: "DK",
                    status: "stats",
                    verified: false,
                    avatar: "avatar",
                    games: [games, games],
                    faceitStats: faceitStats,
                    faceit_url: "www.faceit.com"
            )
            Steam steam = new Steam(
                    steamid: "steamid",
                    loccountrycode: "DK",
                    profileurl: "url",
                    avatar: "avatar",
                    avatarmedium: "medium",
                    avatarfull: "full",
                    lastlogoff: 1234,
                    accountAge: 23,
                    communityvisibilitystate: 3,
                    game_count: 2
            )
            Player player = new Player(
                    steam: steam,
                    faceit: faceit
            )
        String expected = getFile('src/test/resources/viewTest/accountcheckGreen.json')
        when:
            JsonRenderResult renderResult = render(view: "account/index", model: [players: [player, player]])
            def actucal = renderResult.jsonText

        then:
            assertJsonEquals(expected, actucal)
    }

    String getFile(String path) {
        new File(path).text
    }

}
