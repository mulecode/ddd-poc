package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.randomBase36Id
import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.sortedUuid
import java.math.BigDecimal
import java.util.UUID


enum class ProductStatus {
    ACTIVE,
    INACTIVE
}

enum class ProductVariationStatus {
    ACTIVE,
    INACTIVE
}

enum class ProductTaxCategory {
    STANDARD,
    REDUCED,
    ZERO
}

interface Inventory {
    val inventoryId: UUID
    val stockLevelMin: Int
    val stockLevelMax: Int
    val stockLevel: Int
    val reorderLevel: Int
    val reservedStockLevel: Int
    val backorderStockLevel: Int
}

interface SellingDetails {
    // A product can have multiple selling prices
    var sellingPrice1: BigDecimal
    var sellingPrice2: BigDecimal
    var sellingPrice3: BigDecimal

    // tax related fields
    var taxCode: String
    var taxCategory: ProductTaxCategory
    var defaultTaxRate: BigDecimal?
    var isTaxExempt: Boolean
}

interface ProductFilter {
    val id: UUID?
    val code: String?
    var manufacturer: String?
    var supplier: String?
    var brand: String?
    var name: String?
    var description: String?
    var category: String?
    var subCategory: String?
    var originCountryCode: String?
    var status: ProductStatus?
}

interface Product {
    val id: UUID
    val code: String
    var manufacturer: String
    var supplier: String
    var brand: String
    var name: String
    var description: String
    var category: String
    var subCategory: String
    var originCountryCode: String
    var status: ProductStatus
}

interface ProductVariation {
    val id: UUID
    val upcCode: String
    var name: String
    var description: String
    var status: ProductVariationStatus
}

interface ProductVariationSpecification {
    val name: String
    var value: String
    var unit: String
}

class ProductModel(
    val product: Product,
    val variations: List<ProductVariationModel>? = null,
) : BaseModel() {

    val prospectVariations = mutableListOf<ProductVariationModel>()

    fun addVariation(variation: ProductVariationModel) {
        prospectVariations.add(variation)
    }

    fun updateVariationDetails(variationId: UUID, name: String, description: String) {
        variations?.find { it.productVariation.id == variationId }?.updateDetails(name, description)
    }

    fun getVariation(variationId: UUID): ProductVariationModel {
        return variations?.find { it.productVariation.id == variationId }
            ?: throw IllegalArgumentException("Product variation not found")
    }

    companion object {
        @JvmStatic
        fun create(
            manufacturer: String,
            supplier: String,
            brand: String,
            name: String,
            description: String,
            category: String,
            subCategory: String,
            originCountryCode: String,
        ) = ProductModel(
            product = object : Product {
                override val id: UUID = sortedUuid()
                override val code: String = randomBase36Id()
                override var manufacturer: String = manufacturer
                override var supplier: String = supplier
                override var brand: String = brand
                override var name: String = name
                override var description: String = description
                override var category: String = category
                override var subCategory: String = subCategory
                override var originCountryCode: String = originCountryCode
                override var status: ProductStatus = ProductStatus.INACTIVE
            }
        )
    }
}

class ProductListModel(
    val data: List<ProductModel>,
    val pagination: ModelListPageDetails
)

class ProductVariationModel(
    val productVariation: ProductVariation,
    var specifications: MutableList<ProductVariationSpecification>? = null,
) : BaseModel() {

    fun updateDetails(
        name: String,
        description: String,
    ) {
        productVariation.name = name
        productVariation.description = description
    }

    fun updateSpecifications(specifications: List<ProductVariationSpecification>) {
        if (this.specifications == null) {
            throw IllegalStateException("ProductVariationModel specifications is not initialized")
        }

        // update by name
        specifications.forEach { spec ->
            val existingSpec = this.specifications?.find { it.name == spec.name }
            if (existingSpec != null) {
                existingSpec.value = spec.value
                existingSpec.unit = spec.unit
            } else {
                this.specifications?.add(spec)
            }
        }
        // remove by name
        this.specifications?.removeIf { spec -> specifications.none { it.name == spec.name } }
    }

    companion object {
        @JvmStatic
        fun create(
            upc: String,
            name: String,
            description: String,
        ) = ProductVariationModel(
            productVariation = object : ProductVariation {
                override val id: UUID = UUID.randomUUID()
                override val upcCode: String = upc
                override var name: String = name
                override var description: String = description
                override var status: ProductVariationStatus = ProductVariationStatus.INACTIVE
            }
        )
    }
}
