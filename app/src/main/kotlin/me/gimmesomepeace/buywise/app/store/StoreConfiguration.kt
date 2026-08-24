package me.gimmesomepeace.buywise.app.store

import me.gimmesomepeace.buywise.app.BeanNames
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import me.gimmesomepeace.buywise.application.store.StoreQuery
import me.gimmesomepeace.buywise.application.store.create.CreateStoreUseCase
import me.gimmesomepeace.buywise.application.store.delete.DeleteStoreUseCase
import me.gimmesomepeace.buywise.application.store.get.GetStoreUseCase
import me.gimmesomepeace.buywise.application.store.list.ListStoresUseCase
import me.gimmesomepeace.buywise.application.store.rename.RenameStoreUseCase
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.store.StoreRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreJpaRepository
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreQueryImpl
import me.gimmesomepeace.buywise.infrastructure.persistence.store.StoreRepositoryImpl
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StoreConfiguration {
    @Bean
    fun storeRepository(storeJpaRepository: StoreJpaRepository): StoreRepository =
        StoreRepositoryImpl(storeJpaRepository)

    @Bean
    fun storeQuery(storeJpaRepository: StoreJpaRepository): StoreQuery = StoreQueryImpl(storeJpaRepository)

    @Bean
    fun getStoreUseCase(storeQuery: StoreQuery): GetStoreUseCase = GetStoreUseCase(storeQuery)

    @Bean
    fun createStoreUseCase(
        @Qualifier(BeanNames.STORE_ID_GENERATOR)
        idGenerator: IdGenerator<StoreId>,
        repository: StoreRepository,
    ) = CreateStoreUseCase(
        idGenerator,
        repository,
    )

    @Bean
    fun deleteStoreUseCase(repository: StoreRepository) = DeleteStoreUseCase(repository)

    @Bean
    fun listStoresUseCase(query: StoreQuery) = ListStoresUseCase(query)

    @Bean
    fun renameStoreUseCase(repository: StoreRepository) = RenameStoreUseCase(repository)
}
