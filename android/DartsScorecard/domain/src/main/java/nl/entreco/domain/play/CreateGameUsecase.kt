package nl.entreco.domain.play

import javax.inject.Inject

/**
 * Created by Entreco on 14/11/2017.
 */
open class CreateGameUsecase @Inject constructor(private val arbiter: Arbiter) {
    open fun start(): Game = Game(arbiter).apply { start() }
}