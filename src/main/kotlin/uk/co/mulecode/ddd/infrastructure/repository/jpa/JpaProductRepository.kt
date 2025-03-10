package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface JpaProductRepository : JpaRepository<JpaProductEntity, UUID>,
    JpaSpecificationExecutor<JpaProductEntity>


@Repository
interface JpaProductVariationRepository : JpaRepository<JpaProductVariationEntity, UUID>,
    JpaSpecificationExecutor<JpaProductVariationEntity> {
    fun findAllByProductId(productId: UUID): List<JpaProductVariationEntity>
}

@Repository
interface JpaProductVariationSpecificationRepository :
    JpaRepository<JpaProductVariationSpecificationEntity, JpaProductVariationSpecificationId>,
    JpaSpecificationExecutor<JpaProductVariationSpecificationEntity> {

    fun findAllByProductIdAndVariationIdIsIn(
        productId: UUID,
        variationIds: List<UUID>
    ): MutableList<JpaProductVariationSpecificationEntity>

    fun deleteAllByNameNotInAndProductIdAndVariationId(
        names: List<String>,
        productId: UUID,
        variationId: UUID
    ): Int
}
