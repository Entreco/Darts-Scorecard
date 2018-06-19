package nl.entreco.dartsscorecard.profile.view

import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.profile.ProfileStat
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayerStatsTest {

    @Mock private lateinit var mockProfileStat: ProfileStat
    private lateinit var subject: PlayerStats

    @Test
    fun `it should show isEmpty(true)`() {
        givenSubject()
        assertTrue(subject.isEmpty.get())
    }

    @Test
    fun `it should show isEmpty(false)`() {
        givenSubject(4)
        assertFalse(subject.isEmpty.get())
    }

    @Test
    fun `it should show numberOfGames`() {
        givenSubject(4)
        assertEquals("4", subject.gamesPlayed.get())
    }

    @Test
    fun `it should show average`() {
        givenSubject(points = 100, darts = 3)
        assertEquals("100.00", subject.average.get())
    }

    private fun givenSubject(played: Int = 0, points: Int = 0, darts: Int = 0) {
        whenever(mockProfileStat.numberOfGames).thenReturn(played)
        whenever(mockProfileStat.numberOfPoints).thenReturn(points)
        whenever(mockProfileStat.numberOfDarts).thenReturn(darts)
        subject = PlayerStats(mockProfileStat)
    }
}