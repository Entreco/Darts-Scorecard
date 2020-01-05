package nl.entreco.domain.setup.players

import nl.entreco.domain.model.players.Bot
import nl.entreco.domain.repository.BotRepository
import nl.entreco.shared.BaseUsecase
import nl.entreco.shared.threading.Background
import nl.entreco.shared.threading.Foreground
import javax.inject.Inject

class FetchBotsUsecase @Inject constructor(
        private var botRepository: BotRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    fun exec(done: (FetchBotsResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({
            val existingBots = botRepository.fetchAll()
            if(existingBots.isEmpty()){
                // Create all supported bots now
                insertBots()
                val bots = botRepository.fetchAll()
                onUi { done(FetchBotsResponse(bots)) }
            } else {
                // Bots already created -> return them now
                onUi { done(FetchBotsResponse(existingBots)) }
            }
        }, fail)
    }

    private fun insertBots() {
        val easy = botRepository.create(Bot.Easy.displayName)
        val pro = botRepository.create(Bot.Pro.displayName)
    }
}