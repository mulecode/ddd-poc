package uk.co.mulecode.ddd.domain.model

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
    val upcCode: String?
    var supplier: String?
    var brand: String?
    var name: String?
    var description: String?
    var category: String?
    var subCategory: String?
    var status: ProductStatus?
}

interface Product {
    val id: UUID
    val upcCode: String
    var supplier: String
    var brand: String
    var name: String
    var description: String
    var category: String
    var subCategory: String
    var status: ProductStatus
}

class ProductModel(
    val product: Product,
    var sellingDetails: SellingDetails? = null,
    var dimensions: Dimensions? = null,
    var inventory: Inventory? = null
) : BaseModel() {

    companion object {
        @JvmStatic
        fun create(
            upcCode: String,
            supplier: String,
            brand: String,
            name: String,
            description: String,
            category: String,
            subCategory: String,
        ) = ProductModel(
            product = object : Product {
                override val id: UUID = sortedUuid()
                override val upcCode: String = upcCode
                override var supplier: String = supplier
                override var brand: String = brand
                override var name: String = name
                override var description: String = description
                override var category: String = category
                override var subCategory: String = subCategory
                override var status: ProductStatus = ProductStatus.INACTIVE
            }
        )
    }
}

class ProductListModel(
    val data: List<ProductModel>,
    val pagination: ModelListPageDetails
)
