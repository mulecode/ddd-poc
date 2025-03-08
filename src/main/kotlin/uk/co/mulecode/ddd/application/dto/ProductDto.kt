package uk.co.mulecode.ddd.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.ProductModel
import uk.co.mulecode.ddd.domain.model.ProductStatus
import java.util.UUID


data class  ProductListDto(
    val products: List<ProductDto>,
    val page: Int,
    val totalPages: Int,
    val size: Int,
    val totalElements: Long
)

data class ProductDto(
    val id: UUID,
    val upcCode: String,
    var supplier: String,
    var brand: String,
    var name: String,
    var description: String,
    var category: String,
    var subCategory: String,
    var status: ProductStatus,
)

data class ProductRegistrationRequest(
    @field:NotBlank(message = "UPC is required")
    @field:Size(min = 5, max = 50, message = "Upc must be between 5 and 50 characters")
    val upcCode: String,

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
)


fun ProductModel.dto(): ProductDto {
    return ProductDto(
        id = this.product.id,
        upcCode = this.product.upcCode,
        supplier = this.product.supplier,
        brand = this.product.brand,
        name = this.product.name,
        description = this.product.description,
        category = this.product.category,
        subCategory = this.product.subCategory,
        status = this.product.status,
    )
}
