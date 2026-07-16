package me.gimmesomepeace.buywise.planning

import me.gimmesomepeace.buywise.planning.plan.PurchasePlan

/**
 * Результат построения плана покупки.
 */
sealed interface PurchasePlanningResult {
    /**
     * План покупки успешно построен.
     *
     * @property plans Предлагаемые планы покупки.
     */
    data class Success(
        val plans: List<PurchasePlan>,
    ) : PurchasePlanningResult

    /**
     * Построить план покупки не удалось.
     */
    data object Impossible : PurchasePlanningResult
}
