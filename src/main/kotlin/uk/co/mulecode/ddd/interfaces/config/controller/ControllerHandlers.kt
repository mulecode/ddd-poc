package uk.co.mulecode.ddd.interfaces.config.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Order(1)
@Component
class AppRequestIdPreRequest : PreRequestHandler {

    private val log = KotlinLogging.logger { }

    override fun handle(request: CachedBodyHttpServletRequest) {
        val appUniqueId = UUID.randomUUID().toString()
        log.debug { "Generated appUniqueId: $appUniqueId" }
        request.request.setAttribute("appUniqueId", appUniqueId)
    }
}

@Order(2)
@Component
class LoggerPreRequest : PreRequestHandler {

    private val log = KotlinLogging.logger { }

    override fun handle(request: CachedBodyHttpServletRequest) {
        val requestBody = request.bodyAsString()
        val appRequestId = request.getAttribute("appUniqueId") as String
        val headers = request.headerNames.toList().joinToString { "$it: ${request.getHeader(it)}" }
        log.debug {
            "=> Request [$appRequestId]: ${request.method} ${request.requestURI}, Headers: $headers, Body: $requestBody"
        }
    }
}

@Order(2)
@Component
class LoggerPostRequest : PostRequestHandler {

    private val log = KotlinLogging.logger { }

    override fun handle(response: CachedBodyHttpServletResponse) {
        val responseBody = response.bodyAsString()
        val request = response.getRequest()
        val appRequestId = request.getAttribute("appUniqueId") as? String
        log.debug { "<= Response [$appRequestId]: ${request.method} ${request.requestURI} - ${response.status} $responseBody" }
    }
}

@Order(3)
@Component
class AppRequestIdEnricherRequest : PostRequestHandler {

    private val log = KotlinLogging.logger { }

    override fun handle(response: CachedBodyHttpServletResponse) {
        val request = response.getRequest()
        val appRequestId = request.getAttribute("appUniqueId") as? String

        if (appRequestId != null) {
            response.setHeader("App-Unique-Id", appRequestId)
            log.debug { "Added appRequestId to response: $appRequestId" }
        } else {
            log.warn { "appRequestId not found in request attributes!" }
        }
    }
}
