package uk.co.mulecode.ddd.interfaces.config.controller

import jakarta.servlet.AsyncEvent
import jakarta.servlet.AsyncListener
import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.WriteListener
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

interface PreRequestHandler {
    fun handle(request: CachedBodyHttpServletRequest)
}

interface PostRequestHandler {
    fun handle(response: CachedBodyHttpServletResponse)
}

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ControllerLoggingFilter(
    private val preRequestHandlers: List<PreRequestHandler>,
    private val postRequestHandlers: List<PostRequestHandler>
) : HttpFilter() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        if (request is HttpServletRequest && response is HttpServletResponse) {
            val wrappedRequest = CachedBodyHttpServletRequest(request)
            val wrappedResponse = CachedBodyHttpServletResponse(request, response)

            preRequestHandlers.forEach { it.handle(wrappedRequest) }

            chain.doFilter(wrappedRequest, wrappedResponse)

            if (request.isAsyncStarted) {
                // If the request is async, defer logging until completion
                request.asyncContext.addListener(object : AsyncListener {
                    override fun onComplete(event: AsyncEvent) {
                        postRequestHandlers.forEach { it.handle(wrappedResponse) }
                        wrappedResponse.flushBuffer()
                    }

                    override fun onTimeout(event: AsyncEvent?) {}
                    override fun onError(event: AsyncEvent?) {}
                    override fun onStartAsync(event: AsyncEvent?) {}
                })
            } else {
                // Normal request, log immediately
                postRequestHandlers.forEach { it.handle(wrappedResponse) }
                wrappedResponse.flushBuffer()
            }
        } else {
            chain.doFilter(request, response)
        }
    }
}

class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val cachedBody: ByteArray

    init {
        val inputStream = request.inputStream
        this.cachedBody = inputStream.readBytes()
    }

    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(cachedBody)
    }

    fun bodyAsString(): String = String(cachedBody, Charsets.UTF_8)
}

class CachedBodyServletInputStream(body: ByteArray) : ServletInputStream() {
    private val inputStream = ByteArrayInputStream(body)

    override fun read(): Int = inputStream.read()
    override fun isFinished(): Boolean = inputStream.available() == 0
    override fun isReady(): Boolean = true
    override fun setReadListener(listener: ReadListener?) { /* Not implemented */
    }
}

class CachedBodyHttpServletResponse(
    private val request: HttpServletRequest,
    response: HttpServletResponse
) : HttpServletResponseWrapper(response) {
    private val captureStream = ByteArrayOutputStream()
    private val servletOutputStream = object : ServletOutputStream() {
        override fun write(b: Int) {
            captureStream.write(b)
        }

        override fun isReady() = true
        override fun setWriteListener(listener: WriteListener?) {}
    }
    private val writer = PrintWriter(OutputStreamWriter(captureStream, StandardCharsets.UTF_8))

    override fun getOutputStream(): ServletOutputStream = servletOutputStream

    override fun getWriter(): PrintWriter = writer

    override fun flushBuffer() {
        writer.flush()
        val responseBytes = captureStream.toByteArray()

        // Ensure the response body is written back to the original response
        response.outputStream.write(responseBytes)
        response.outputStream.flush()

        super.flushBuffer()
    }

    fun getRequest(): HttpServletRequest = request

    fun bodyAsString(): String {
        writer.flush()  // Ensure all buffered data is written
        return captureStream.toString(StandardCharsets.UTF_8.name())
    }

}
