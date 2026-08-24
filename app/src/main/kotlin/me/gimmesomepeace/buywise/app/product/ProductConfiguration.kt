package me.gimmesomepeace.buywise.app.product

import me.gimmesomepeace.buywise.app.BeanNames
import me.gimmesomepeace.buywise.application.product.ProductQuery
import me.gimmesomepeace.buywise.application.product.create.CreateProductUseCase
import me.gimmesomepeace.buywise.application.product.delete.DeleteProductUseCase
import me.gimmesomepeace.buywise.application.product.list.ListProductsUseCase
import me.gimmesomepeace.buywise.application.product.rename.RenameProductUseCase
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.product.ProductRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.product.ProductJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.product.ProductQueryImpl
import me.gimmesomepeace.buywise.infrastructure.persistence.product.ProductRepositoryImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductConfiguration {
    @Bean
    fun productRepository(productJpaRepository: ProductJpaRepository): ProductRepository =
        ProductRepositoryImpl(
            productJpaRepository,
        )

    @Bean
    fun productQuery(productJpaRepository: ProductJpaRepository): ProductQuery = ProductQueryImpl(productJpaRepository)

    @Bean
    fun createProductUseCase(
        @Qualifier(BeanNames.PRODUCT_ID_GENERATOR)
        idGenerator: IdGenerator<ProductId>,
        productRepository: ProductRepository,
    ) = CreateProductUseCase(
        idGenerator,
        productRepository,
    )

    @Bean
    fun deleteProductUseCase(repository: ProductRepository) = DeleteProductUseCase(repository)

    @Bean
    fun listProductsUseCase(query: ProductQuery) = ListProductsUseCase(query)

    @Bean
    fun renameProductUseCase(repository: ProductRepository) = RenameProductUseCase(repository)
}
