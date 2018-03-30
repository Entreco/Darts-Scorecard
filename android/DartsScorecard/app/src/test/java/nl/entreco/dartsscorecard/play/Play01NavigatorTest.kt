package nl.entreco.dartsscorecard.play

import android.view.View
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.profile.view.ProfileActivity
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.Team
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.exceptions.misusing.NotAMockException
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Play01NavigatorTest {

    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockActivity: Play01Activity
    private lateinit var subject: Play01Navigator

    @Test(expected = NotAMockException::class)
    fun `it should go to player profile`() {
        givenSubject()
        whenGoingToTeamProfile(Team(arrayOf(Player("coco", id = 22))))
        thenProfileActivityIsLaunched()
    }

    @Test(expected = NullPointerException::class)
    fun `it should show snackbar when invalid players`() {
        givenSubject()
        whenGoingToTeamProfile(Team(emptyArray()))
        thenProfileActivityIsNotLaunched()
    }

    private fun givenSubject() {
        subject = Play01Navigator(mockActivity)
    }

    private fun whenGoingToTeamProfile(team: Team) {
        subject.gotoTeamProfile(mockView, team)
    }

    private fun thenProfileActivityIsLaunched() {
        verify(ProfileActivity).launch(any(), any(), any())
    }

    private fun thenProfileActivityIsNotLaunched() {

    }
}