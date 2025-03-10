package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.ModelListPageDetails
import uk.co.mulecode.ddd.domain.model.ProductFilter
import uk.co.mulecode.ddd.domain.model.ProductListModel
import uk.co.mulecode.ddd.domain.model.ProductModel
import uk.co.mulecode.ddd.domain.model.ProductStatus
import uk.co.mulecode.ddd.domain.model.ProductVariationModel
import uk.co.mulecode.ddd.domain.model.ProductVariationSpecification
import uk.co.mulecode.ddd.domain.repository.ProductRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductVariationEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductVariationRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductVariationSpecificationEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaProductVariationSpecificationRepository
import java.util.UUID

@Component
class ProductRepositoryImpl(
    private val jpaProductRepository: JpaProductRepository,
    private val jpaProductVariationRepository: JpaProductVariationRepository,
    private val jpaProductVariationSpecificationRepository: JpaProductVariationSpecificationRepository
) : ProductRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun findById(id: UUID): ProductModel {
        return jpaProductRepository.findByIdOrNull(id)
            ?.let { product ->
                val allVariations = jpaProductVariationRepository.findAllByProductId(product.id)
                val idToSpecifications = jpaProductVariationSpecificationRepository
                    .findAllByProductIdAndVariationIdIsIn(product.id, allVariations.map { it.id })
                    .groupBy { it.variationId }
                ProductModel(
                    product = product,
                    variations = allVariations.map { productVariation ->
                        ProductVariationModel(
                            productVariation = productVariation,
                            specifications = idToSpecifications[productVariation.id]
                                ?.toMutableList()
                                ?: mutableListOf()
                        )
                    }
                )
            }
            ?: throw IllegalArgumentException("Product not found for id $id")
    }

    @Transactional
    override fun save(productModel: ProductModel): ProductModel {
        log.debug { "Saving product: $productModel" }
        val entity = if (productModel.product is JpaProductEntity) {
            jpaProductRepository.save(productModel.product)
        } else {
            jpaProductRepository.save(productModel.jpaEntity())
        }

        productModel.variations?.forEach { variation ->
            jpaProductVariationRepository.save(variation.productVariation as JpaProductVariationEntity)
            log.debug { "Saving variation: $variation total specifications ${variation.specifications?.size}" }
            variation.specifications?.forEach { spec ->
                if (spec is JpaProductVariationSpecificationEntity) {
                    jpaProductVariationSpecificationRepository.save(spec)
                } else {
                    jpaProductVariationSpecificationRepository.save(
                        spec.jpaEntity(
                            productId = entity.id,
                            variationId = variation.productVariation.id
                        )
                    )
                }
            }
            val allNames = variation.specifications?.map { it.name } ?: emptyList()
            jpaProductVariationSpecificationRepository.deleteAllByNameNotInAndProductIdAndVariationId(
                names = allNames,
                productId = entity.id,
                variationId = variation.productVariation.id
            )
        }
        productModel.prospectVariations.forEach { variation ->
            jpaProductVariationRepository.save(variation.jpaEntity(entity.id))
            variation.specifications?.forEach { spec ->
                jpaProductVariationSpecificationRepository.save(
                    spec.jpaEntity(
                        productId = entity.id,
                        variationId = variation.productVariation.id
                    )
                )
            }
        }

        return findById(entity.id)
    }

    override fun findAll(pageable: Pageable, filter: ProductFilter?): ProductListModel {
        log.debug { "Retrieving all products with filter $filter" }
        return jpaProductRepository.findAll(filter?.toSpecification(), pageable).let {
            log.debug { "Found ${it.content.size} products in the database" }
            ProductListModel(data = it.content.map { productEntity ->
                ProductModel(
                    product = productEntity
                )
            }, pagination = object : ModelListPageDetails {
                override var page: Int = it.number
                override var totalPages: Int = it.totalPages
                override var size: Int = it.size
                override var totalElements: Long = it.totalElements
            })
        }
    }
}


fun ProductModel.jpaEntity(): JpaProductEntity {
    return JpaProductEntity(
        id = this.product.id,
        code = this.product.code,
        manufacturer = this.product.manufacturer,
        supplier = this.product.supplier,
        brand = this.product.brand,
        name = this.product.name,
        description = this.product.description,
        category = this.product.category,
        subCategory = this.product.subCategory,
        status = this.product.status,
        originCountryCode = this.product.originCountryCode,
    )
}

fun ProductVariationModel.jpaEntity(productId: UUID): JpaProductVariationEntity {
    return JpaProductVariationEntity(
        id = this.productVariation.id,
        productId = productId,
        upcCode = this.productVariation.upcCode,
        name = this.productVariation.name,
        description = this.productVariation.description,
        status = this.productVariation.status,
    )
}

fun ProductVariationSpecification.jpaEntity(
    productId: UUID,
    variationId: UUID
): JpaProductVariationSpecificationEntity {
    return JpaProductVariationSpecificationEntity(
        productId = productId,
        variationId = variationId,
        name = this.name,
        value = this.value,
        unit = this.unit
    )
}

fun JpaProductVariationEntity.model(specifications: MutableList<ProductVariationSpecification>): ProductVariationModel {
    return ProductVariationModel(
        productVariation = this,
        specifications = specifications
    )
}


fun ProductFilter.toSpecification(): Specification<JpaProductEntity> {
    return ProductSpecification.withFilter(this)
}

object ProductSpecification {
    fun withFilter(filter: ProductFilter): Specification<JpaProductEntity> {
        return Specification { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            filter.id?.let { predicates.add(criteriaBuilder.equal(root.get<UUID>("id"), it)) }
            filter.code?.let { predicates.add(criteriaBuilder.equal(root.get<String>("code"), it)) }
            filter.manufacturer?.let { predicates.add(criteriaBuilder.equal(root.get<String>("manufacturer"), it)) }
            filter.supplier?.let { predicates.add(criteriaBuilder.equal(root.get<String>("supplier"), it)) }
            filter.brand?.let { predicates.add(criteriaBuilder.equal(root.get<String>("brand"), it)) }
            filter.name?.let { predicates.add(criteriaBuilder.equal(root.get<String>("name"), it)) }
            filter.description?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%${it}%"))
            }
            filter.category?.let { predicates.add(criteriaBuilder.equal(root.get<String>("category"), it)) }
            filter.subCategory?.let { predicates.add(criteriaBuilder.equal(root.get<String>("subCategory"), it)) }
            filter.originCountryCode?.let {
                predicates.add(
                    criteriaBuilder.equal(
                        root.get<String>("originCountryCode"), it
                    )
                )
            }
            filter.status?.let { predicates.add(criteriaBuilder.equal(root.get<ProductStatus>("status"), it)) }
            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}
