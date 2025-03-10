package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.Product
import uk.co.mulecode.ddd.domain.model.ProductStatus
import uk.co.mulecode.ddd.domain.model.ProductVariation
import uk.co.mulecode.ddd.domain.model.ProductVariationSpecification
import uk.co.mulecode.ddd.domain.model.ProductVariationStatus
import uk.co.mulecode.ddd.infrastructure.validator.ValidUPC
import java.io.Serializable
import java.util.UUID

@Entity
@Table(name = "product")
class JpaProductEntity(
    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    override val id: UUID,
    @NotBlank
    @Column(name = "code", unique = true, updatable = false, nullable = false)
    override val code: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "manufacturer", unique = false, updatable = true, nullable = false)
    override var manufacturer: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "supplier", unique = false, updatable = true, nullable = false)
    override var supplier: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "brand", unique = false, updatable = true, nullable = false)
    override var brand: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "name", unique = true, updatable = true, nullable = false)
    override var name: String,
    @NotBlank
    @Size(min = 5, max = 254)
    @Column(name = "description", unique = false, updatable = true, nullable = false)
    override var description: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "category", unique = false, updatable = true, nullable = false)
    override var category: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "sub_category", unique = false, updatable = true, nullable = false)
    override var subCategory: String,
    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "origin_country_code", unique = false, updatable = true, nullable = false)
    override var originCountryCode: String,
    @Enumerated(EnumType.STRING)
    override var status: ProductStatus,
) : Product, JpaAuditingBase()


@Entity
@Table(name = "product_variation")
class JpaProductVariationEntity(
    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    override val id: UUID,
    @NotNull
    @Column(name = "product_id", unique = false, updatable = false, nullable = false)
    val productId: UUID,
    @NotBlank
    @ValidUPC
    @Column(name = "upc_code", unique = true, updatable = true, nullable = false)
    override val upcCode: String,
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "name", unique = true, updatable = true, nullable = false)
    override var name: String,
    @NotBlank
    @Size(min = 5, max = 254)
    @Column(name = "description", unique = false, updatable = true, nullable = false)
    override var description: String,
    @Enumerated(EnumType.STRING)
    override var status: ProductVariationStatus
) : ProductVariation, JpaAuditingBase()

data class JpaProductVariationSpecificationId(
    val productId: UUID = UUID.randomUUID(),
    val variationId: UUID = UUID.randomUUID(),
    val name: String = ""
) : Serializable

@Entity
@Table(name = "product_variation_specification")
@IdClass(JpaProductVariationSpecificationId::class)
class JpaProductVariationSpecificationEntity(
    @Id
    @Column(name = "product_id", updatable = false, nullable = false)
    val productId: UUID,
    @Id
    @Column(name = "variation_id", updatable = false, nullable = false)
    val variationId: UUID,
    @Id
    @NotBlank
    @Size(min = 5, max = 50)
    @Column(name = "spec_name", updatable = false, nullable = false)
    override val name: String,
    @NotBlank
    @Size(min = 5, max = 254)
    @Column(name = "spec_value", unique = false, updatable = true, nullable = false)
    override var value: String,
    @NotBlank
    @Size(min = 5, max = 254)
    @Column(name = "spec_unit", unique = false, updatable = true, nullable = false)
    override var unit: String,
) : ProductVariationSpecification, JpaAuditingBase()
