package uk.co.mulecode.ddd.infrastructure.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UPCValidator::class])
annotation class ValidUPC(
    val message: String = "Invalid UPC format",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UPCValidator : ConstraintValidator<ValidUPC, String> {
    override fun isValid(upc: String?, context: ConstraintValidatorContext?): Boolean {
        if (upc.isNullOrBlank()) return false
        if (!upc.matches(Regex("^\\d{12}$"))) return false // Ensure it's 12 digits
        return isValidCheckDigit(upc)
    }

    private fun isValidCheckDigit(upc: String): Boolean {
        val digits = upc.map { it.toString().toInt() }
        val sum = digits.dropLast(1).withIndex().sumOf { (i, n) -> if (i % 2 == 0) n * 3 else n }
        val checkDigit = (10 - (sum % 10)) % 10
        return checkDigit == digits.last()
    }
}
