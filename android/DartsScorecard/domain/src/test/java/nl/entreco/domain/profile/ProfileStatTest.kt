package nl.entreco.domain.profile

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileStatTest {

    private lateinit var subject: ProfileStat

    @Test
    fun getId() {
        givenSubject(id = 180)
        assertEquals(180, subject.playerId)
    }

    @Test
    fun getNumberOfGamesPlayed() {
        givenSubject(numberOfGames = 8)
        assertEquals(8, subject.numberOfGames)
    }

    @Test
    fun getNumberOfDarts() {
        givenSubject(numberOfDarts = 18)
        assertEquals(18, subject.numberOfDarts)
    }

    @Test
    fun getNumberOf180s() {
        givenSubject(numberOf180s = 3)
        assertEquals(3, subject.numberOf180s)
    }

    private fun givenSubject(id: Long = -2, numberOfGames: Int = -2, numberOfDarts: Int = -2, numberOf180s: Int = -2) {
        subject = ProfileStat(id, numberOfGames, 4, numberOfDarts, 4, numberOf180s)
    }

}