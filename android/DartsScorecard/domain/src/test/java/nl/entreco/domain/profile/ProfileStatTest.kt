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
    @Test
    fun getNumberOf140s() {
        givenSubject(numberOf140s = 3)
        assertEquals(3, subject.numberOf140s)
    }
    @Test
    fun getNumberOf100s() {
        givenSubject(numberOf100s = 3)
        assertEquals(3, subject.numberOf100s)
    }
    @Test
    fun getNumberOf60s() {
        givenSubject(numberOf60s = 3)
        assertEquals(3, subject.numberOf60s)
    }
    @Test
    fun getNumberOf20s() {
        givenSubject(numberOf20s = 3)
        assertEquals(3, subject.numberOf20s)
    }
    @Test
    fun getNumberOf10s() {
        givenSubject(numberOf0s = 3)
        assertEquals(3, subject.numberOf0s)
    }

    private fun givenSubject(id: Long = -2, numberOfGames: Int = -2, numberOfDarts: Int = -2, numberOf180s: Int = -2, numberOf140s: Int = -2, numberOf100s: Int = -2, numberOf60s: Int = -2, numberOf20s: Int = -2, numberOf0s: Int = -2) {
        subject = ProfileStat(id, numberOfGames, 4, numberOfDarts, 4, numberOf180s, numberOf140s, numberOf100s, numberOf60s, numberOf20s, numberOf0s)
    }

}