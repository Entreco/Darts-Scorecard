package nl.entreco.dartsscorecard.play

import android.content.Context
import android.content.Intent
import org.mockito.kotlin.any
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.domain.setup.game.CreateGameRequest
import nl.entreco.domain.setup.game.CreateGameResponse
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
    private val givenTeamIds = givenTeamString
    private val givenCreate = CreateGameRequest(1, 2, 3, 4)

    val subject = spy(Play01Activity())

    @Test
    fun `should start Play01Activity`() {
        whenever(mockCreateResponse.gameId).thenReturn(givenGameId)
        whenever(mockCreateResponse.teamIds).thenReturn(givenTeamIds)
        whenever(mockCreateResponse.startIndex).thenReturn(givenCreate.startIndex)
        whenever(mockCreateResponse.startScore).thenReturn(givenCreate.startScore)
        whenever(mockCreateResponse.numLegs).thenReturn(givenCreate.numLegs)
        whenever(mockCreateResponse.numSets).thenReturn(givenCreate.numSets)
        Play01Activity.startGame(mockContext, mockCreateResponse)
        verify(mockContext).startActivity(any())
    }

    @Test
    fun `should retrieve setup from Intent`() {
        whenever(mockIntent.getLongExtra("gameId", -1)).thenReturn(givenGameId)
        whenever(mockIntent.getStringExtra("teamIds")).thenReturn(givenTeamString)
        whenever(mockIntent.getIntExtra("startScore", -1)).thenReturn(givenCreate.startScore)
        whenever(mockIntent.getIntExtra("legs", -1)).thenReturn(givenCreate.numLegs)
        whenever(mockIntent.getIntExtra("sets", -1)).thenReturn(givenCreate.numSets)
        assertNotNull(Play01Activity.retrieveSetup(mockIntent))
    }

}