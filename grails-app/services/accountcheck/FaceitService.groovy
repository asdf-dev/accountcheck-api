package accountcheck

import groovy.json.JsonSlurper
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder
import jdk.incubator.jpackage.internal.Log

import static accountcheck.secrets.secrets.faceitToken

class FaceitService {

    private static final String SEARCH_FOR_PLAYERS = "https://open.faceit.com/data/v4/search/players"


    Object searchForPlayer(String steamId64) {
        def jsonSlurper = new JsonSlurper()

        HttpClient client = HttpClient.create(SEARCH_FOR_PLAYERS.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(SEARCH_FOR_PLAYERS)
                .queryParam("nickname", steamId64)
                .build())
        request.header("Authorization", faceitToken())
        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String)
            return jsonSlurper.parseText(response.body())
        }
        catch (Exception e) {
            Log.error("searchForPlayer failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }

}
