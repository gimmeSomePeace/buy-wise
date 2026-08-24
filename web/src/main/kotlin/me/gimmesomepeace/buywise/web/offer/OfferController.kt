package me.gimmesomepeace.buywise.web.offer

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import me.gimmesomepeace.buywise.application.offer.create.CreateOfferUseCase
import me.gimmesomepeace.buywise.application.offer.delete.DeleteOfferUseCase
import me.gimmesomepeace.buywise.application.offer.get.GetOfferUseCase
import me.gimmesomepeace.buywise.application.offer.list.ListOffersUseCase
import me.gimmesomepeace.buywise.application.offer.price.change.ChangeOfferPriceUseCase
import me.gimmesomepeace.buywise.application.shared.Cursor
import me.gimmesomepeace.buywise.application.shared.PageRequest
import me.gimmesomepeace.buywise.domain.offer.OfferId
import me.gimmesomepeace.buywise.domain.product.ProductId
import me.gimmesomepeace.buywise.domain.shared.Money
import me.gimmesomepeace.buywise.domain.store.StoreId
import me.gimmesomepeace.buywise.domain.user.UserId
import me.gimmesomepeace.buywise.web.offer.create.CreateOfferRequest
import me.gimmesomepeace.buywise.web.offer.list.ListOffersResponse
import me.gimmesomepeace.buywise.web.offer.update.ChangePriceRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
@Validated
@RequestMapping("/offers")
internal open class OfferController(
    private val getOfferUseCase: GetOfferUseCase,
    private val createOfferUseCase: CreateOfferUseCase,
    private val changeOfferPriceUseCase: ChangeOfferPriceUseCase,
    private val deleteOfferUseCase: DeleteOfferUseCase,
    private val listOffersUseCase: ListOffersUseCase,
) {
    @GetMapping("/{id}")
    open suspend fun get(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: OfferId,
    ): ResponseEntity<OfferDetailsResponse> {
        val offer =
            getOfferUseCase.execute(
                UserId(userId),
                id,
            )
        return ResponseEntity.ok(
            offer.toDetailsResponse(),
        )
    }

    @GetMapping
    open suspend fun list(
        @AuthenticationPrincipal userId: UUID,
        @RequestParam(
            value = "page_size",
            defaultValue = "20",
        ) @Positive pageSize: Int,
        @RequestParam(
            value = "page_token",
            required = false,
        ) pageToken: String?,
    ): ResponseEntity<ListOffersResponse> {
        val cursor = pageToken?.let { Cursor(it) }
        val request =
            PageRequest(pageSize, cursor)
        val result =
            listOffersUseCase
                .execute(UserId(userId), request)
                .toListOffersResponse()
        return ResponseEntity.ok(result)
    }

    @PostMapping
    open suspend fun create(
        @Valid @RequestBody request: CreateOfferRequest,
    ): ResponseEntity<OfferDetailsResponse> {
        val offer =
            createOfferUseCase.execute(
                productId =
                    ProductId(
                        request.productId,
                    ),
                storeId =
                    StoreId(
                        request.storeId,
                    ),
                unitPrice =
                    Money(
                        request.unitPrice,
                        request.currency,
                    ),
            )
        return ResponseEntity
            .created(
                URI("/offers/${offer.id.value}"),
            ).body(offer.toDetailsResponse())
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun changePrice(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: OfferId,
        @Valid @RequestBody request: ChangePriceRequest,
    ) {
        changeOfferPriceUseCase.execute(
            userId = UserId(userId),
            offerId = id,
            newPrice =
                Money(
                    request.newPrice,
                    request.newCurrency,
                ),
        )
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    open suspend fun delete(
        @AuthenticationPrincipal userId: UUID,
        @PathVariable id: OfferId,
    ) {
        deleteOfferUseCase.execute(
            userId = UserId(userId),
            offerId = id,
        )
    }
}
