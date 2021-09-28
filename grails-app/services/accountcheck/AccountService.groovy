package accountcheck


import accountcheck.model.Player
import accountcheck.model.RequestId
import grails.gorm.services.Service
import groovyx.gpars.GParsPool


@Service
class AccountService {

    FaceitService faceitService
    SteamService steamService
    SteamIdHandler steamIdHandler = new SteamIdHandler()

    PlayerDataBinder playerDataBinder

    List<Player> findPlayers(String plainText) {

         List<RequestId> requestList = steamIdHandler.findBasicSteamId(plainText)


        playerDataBinder = new PlayerDataBinder()

        List<Player> players = []

        GParsPool.withPool {
            requestList.eachParallel {
                Object faceitSearch = faceitService.searchForPlayer(it.steam64)
                String faceitId = faceitSearch?.items[0]?.player_id


                Object faceitStats

                if (faceitId) {
                    faceitStats = faceitService.playerStats(faceitId)
                }

                Object steamProfile = steamService.steamProfile(it.steam64)

                if (steamProfile?.response?.players?.getAt(0)?.communityvisibilitystate == 3) {
                    Integer gameCount = steamService?.steamGames(it.steam64)?.response?.game_count ?: null
                    steamProfile.response.players[0].game_count = gameCount
                }

                Player player = playerDataBinder.playerBuilder(faceitSearch, faceitStats, steamProfile)

                if (player) {
                    players.add(player)
                }
            }
        }
        players
    }


}

