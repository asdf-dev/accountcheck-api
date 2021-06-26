package rest

import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.junit.WireMockRule
import groovy.json.JsonSlurper
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import org.junit.Rule
import spock.lang.Specification

import static io.micronaut.http.HttpMethod.GET
import static io.micronaut.http.HttpRequest.create
import static com.github.tomakehurst.wiremock.client.WireMock.*

abstract class RestBackendSpec extends Specification {

    abstract String getResursePath()

    @Rule
    WireMockRule wireMockRule

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


    void setupWireMockResponse(String url, String responseBody, int statusCode) {
        wireMockRule.stubFor(post(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json; charset=UTF-8")
                        .withBody(responseBody)))
    }

    void setupWireMockFault(String url, Fault fault) {
        wireMockRule.stubFor(post(urlEqualTo(url))
                .willReturn(aResponse()
                        .withFault(fault)))
    }


}
