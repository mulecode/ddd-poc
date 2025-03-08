package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.ProductDto
import uk.co.mulecode.ddd.application.dto.ProductListDto
import uk.co.mulecode.ddd.application.dto.ProductRegistrationRequest
import uk.co.mulecode.ddd.application.dto.UserFilterRequest
import uk.co.mulecode.ddd.application.dto.dto
import uk.co.mulecode.ddd.domain.model.ProductFilter
import uk.co.mulecode.ddd.domain.model.ProductListModel
import uk.co.mulecode.ddd.domain.model.ProductModel
import uk.co.mulecode.ddd.domain.repository.ProductRepository
import java.util.UUID

@Service
class ProductService(
    val productRepository: ProductRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun registerProduct(newProduct: ProductRegistrationRequest): ProductDto {
        log.debug { "Creating new product: $newProduct" }
        return productRepository.save(
            ProductModel.create(
                upcCode = newProduct.upcCode,
                supplier = newProduct.supplier,
                brand = newProduct.brand,
                name = newProduct.name,
                description = newProduct.description,
                category = newProduct.category,
                subCategory = newProduct.subCategory,
            )
        ).dto()
    }

    @Transactional(readOnly = true)
    fun getProductById(productId: UUID): ProductDto {
        log.debug { "Getting product by id: $productId" }
        return productRepository.findById(productId).dto()
    }

    @Transactional(readOnly = true)
    fun getAllProducts(pageable: Pageable, filter: ProductFilter): ProductListDto {
        log.debug { "Getting all products" }
        return productRepository.findAll(pageable, filter).let { model ->
            ProductListDto(
                products = model.data.map { it.dto() },
                page = model.pagination.page,
                totalPages = model.pagination.totalPages,
                size = model.pagination.size,
                totalElements = model.pagination.totalElements
            )
        }
    }

}


