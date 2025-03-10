package uk.co.mulecode.ddd.interfaces.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class AppControllerAdvice {

    private val log = KotlinLogging.logger { }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> {
        val errors = mutableMapOf<String, String?>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        val response = mutableMapOf<String, Any?>()
        response["message"] = "Validation failed"
        response["errors"] = errors
        response["status"] = HttpStatus.BAD_REQUEST.value()
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationExceptions(ex: ConstraintViolationException): ResponseEntity<Map<String, Any?>> {
        val errors = mutableMapOf<String, String?>()
        ex.constraintViolations.forEach { violation ->
            val fieldName = violation.propertyPath.toString()
            val errorMessage = violation.message
            errors[fieldName] = errorMessage
        }
        val response = mutableMapOf<String, Any?>()
        response["message"] = "Validation failed"
        response["errors"] = errors
        response["status"] = HttpStatus.BAD_REQUEST.value()
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentExceptions(ex: IllegalArgumentException): ResponseEntity<Map<String, Any?>> {
        val response = mutableMapOf<String, Any?>()
        response["message"] = ex.message
        response["status"] = HttpStatus.BAD_REQUEST.value()
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleExceptions(ex: Exception): ResponseEntity<Map<String, Any?>> {
        log.error(ex) { "An error occurred ${ex.message}" }
        val response = mutableMapOf<String, Any?>()
        response["message"] = ex.message
        response["status"] = HttpStatus.INTERNAL_SERVER_ERROR.value()
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
