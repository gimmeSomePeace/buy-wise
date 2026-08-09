package me.gimmesomepeace.buywise.infrastructure.persistence.shared

import com.github.f4b6a3.uuid.UuidCreator
import me.gimmesomepeace.buywise.application.shared.IdGenerator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UuidV7Generator : IdGenerator<UUID> {
    override fun generate(): UUID =
        UuidCreator.getTimeOrderedEpoch()
}
