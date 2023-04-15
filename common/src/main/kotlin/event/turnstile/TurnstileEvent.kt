package event.turnstile

import domain.turnstile.Visit
import event.SingleTargetEvent

abstract class TurnstileEvent(targetId: Long?) : SingleTargetEvent<Visit>(targetId)