package nl.entreco.domain.profile

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileStatTest {

    private lateinit var subject: ProfileStat

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
    fun getNumberOfWins() {
        givenSubject(numberOfWins = 18)
        assertEquals(18, subject.numberOfWins)
    }

    @Test
    fun getNumberOfPoints() {
        givenSubject(numberOfPoints = 1800)
        assertEquals(1800, subject.numberOfPoints)
    }

    @Test
    fun getNumberOfDarts9() {
        givenSubject(numberOfDarts9 = 9)
        assertEquals(9, subject.numberOfDarts9)
    }

    @Test
    fun getNumberOfPoints9() {
        givenSubject(numberOfPoints9 = 11)
        assertEquals(11, subject.numberOfPoints9)
    }

    @Test
    fun getNumberOfDartsAtFinish() {
        givenSubject(numberOfDartsAtFinish = 66)
        assertEquals(66, subject.numberOfDartsAtFinish)
    }

    @Test
    fun getNumberOfFinishes() {
        givenSubject(numberOfFinishes = 10)
        assertEquals(10, subject.numberOfFinishes)
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

    private fun givenSubject(numberOfGames: Int = -2, numberOfWins: Int = -2, numberOfDarts: Int = -2,
                             numberOfPoints: Int = -2, numberOfDarts9: Int = -2, numberOfPoints9: Int = -2,
                             numberOf180s: Int = -2, numberOf140s: Int = -2, numberOf100s: Int = -2,
                             numberOf60s: Int = -2, numberOf20s: Int = -2, numberOf0s: Int = -2,
                             numberOfDartsAtFinish: Int = -2, numberOfFinishes: Int = -2
    ) {
        subject = ProfileStat(numberOfGames, numberOfWins, numberOfDarts, numberOfPoints, numberOfDarts9, numberOfPoints9, numberOf180s, numberOf140s, numberOf100s, numberOf60s, numberOf20s, numberOf0s, numberOfDartsAtFinish, numberOfFinishes)
    }

}