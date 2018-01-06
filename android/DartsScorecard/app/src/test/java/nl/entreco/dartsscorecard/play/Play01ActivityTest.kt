package nl.entreco.dartsscorecard.play

import android.content.Context
import android.content.Intent
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.game.CreateGameResponse
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Assert.assertNotNull
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
    @Mock private lateinit var mockCreateResponse: CreateGameResponse
    @Mock private lateinit var mockIntent: Intent
    private val givenGameId: Long = 111
    private val givenTeamString = "1,2|3"
    private val givenTeamIds = TeamIdsString(givenTeamString)
    private val givenCreate = CreateGameRequest(1, 2, 3, 4)

    val subject = spy(Play01Activity())

    @Test
    fun `should start Play01Activity`() {
        whenever(mockCreateResponse.gameId).thenReturn(givenGameId)
        whenever(mockCreateResponse.teamIds).thenReturn(givenTeamIds)
        whenever(mockCreateResponse.create).thenReturn(givenCreate)
        Play01Activity.startGame(mockContext, mockCreateResponse)
        verify(mockContext).startActivity(any())
    }

    @Test
    fun `should retrieve setup from Intent`() {
        whenever(mockIntent.getLongExtra("gameId", -1)).thenReturn(givenGameId)
        whenever(mockIntent.getStringExtra("teamIds")).thenReturn(givenTeamString)
        whenever(mockIntent.getParcelableExtra<CreateGameRequest>("exec")).thenReturn(givenCreate)
        assertNotNull(Play01Activity.retrieveSetup(mockIntent))
    }

}