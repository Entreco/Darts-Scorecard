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

    private fun givenSubject(id: Long = -2, numberOfGames: Int = -2) {
        subject = ProfileStat(id, numberOfGames)
    }

}