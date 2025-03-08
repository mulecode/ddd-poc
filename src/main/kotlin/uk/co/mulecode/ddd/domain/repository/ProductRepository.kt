package uk.co.mulecode.ddd.domain.repository

import org.springframework.data.domain.Pageable
import uk.co.mulecode.ddd.domain.model.ProductFilter
import uk.co.mulecode.ddd.domain.model.ProductListModel
import uk.co.mulecode.ddd.domain.model.ProductModel
import java.util.UUID

interface ProductRepository {
    fun findById(id: UUID): ProductModel
    fun save(productModel: ProductModel): ProductModel
    fun findAll(pageable: Pageable, filter: ProductFilter?): ProductListModel
}
