package nl.entreco.domain.play

import org.junit.Assert

/**
 * Created by Entreco on 11/11/2017.
 */
abstract class BaseGameTest(val subject: Game = Game(Arbiter(Score(), 2))) {

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

    protected fun sixty() = Turn(20, 20, 20)
    protected fun oneEighty() = Turn(60, 60, 60)
}