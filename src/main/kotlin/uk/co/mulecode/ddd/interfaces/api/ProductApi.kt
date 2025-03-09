package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.ProductDto
import uk.co.mulecode.ddd.application.dto.ProductListDto
import uk.co.mulecode.ddd.application.dto.ProductRegistrationRequest
import uk.co.mulecode.ddd.application.dto.ProductVariationRegistrationRequest
import uk.co.mulecode.ddd.application.dto.ProductVariationUpdateRequest
import uk.co.mulecode.ddd.domain.model.ProductFilter
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/products")
@Validated
interface ProductApi {

    @PostMapping
    fun registerProduct(
        @Valid @RequestBody request: ProductRegistrationRequest
    ): CompletableFuture<ProductDto>

    @PutMapping("/{productId}")
    fun updateProduct(
        @PathVariable productId: UUID,
        @Valid @RequestBody request: ProductRegistrationRequest
    ): CompletableFuture<ProductDto>

    @GetMapping("/{productId}")
    fun getProductById(
        @PathVariable productId: UUID
    ): CompletableFuture<ProductDto>

    @GetMapping
    fun getAllProducts(
        @Valid @ModelAttribute queryParams: ProductFilter,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): CompletableFuture<ProductListDto>

    @PostMapping("/{productId}/variations")
    fun registerProductVariation(
        @PathVariable productId: UUID,
        @Valid @RequestBody request: ProductVariationRegistrationRequest
    ): CompletableFuture<ProductDto>

    @PutMapping("/{productId}/variations/{variationId}")
    fun updateProductVariation(
        @PathVariable productId: UUID,
        @PathVariable variationId: UUID,
        @Valid @RequestBody request: ProductVariationUpdateRequest
    ): CompletableFuture<ProductDto>
}
