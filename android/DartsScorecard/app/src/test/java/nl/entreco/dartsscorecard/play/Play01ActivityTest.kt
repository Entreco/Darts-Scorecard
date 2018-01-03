package nl.entreco.dartsscorecard.play

import android.content.Context
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
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
class Play01ActivityTest {

    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockRetrieveRequest: RetrieveGameRequest
    private val givenGameId: Long = 111
    private val givenTeamString = "1,2|3"
    private val givenTeamIds = TeamIdsString(givenTeamString)
    private val givenCreate = CreateGameRequest(1, 2, 3, 4)

    val subject = spy(Play01Activity())

    @Test
    fun `should start Play01Activity`() {
        whenever(mockRetrieveRequest.gameId).thenReturn(givenGameId)
        whenever(mockRetrieveRequest.teamIds).thenReturn(givenTeamIds)
        whenever(mockRetrieveRequest.create).thenReturn(givenCreate)
        Play01Activity.startGame(mockContext, mockRetrieveRequest)
        verify(mockContext).startActivity(any())
    }

}