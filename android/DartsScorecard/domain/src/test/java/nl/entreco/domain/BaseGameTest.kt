package nl.entreco.domain

import org.junit.Assert

/**
 * Created by Entreco on 11/11/2017.
 */
abstract class BaseGameTest(val subject: Game = Game()) {

    protected fun whenDartsThrown(vararg turns: Turn) {
        for(turn in turns) {
            subject.handle(turn)
        }
    }

    protected fun givenGameStarted() {
        subject.start()
    }

    protected fun assertScore(score1: Int, score2: Int, leg: Int = 0) {
        Assert.assertEquals("$score1, $score2 (leg:$leg)", subject.score)
    }

    protected fun sixty() = Turn(20, 20, 20)
    protected fun oneEighty() = Turn(60, 60, 60)
}