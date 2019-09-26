package nl.entreco.domain.setup.players

import nl.entreco.domain.model.players.Bot
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class FetchBotsUsecase @Inject constructor(bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchBotsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val bots = listOf(Bot(), Bot())
            onUi { done(FetchBotsResponse(bots)) }
        }, fail)
    }
}