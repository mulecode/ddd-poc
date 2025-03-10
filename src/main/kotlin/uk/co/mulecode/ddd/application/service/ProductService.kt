package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.ProductDto
import uk.co.mulecode.ddd.application.dto.ProductListDto
import uk.co.mulecode.ddd.application.dto.ProductRegistrationRequest
import uk.co.mulecode.ddd.application.dto.ProductVariationRegistrationRequest
import uk.co.mulecode.ddd.application.dto.ProductVariationSpecificationRequest
import uk.co.mulecode.ddd.application.dto.ProductVariationUpdateRequest
import uk.co.mulecode.ddd.application.dto.dto
import uk.co.mulecode.ddd.domain.model.ProductFilter
import uk.co.mulecode.ddd.domain.model.ProductModel
import uk.co.mulecode.ddd.domain.model.ProductVariationModel
import uk.co.mulecode.ddd.domain.model.ProductVariationSpecification
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
                manufacturer = newProduct.manufacturer,
                supplier = newProduct.supplier,
                brand = newProduct.brand,
                name = newProduct.name,
                description = newProduct.description,
                category = newProduct.category,
                subCategory = newProduct.subCategory,
                originCountryCode = newProduct.originCountryCode,
            )
        ).dto()
    }

    @Transactional
    fun updateProduct(productId: UUID, updatedProduct: ProductRegistrationRequest): ProductDto {
        log.debug { "Updating product: $productId" }
        return productRepository.findById(productId)
            .let { model ->
                model.product.name = updatedProduct.name
                model.product.description = updatedProduct.description
                model.product.manufacturer = updatedProduct.manufacturer
                model.product.supplier = updatedProduct.supplier
                model.product.brand = updatedProduct.brand
                model.product.category = updatedProduct.category
                model.product.subCategory = updatedProduct.subCategory
                model.product.originCountryCode = updatedProduct.originCountryCode
                productRepository.save(model)
            }.dto()
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

    @Transactional
    fun registerVariation(productId: UUID, request: ProductVariationRegistrationRequest): ProductDto {
        log.debug { "Registering product variation for product: $productId" }
        return productRepository.findById(productId)
            .let { model ->
                model.addVariation(
                    ProductVariationModel.create(
                        upc = request.upcCode,
                        name = request.name,
                        description = request.description,
                    )
                )
                productRepository.save(model)
            }.dto()
    }

    @Transactional
    fun updateProductVariation(productId: UUID, variationId: UUID, request: ProductVariationUpdateRequest): ProductDto {
        log.debug { "Updating product variation for product: $productId" }
        return productRepository.findById(productId)
            .let { model ->
                model.updateVariationDetails(
                    variationId = variationId,
                    name = request.name,
                    description = request.description,
                )
                productRepository.save(model)
            }.dto()
    }

    @Transactional
    fun updateProductVariationSpecs(
        productId: UUID,
        variationId: UUID,
        request: ProductVariationSpecificationRequest
    ): ProductDto {
        log.debug { "Updating product variation specs for product: $productId" }
        return productRepository.findById(productId)
            .let { model ->
                model.getVariation(variationId).updateSpecifications(
                    specifications = request.specifications.map {
                        object : ProductVariationSpecification {
                            override val name = it.name
                            override var value = it.value
                            override var unit = it.unit
                        }
                    }
                )
                productRepository.save(model)
            }.dto()
    }
}


