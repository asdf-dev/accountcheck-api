package accountcheck

import grails.plugin.cache.Cacheable
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
    protected static String PLAYER_STATS = "https://open.faceit.com/data/v4/players"
    private final static String AUTHORIZATION = "Authorization"
    //https://open.faceit.com/data/v4/players/FACEIT_ID/stats/csgo


    @Cacheable('faceitplayer')
    Object searchForPlayer(String steamId64) {
        def jsonSlurper = new JsonSlurper()

        HttpClient client = HttpClient.create(SEARCH_FOR_PLAYERS.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(SEARCH_FOR_PLAYERS)
                .queryParam("nickname", steamId64)
                .build())
        request.header(AUTHORIZATION, faceitToken())
        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String)
            return jsonSlurper.parseText(response.body())
        }
        catch (Exception e) {
            log.error("searchForPlayer failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }

    @Cacheable('faceitstats')
    Object playerStats(String faceitID, String game = "csgo") {
        def jsonSlurper = new JsonSlurper()

        String url = "$PLAYER_STATS/$faceitID/stats/$game"

        HttpClient client = HttpClient.create(url.toURL())
        HttpRequest request = HttpRequest.GET(UriBuilder.of(url)
                .build())
        request.header(AUTHORIZATION, faceitToken())
        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String)
            return jsonSlurper.parseText(response.body())
        }
        catch (Exception e) {
            log.error("playerStats failed with error: $e.response.status.reason")
            return jsonSlurper.parseText("""{"message": "${e.response.status.reason}"}""")
        }
    }
}
