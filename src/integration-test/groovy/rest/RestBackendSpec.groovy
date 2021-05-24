package rest

import groovy.json.JsonSlurper
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import spock.lang.Specification

import static io.micronaut.http.HttpMethod.GET
import static io.micronaut.http.HttpRequest.create

abstract class RestBackendSpec extends Specification {

    abstract String getResursePath()

    JsonSlurper slurper = new JsonSlurper()


    String getBaseUrl() {
        return "http://localhost:$serverPort"
    }


    HttpResponse<String> httpGET() {
        return createCall(GET, "${getBaseUrl()}/$resursePath")
    }

    BlockingHttpClient getClient() {
        HttpClient.create("${getBaseUrl()}".toURL()).toBlocking()
    }

    private HttpResponse<String> createCall(HttpMethod method, String uri, def body = null) {
        def request
        if (body) {
            request = create(method, uri).body(body).header('x-cvr', "$cvr")
            return client.exchange(request, String)
        }
        request = create(method, uri)
        return client.exchange(request, String)
    }

    Object parseResponseBodyToJson(HttpResponse response) {
        return slurper.parseText(response.body.get().toString())
    }


}
