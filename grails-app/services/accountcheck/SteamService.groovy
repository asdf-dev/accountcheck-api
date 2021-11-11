package accountcheck

import grails.plugin.cache.Cacheable
import groovy.json.JsonSlurper
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder

import static accountcheck.secrets.secrets.steamToken

@SuppressWarnings("CatchException")
//we want to capture all
class SteamService {

    protected static String STEAM_API_URL_PROFILE = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"
    protected static String STEAM_API_URL_GAME = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/"

    private final static String KEY_FOR_QUERYPARAM = "key"

    @Cacheable('steamprofile')
    Object steamProfile(String steamId) {
        def jsonSlurper = new JsonSlurper()

        HttpClient client = HttpClient.create(STEAM_API_URL_PROFILE.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(STEAM_API_URL_PROFILE)
                .queryParam(KEY_FOR_QUERYPARAM, steamToken())
                .queryParam("steamids", steamId)
                .build())
        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String)
            return jsonSlurper.parseText(response.body())
        }
        catch (Exception e) {
            log.error("searchForPlayer failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }

    @Cacheable('steamgames')
    Object steamGames(String steamId) {
        def jsonSlurper = new JsonSlurper()

        HttpClient client = HttpClient.create(STEAM_API_URL_GAME.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(STEAM_API_URL_GAME)
                .queryParam("format", "json")
                .queryParam(KEY_FOR_QUERYPARAM, steamToken())
                .queryParam("steamid", steamId)
                .build())
        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String)
            return jsonSlurper.parseText(response.body())
        }
        catch (Exception e) {
            log.error("searchForPlayer failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }
}
