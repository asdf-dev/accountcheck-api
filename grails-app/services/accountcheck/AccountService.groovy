package accountcheck

import accountcheck.model.Player
import accountcheck.model.RequestList
import grails.gorm.services.Service
import groovyx.gpars.GParsPool


@Service
class AccountService {

    FaceitService faceitService
    SteamService steamService

    PlayerDataBinder playerDataBinder


    List<Player> findPlayers(RequestList requestList) {
        playerDataBinder = new PlayerDataBinder()

        List<Player> players = []

        GParsPool.withPool {
            requestList.steam64.eachParallel {
                Object faceitSearch = faceitService.searchForPlayer(it)
                String faceitId = faceitSearch?.items[0]?.player_id


                Object faceitStats

                if (faceitId) {
                    faceitStats = faceitService.playerStats(faceitId)
                }

                Object steamProfile = steamService.steamProfile(it)

                if (steamProfile?.response?.players?.getAt(0)?.communityvisibilitystate == 3) {
                    Integer gameCount = steamService?.steamGames(it)?.response?.game_count ?: null
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

