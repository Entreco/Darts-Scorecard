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
        val rand = botRepository.create(Bot.Rand.displayName, Bot.Rand.level)
        val noob = botRepository.create(Bot.Noob.displayName, Bot.Noob.level)
        val easy = botRepository.create(Bot.Easy.displayName, Bot.Easy.level)
        val midi = botRepository.create(Bot.Midi.displayName, Bot.Midi.level)
        val oki = botRepository.create(Bot.Oki.displayName, Bot.Oki.level)
        val gui = botRepository.create(Bot.Gui.displayName, Bot.Gui.level)
        val pro = botRepository.create(Bot.Pro.displayName, Bot.Pro.level)
    }
}