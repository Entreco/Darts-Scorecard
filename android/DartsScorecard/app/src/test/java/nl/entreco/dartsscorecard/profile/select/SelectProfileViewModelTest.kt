package nl.entreco.dartsscorecard.profile.select

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.setup.players.DeletePlayerResponse
import nl.entreco.domain.setup.players.DeletePlayerUsecase
import nl.entreco.domain.setup.players.FetchExistingPlayersResponse
import nl.entreco.domain.setup.players.FetchExistingPlayersUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectProfileViewModelTest {

    @Mock private lateinit var mockPlayer: Player
    @Mock private lateinit var mockAdapter: SelectProfileAdapter
    @Mock private lateinit var mockFetchUsecase: FetchExistingPlayersUsecase
    @Mock private lateinit var mockDeleteUsecase: DeletePlayerUsecase
    private lateinit var subject: SelectProfileViewModel

    private val doneCaptor = argumentCaptor<(FetchExistingPlayersResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()
    private val profileCaptor = argumentCaptor<List<Profile>>()
    private val deleteCaptor = argumentCaptor<(DeletePlayerResponse)->Unit>()

    @Test
    fun `it should set isLoading initially`() {
        givenSubject()
        thenLoadingIs(true)
    }

    @Test
    fun `it should NOT set isEmpty initially`() {
        givenSubject()
        thenEmptys(false)
    }

    @Test
    fun `it should isEmpty when fetching players succeeds with empty list`() {
        givenMockPlayer()
        givenSubject()
        whenFetchingPlayersSucceeds()
        thenEmptys(true)
    }

    @Test
    fun `it should set Profiles on the Adapter when fetching players succeeds`() {
        givenMockPlayer()
        givenSubject()
        whenFetchingPlayersSucceeds(mockPlayer, mockPlayer)
        thenProfilesAreSet(2)
    }

    @Test
    fun `it should set isLoading(false) when fetching players fails`() {
        givenSubject()
        whenFetchingPlayersFails()
        thenLoadingIs(false)
    }

    @Test
    fun `it should set isEmpty(true) when fetching players fails`() {
        givenSubject()
        whenFetchingPlayersFails()
        thenEmptys(true)
    }

    @Test
    fun `it should reload items when reloading`() {
        givenSubject()
        whenReloading()
        thenLoadingIs(true)
    }

    @Test
    fun `it should reload when deleting player succeeds`() {
        givenSubject()
        whenDeletingPlayer()
        // TODO: Verify loader or something
    }

    @Test
    fun `it should report error when deleting player fails`() {
        givenSubject()
        whenDeletingPlayer()
        // TODO: Verify loader or something
    }

    private fun givenSubject() {
        subject = SelectProfileViewModel(mockFetchUsecase, mockDeleteUsecase)
    }

    private fun givenMockPlayer() {
        whenever(mockPlayer.name).thenReturn("Some name")
        whenever(mockPlayer.id).thenReturn(1)
        whenever(mockPlayer.image).thenReturn("Some image")
        whenever(mockPlayer.prefs).thenReturn(PlayerPrefs(20))
    }

    private fun whenFetchingPlayersSucceeds(vararg players: Player) {
        subject.fetchPlayers(mockAdapter)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(players.toList()))
    }

    private fun whenFetchingPlayersFails() {
        subject.fetchPlayers(mockAdapter)
        verify(mockFetchUsecase).exec(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(RuntimeException("Unable to fetch players"))
    }

    private fun whenReloading() {
        subject.reload(mockAdapter)
    }

    private fun whenDeletingPlayer() {
        subject.deletePlayerProfile(12, mockAdapter)
        verify(mockDeleteUsecase).delete(any(), deleteCaptor.capture(), any())
        deleteCaptor.lastValue.invoke(DeletePlayerResponse(12))
    }

    private fun thenProfilesAreSet(expected: Int) {
        verify(mockAdapter).setItems(profileCaptor.capture())
        assertEquals(expected, profileCaptor.lastValue.size)
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.isLoading.get())
    }

    private fun thenEmptys(expected: Boolean) {
        assertEquals(expected, subject.isEmpty.get())
    }
}
