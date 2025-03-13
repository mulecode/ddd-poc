package uk.co.mulecode.ddd.application.dto

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.application.validator.UniqueSpecificationName
import uk.co.mulecode.ddd.domain.model.ProductModel
import uk.co.mulecode.ddd.domain.model.ProductStatus
import uk.co.mulecode.ddd.domain.model.ProductVariationModel
import uk.co.mulecode.ddd.domain.model.ProductVariationSpecification
import uk.co.mulecode.ddd.domain.model.ProductVariationStatus
import uk.co.mulecode.ddd.infrastructure.validator.ValidUPC
import java.util.UUID


data class ProductListDto(
    val products: List<ProductDto>,
    val page: Int,
    val totalPages: Int,
    val size: Int,
    val totalElements: Long
)

data class ProductDto(
    val id: UUID,
    val code: String,
    val manufacturer: String,
    var supplier: String,
    var brand: String,
    var name: String,
    var description: String,
    var category: String,
    var subCategory: String,
    var originCountryCode: String,
    var status: ProductStatus,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var variations: List<ProductVariationDto>? = null,
)

data class ProductVariationDto(
    var id: UUID,
    var upcCode: String,
    var name: String,
    var description: String,
    var specificationsInLine: String? = null,
    var specifications: List<ProductVariationSpecificationDto>? = null,
    var status: ProductVariationStatus,
)

data class ProductVariationSpecificationDto(
    var specName: String,
    var specValue: String,
    var specUnit: String,
)

data class ProductRegistrationRequest(
    @field:NotBlank(message = "manufacturer is required")
    @field:Size(min = 5, max = 50, message = "manufacturer must be between 5 and 50 characters")
    val manufacturer: String,

    @field:NotBlank(message = "Supplier is required")
    @field:Size(min = 5, max = 50, message = "Supplier must be between 5 and 50 characters")
    val supplier: String,

    @field:NotBlank(message = "brand is required")
    @field:Size(min = 5, max = 50, message = "brand must be between 5 and 50 characters")
    val brand: String,

    @field:NotBlank(message = "name is required")
    @field:Size(min = 5, max = 50, message = "name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "description is required")
    @field:Size(min = 5, max = 254, message = "description must be between 5 and 254 characters")
    val description: String,

    @field:NotBlank(message = "category is required")
    @field:Size(min = 5, max = 50, message = "category must be between 5 and 254 characters")
    val category: String,

    @field:NotBlank(message = "subCategory is required")
    @field:Size(min = 5, max = 50, message = "subCategory must be between 5 and 254 characters")
    val subCategory: String,

    @field:NotBlank(message = "originCountryCode is required")
    @field:Size(min = 2, max = 2, message = "originCountryCode must be between 5 and 2 characters")
    val originCountryCode: String,
)

data class ProductVariationRegistrationRequest(
    @field:NotBlank(message = "upc is required")
    @field:ValidUPC(message = "upc is invalid")
    val upcCode: String,

    @field:NotBlank(message = "name is required")
    @field:Size(min = 5, max = 50, message = "name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "description is required")
    @field:Size(min = 5, max = 254, message = "description must be between 5 and 254 characters")
    val description: String,
)

data class ProductVariationUpdateRequest(
    @field:NotBlank(message = "name is required")
    @field:Size(min = 5, max = 50, message = "name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "description is required")
    @field:Size(min = 5, max = 254, message = "description must be between 5 and 254 characters")
    val description: String,
)

@UniqueSpecificationName
data class ProductVariationSpecificationRequest(

    @field:Valid
    val specifications: List<Specification>

) {
    data class Specification(
        @field:NotBlank(message = "name is required")
        @field:Size(min = 3, max = 10, message = "name must be between 3 and 10 characters")
        val name: String,

        @field:NotBlank(message = "value is required")
        @field:Size(min = 1, max = 10, message = "value must be between 1 and 10 characters")
        val value: String,

        @field:NotBlank(message = "unit is required")
        @field:Size(min = 1, max = 10, message = "unit must be between 1 and 10 characters")
        val unit: String,
    )
}


fun ProductModel.dto(): ProductDto {
    return ProductDto(
        id = this.product.id,
        code = this.product.code,
        manufacturer = this.product.manufacturer,
        supplier = this.product.supplier,
        brand = this.product.brand,
        name = this.product.name,
        description = this.product.description,
        category = this.product.category,
        subCategory = this.product.subCategory,
        originCountryCode = this.product.originCountryCode,
        status = this.product.status,
        variations = this.variations?.map { it.dto() }
    )
}

fun ProductVariationModel.dto(): ProductVariationDto {
    return ProductVariationDto(
        id = this.productVariation.id,
        upcCode = this.productVariation.upcCode,
        name = this.productVariation.name,
        description = this.productVariation.description,
        status = this.productVariation.status,
        specificationsInLine = this.specifications?.joinToString { "${it.name}: ${it.value}${it.unit}" },
        specifications = this.specifications?.map { it.dto() }
    )
}

fun ProductVariationSpecification.dto(): ProductVariationSpecificationDto {
    return ProductVariationSpecificationDto(
        specName = this.name,
        specValue = this.value,
        specUnit = this.unit
    )
}
