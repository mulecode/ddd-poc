package uk.co.mulecode.ddd.interfaces.controller

import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.ProductDto
import uk.co.mulecode.ddd.application.dto.ProductListDto
import uk.co.mulecode.ddd.application.dto.ProductRegistrationRequest
import uk.co.mulecode.ddd.application.service.ProductService
import uk.co.mulecode.ddd.domain.model.ProductFilter
import uk.co.mulecode.ddd.interfaces.api.ProductApi
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Component
class ProductController(
    private val productService: ProductService
) : ProductApi {

    @Async("controllerTreadPoolExecutor")
    override fun registerProduct(request: ProductRegistrationRequest): CompletableFuture<ProductDto> {
        return CompletableFuture.completedFuture(
            productService.registerProduct(request)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun updateProduct(productId: UUID, request: ProductRegistrationRequest): CompletableFuture<ProductDto> {
        return CompletableFuture.completedFuture(
            productService.updateProduct(productId, request)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getProductById(productId: UUID): CompletableFuture<ProductDto> {
        return CompletableFuture.completedFuture(
            productService.getProductById(productId)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getAllProducts(queryParams: ProductFilter, pageable: Pageable): CompletableFuture<ProductListDto> {
        return CompletableFuture.completedFuture(
            productService.getAllProducts(pageable, queryParams)
        )
    }
}
