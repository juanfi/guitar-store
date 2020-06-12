package guitar.store.domain

import guitar.store.domain.auth.UserId
import guitar.store.domain.card.Card
import guitar.store.domain.auth.UserId
import guitar.store.domain.card.Card
import squants.market.Money

object payment {
  final case class Payment(id: UserId, total: Money, card: Card)
}
