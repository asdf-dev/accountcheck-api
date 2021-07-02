package accountcheck

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder


import static accountcheck.secrets.secrets.faceitToken

@SuppressWarnings("CatchException") //we want to capture all
@Slf4j
class FaceitService {

    protected static String SEARCH_FOR_PLAYERS = "https://open.faceit.com/data/v4/search/players"


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
            log.error("searchForPlayer failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }
    
    
    //do search for stats:
    //https://open.faceit.com/data/v4/players/6afd3089-c4cf-49fd-b07d-6afcf79c71cc/stats/csgo
    //https://open.faceit.com/data/v4/players/FACEIT_ID/csgo
    

}
