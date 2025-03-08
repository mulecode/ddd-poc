package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.randomBase36Id
import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.sortedUuid
import java.math.BigDecimal
import java.util.UUID

interface Dimensions {
    var weight: Double
    val width: Double
    val height: Double
    val depth: Double
}

enum class ProductStatus {
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
    val manufacturer: String
    var supplier: String
    var brand: String
    var name: String
    var description: String
    var category: String
    var subCategory: String
    var originCountryCode: String
    var status: ProductStatus
}

class ProductModel(
    val product: Product,
) : BaseModel() {

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
                override val manufacturer: String = manufacturer
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
