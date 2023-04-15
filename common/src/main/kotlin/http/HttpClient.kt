package http

import java.net.URI
import java.net.http.HttpClient.*
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpClient(private val baseUrl: String) {
    private val clientImpl = newHttpClient()

    fun req(url: String, paramsMap: Map<String, Any>): String {
        val queryString = paramsMap.toList()
            .joinToString(separator = "&", prefix = "?") { "${it.first}=${it.second}" }
        val uri = URI(baseUrl + url + queryString)
        val req = HttpRequest
            .newBuilder(uri)
            .GET()
            .build()

        return clientImpl.send(req, HttpResponse.BodyHandlers.ofString()).body()
    }
}