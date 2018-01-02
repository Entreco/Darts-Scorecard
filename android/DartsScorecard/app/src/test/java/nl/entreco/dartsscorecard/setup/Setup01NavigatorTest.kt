package nl.entreco.dartsscorecard.setup

import android.databinding.ObservableField
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.setup.players.PlayerAdapter
import nl.entreco.dartsscorecard.setup.players.PlayerViewModel
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class Setup01NavigatorTest {

    @Mock private lateinit var mockActivity: Setup01Activity
    @Mock private lateinit var mockName: ObservableField<String>
    @Mock private lateinit var mockPlayerViewModel: PlayerViewModel
    @Mock private lateinit var mockAdapter: PlayerAdapter
    private lateinit var subject: Setup01Navigator

    @Test
    fun onEditPlayerName() {
        whenever(mockPlayerViewModel.name).thenReturn(mockName)
        whenever(mockName.get()).thenReturn("another name")
        givenSubject()
        subject.onEditPlayerName(mockPlayerViewModel)
        verify(mockPlayerViewModel).name
    }

    @Test
    fun onAddNewPlayer() {
        whenever(mockAdapter.onAddPlayer()).thenReturn("some name")
        givenSubject()
        subject.onAddNewPlayer(mockAdapter)
        verify(mockAdapter).onAddPlayer()
    }

    @Test
    fun launch() {
        givenSubject()
        subject.launch(RetrieveGameRequest(42, TeamIdsString("1,2"), CreateGameRequest(-1, -2, -3, -4)))
        verify(mockActivity).finish()
    }

    private fun givenSubject() {
        subject = Setup01Navigator(mockActivity)
    }

}