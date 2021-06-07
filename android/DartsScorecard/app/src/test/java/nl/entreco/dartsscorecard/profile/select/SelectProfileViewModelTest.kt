package nl.entreco.dartsscorecard.profile.select

import org.mockito.kotlin.*
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.setup.players.*
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
    @Mock private lateinit var mockCreateUsecase: CreatePlayerUsecase
    @Mock private lateinit var mockFetchUsecase: FetchExistingPlayersUsecase
    @Mock private lateinit var mockDeleteUsecase: DeletePlayerUsecase
    private lateinit var subject: SelectProfileViewModel

    private var givenPlayers : List<Player>? = emptyList()

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
        givenMockPlayers()
        givenSubject()
        whenFetchingPlayersSucceeds()
        thenEmptys(true)
    }

    @Test
    fun `it should set Profiles on the Adapter when fetching players succeeds`() {
        givenMockPlayers(1,2)
        givenSubject()
        whenFetchingPlayersSucceeds()
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
        whenHidingPlayer(12L)
        whenDeletingPlayer()
        thenPlayersAreFetched()
    }

    @Test
    fun `it should report error when deleting player fails`() {
        givenSubject()
        whenDeletingPlayer()
        thenPlayersAreFetched()
    }

    @Test
    fun `it should create player`() {
        givenSubject()
        whenCreatingPlayerSucceeds("Pietje")
        thenLoadingIs(true)
    }

    @Test
    fun `it should hide players that are temporarily deleted`() {
        givenMockPlayers(1, 2, 3)
        givenSubject()
        whenHidingPlayer(1)
        thenProfilesAreSet(2)
    }

    @Test
    fun `it should clear hidden players when undoing`() {
        givenMockPlayers(1, 2, 3)
        givenSubject()
        whenHidingPlayer(1)
        thenProfilesAreSet(2)

        whenUndoing()
        thenProfilesAreSet(3)
    }

    private fun givenSubject() {
        subject = SelectProfileViewModel(mockCreateUsecase, mockFetchUsecase, mockDeleteUsecase)
    }

    private fun givenMockPlayers(vararg players: Long) {
        givenPlayers = players.map {
            mock<Player> { _ ->
                on { name } doReturn "$it"
                on { image } doReturn "$it"
                on { id } doReturn it
                on { prefs } doReturn PlayerPrefs(20)
            }
        }
    }

    private fun whenFetchingPlayersSucceeds() {
        subject.fetchPlayers(mockAdapter)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(givenPlayers!!))
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
        subject.deletePlayerProfiles(mockAdapter)
        verify(mockDeleteUsecase).delete(any(), deleteCaptor.capture(), any())
        deleteCaptor.lastValue.invoke(DeletePlayerResponse(arrayOf(12L).toLongArray()))
    }

    private fun whenHidingPlayer(playerId: Long) {
        subject.hidePlayerProfile(playerId, mockAdapter)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(givenPlayers!!))
        reset(mockFetchUsecase)
    }

    private fun whenUndoing(){
        subject.undoDelete(mockAdapter)
        verify(mockFetchUsecase).exec(doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(FetchExistingPlayersResponse(givenPlayers!!))
        reset(mockFetchUsecase)
    }

    private fun whenCreatingPlayerSucceeds(name: String) {
        subject.create(mockAdapter, name, 16)
    }

    private fun thenProfilesAreSet(expected: Int) {
        verify(mockAdapter).setItems(profileCaptor.capture())
        assertEquals(expected, profileCaptor.lastValue.size)
        reset(mockAdapter)
    }

    private fun thenLoadingIs(expected: Boolean) {
        assertEquals(expected, subject.isLoading.get())
    }

    private fun thenEmptys(expected: Boolean) {
        assertEquals(expected, subject.isEmpty.get())
    }

    private fun thenPlayersAreFetched(){
        verify(mockFetchUsecase).exec(any(), any())
    }
}
