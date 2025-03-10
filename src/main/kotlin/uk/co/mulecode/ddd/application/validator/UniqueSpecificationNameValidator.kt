package uk.co.mulecode.ddd.application.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import uk.co.mulecode.ddd.application.dto.ProductVariationSpecificationRequest
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueSpecificationNameValidator::class])
annotation class UniqueSpecificationName(
    val message: String = "Specification names must be unique",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class UniqueSpecificationNameValidator :
    ConstraintValidator<UniqueSpecificationName, ProductVariationSpecificationRequest> {
    override fun isValid(value: ProductVariationSpecificationRequest?, context: ConstraintValidatorContext?): Boolean {
        if (value == null || value.specifications.isEmpty()) return true

        val names = value.specifications.map { it.name }
        return names.size == names.toSet().size
    }
}
