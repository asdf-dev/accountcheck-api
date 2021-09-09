package accountcheck

import accountcheck.model.Player
import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification

class PlayerDataBinderSpec extends Specification {

    @Shared
    JsonSlurper jsonSlurper


    @Shared
    PlayerDataBinder playerDataBinder

    def setup() {
        playerDataBinder = new PlayerDataBinder()
        jsonSlurper = new JsonSlurper()

    }

    def "we can bind data to player"() {
        given:
            Object faceitSearch = jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitSearchOk.json"))
            Object faceitStats = jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitStatsOK.json"))
            Object steam = jsonSlurper.parseText(getFile("src/test/resources/steam/SteamPublic.json"))

        and: "we add game count"
            steam.response.players[0].game_count = 2

        when:
            Player player = playerDataBinder.playerBuilder(faceitSearch, faceitStats, steam)

        then:
            with(player.faceit) {
                player_id == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                country == "DK"
                status == "AVAILABLE"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                with(games[0]) {
                    name == "csgo"
                    skill_level == 5
                }
                with(it.faceitStats) {
                    Average_Headshots == "31"
                    Matches == "114"
                    avg_kd_ratio == "1.14"
                }
                faceit_url == "https://www.faceit.com/en/players/voodoo-csgo"
            }

            with(player.steam) {
                steamid == "123"
                loccountrycode == "DK"
                profileurl == "https://steamcommunity.com/id/itsmem9k/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21.jpg"
                avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_medium.jpg"
                avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_full.jpg"
                lastlogoff == 1625517135
                accountAge == 17
                communityvisibilitystate == 3
                game_count == 2
            }

    }

    def "we can bind data to player no stats found"() {
        given:
            Object faceitSearch = jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitSearchOk.json"))
            Object steam = jsonSlurper.parseText(getFile("src/test/resources/steam/SteamPublic.json"))

        when:
            Player player = playerDataBinder.playerBuilder(faceitSearch, null, steam)

        then:
            with(player.faceit) {
                player_id == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                country == "DK"
                status == "AVAILABLE"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                with(games[0]) {
                    name == "csgo"
                    skill_level == 5
                }
                faceit_url == "https://www.faceit.com/en/players/voodoo-csgo"
            }

            with(player.steam) {
                steamid == "123"
                loccountrycode == "DK"
                profileurl == "https://steamcommunity.com/id/itsmem9k/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21.jpg"
                avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_medium.jpg"
                avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_full.jpg"
                lastlogoff == 1625517135
                accountAge == 17
                communityvisibilitystate == 3
            }

    }

    def "we can bind data to player facit not sound"() {
        given:
            Object steam = jsonSlurper.parseText(getFile("src/test/resources/steam/SteamPublic.json"))

        when:
            Player player = playerDataBinder.playerBuilder(null, null, steam)

        then:
            with(player.steam) {
                steamid == "123"
                loccountrycode == "DK"
                profileurl == "https://steamcommunity.com/id/itsmem9k/"
                avatar == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21.jpg"
                avatarmedium == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_medium.jpg"
                avatarfull == "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/0a/0a7b23ce1e3fa2d517c3c54507b7977aa7fb1d21_full.jpg"
                lastlogoff == 1625517135
                accountAge == 17
                communityvisibilitystate == 3
            }

    }

    def "we some how cant find the steam profile"() {
        given:
            Object faceitSearch = jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitSearchOk.json"))
            Object faceitStats = jsonSlurper.parseText(getFile("src/test/resources/faceit/FaceitStatsOK.json"))

        when:
            Player player = playerDataBinder.playerBuilder(faceitSearch, faceitStats, null)

        then:
            with(player.faceit) {
                player_id == "6afd3089-c4cf-49fd-b07d-6afcf79c71cc"
                nickname == "voodoo-csgo"
                country == "DK"
                status == "AVAILABLE"
                verified == false
                avatar == "https://assets.faceit-cdn.net/avatars/6afd3089-c4cf-49fd-b07d-6afcf79c71cc_1550430232933.jpg"
                with(games[0]) {
                    name == "csgo"
                    skill_level == 5
                }
                with(it.faceitStats) {
                    Average_Headshots == "31"
                    Matches == "114"
                    avg_kd_ratio == "1.14"
                }
                faceit_url == "https://www.faceit.com/en/players/voodoo-csgo"
            }


    }

    def "we can convert times correct"() {
        when:
            Long timestamp = givenTimestamp

        then:
            expectedYear == playerDataBinder.getYearFromUnix(timestamp)

        where:
            givenTimestamp | expectedYear
            1090952696     | 17
            1090952900     | 17
            963478048      | 21
    }


    String getFile(String path) {
        new File(path).text
    }

}
