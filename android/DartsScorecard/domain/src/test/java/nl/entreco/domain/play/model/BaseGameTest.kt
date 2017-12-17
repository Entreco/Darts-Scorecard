package nl.entreco.domain.play.model

import nl.entreco.domain.play.TestProvider
import org.junit.Assert

/**
 * Created by Entreco on 11/11/2017.
 */
abstract class BaseGameTest(val subject: Game = Game(0, TestProvider().arbiter())) {

    protected fun whenDartsThrown(vararg turns: Turn) {
        for(turn in turns) {
            subject.handle(turn)
        }
    }

    protected fun givenGameStarted() {
        subject.start()
    }

    protected fun assertScore(score1: Int, score2: Int, leg: Int = 0) {
        Assert.assertEquals(score1, subject.scores[0].score)
        Assert.assertEquals(score2, subject.scores[1].score)
        Assert.assertEquals(leg, subject.scores[0].leg + subject.scores[1].leg)
    }

    protected fun sixty() = Turn(Dart.SINGLE_20, Dart.SINGLE_20, Dart.SINGLE_20)
    protected fun oneEighty() = Turn(Dart.TRIPLE_20, Dart.TRIPLE_20, Dart.TRIPLE_20)
}