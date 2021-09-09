package accountcheck

import accountcheck.model.Faceit
import accountcheck.model.FaceitStats
import accountcheck.model.Player
import accountcheck.model.Steam
import grails.web.databinding.DataBinder

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class PlayerDataBinder implements DataBinder {

    private final static String FACEIT_PROFILE_URL = "https://www.faceit.com/en/players/"

    Player playerBuilder(Object faceitSearch, Object faceitStats, Object steamProfile) {

        Player player = new Player(faceit: new Faceit(), steam: new Steam())

        if (faceitSearch?.items[0]) {
            bindData(player.faceit, faceitSearch.items[0])
            player.faceit.faceit_url = FACEIT_PROFILE_URL + player.faceit.nickname

            if (faceitStats) {
                player.faceit.faceitStats = bindFaceitStats(faceitStats)
            }
        }

        if (steamProfile?.response?.players?.getAt(0)) {
            bindData(player.steam, steamProfile.response.players[0])
            if (player.steam.accountAge) {
                player.steam.accountAge = getYearFromUnix(steamProfile.response.players[0].timecreated)
            }
        }
        return player
    }

    private static FaceitStats bindFaceitStats(Object stats) {
        FaceitStats faceitStats = new FaceitStats()
        if (stats.lifetime) {
            faceitStats.avg_kd_ratio = stats.lifetime.get("Average K/D Ratio")
            faceitStats.Matches = stats.lifetime.get("Matches")
            faceitStats.Average_Headshots = stats.lifetime.get("Average Headshots %")
        }
        return faceitStats
    }

    protected Integer getYearFromUnix(Long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.of('Europe/Copenhagen')).toLocalDate()
        LocalDate.now().year - date.year
    }
}
