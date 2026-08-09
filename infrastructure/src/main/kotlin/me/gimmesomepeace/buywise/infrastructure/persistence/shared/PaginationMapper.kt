package me.gimmesomepeace.buywise.infrastructure.persistence.shared

import me.gimmesomepeace.buywise.application.shared.PageRequest
import org.springframework.data.domain.Pageable

internal fun PageRequest.toPageable() = Pageable.ofSize(this.pageSize)
