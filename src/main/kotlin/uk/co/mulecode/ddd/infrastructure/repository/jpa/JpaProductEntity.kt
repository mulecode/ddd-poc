package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.Product
import uk.co.mulecode.ddd.domain.model.ProductStatus
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
    override val manufacturer: String,
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
