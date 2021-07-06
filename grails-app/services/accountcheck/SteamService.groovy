package accountcheck

import groovy.json.JsonSlurper
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder

import static accountcheck.secrets.secrets.steamToken

@SuppressWarnings("CatchException")
//we want to capture all
class SteamService {

    protected static String STEAM_API_URL = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/"

    Object steanProfile(String steamId64) {
        def jsonSlurper = new JsonSlurper()

        HttpClient client = HttpClient.create(STEAM_API_URL.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(STEAM_API_URL)
                .queryParam("key", steamToken())
                .queryParam("steamids", steamId64)
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
