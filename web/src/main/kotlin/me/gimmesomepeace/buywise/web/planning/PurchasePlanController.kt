package me.gimmesomepeace.buywise.web.planning

import jakarta.validation.Valid
import me.gimmesomepeace.buywise.application.planning.plan.CreatePurchasePlanUseCase
import me.gimmesomepeace.buywise.domain.planning.PurchasePlanningResult
import me.gimmesomepeace.buywise.web.planning.purchase.CreatePurchasePlanRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
internal class PurchasePlanController(
    private val createPurchasePlanUseCase: CreatePurchasePlanUseCase,
) {
    @PostMapping("/purchase-plan")
    suspend fun purchasePlan(
        @Valid @RequestBody request: CreatePurchasePlanRequest,
    ): ResponseEntity<PurchasePlanningResult> {
        val result =
            createPurchasePlanUseCase.execute(
                request.storeCountLimit,
            )
        return ResponseEntity.ok(result)
    }
}
