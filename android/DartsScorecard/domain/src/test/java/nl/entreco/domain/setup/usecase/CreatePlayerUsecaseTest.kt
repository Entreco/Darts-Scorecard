package nl.entreco.domain.setup.usecase

import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.model.players.Player
import nl.entreco.domain.repository.PlayerRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 02/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class CreatePlayerUsecaseTest {

    @Mock private lateinit var mockRepo : PlayerRepository
    @Mock private lateinit var mockDone : (Player)->Unit
    @Mock private lateinit var mockFail : (Throwable)->Unit
    private val bg = TestBackground()
    private val fg = TestForeground()
    private val givenId : Long = 10
    private lateinit var subject : CreatePlayerUsecase
    private lateinit var givenRequest : CreatePlayerRequest
    private val okCaptor = argumentCaptor<Player>()

    @Test
    fun `it should report Player when creating succeeds`() {
        givenSubject()
        whenCreatingSucceeds("1", -1)
        thenSuccessIsReported()
    }

    @Test
    fun `it should create players with lowercase name`() {
        givenSubject()
        whenCreatingSucceeds("ThIs NaMe Is CaPiTaLiSeD", 180)
        thenSuccessIsReported()
        andReturnedPlayerNameIs("this name is capitalised")
    }

    @Test
    fun `it should report Error when creating fails`() {
        givenSubject()
        whenCreatingFails("2", 100)
        thenErrorIsReported()
    }

    @Test
    fun `it should report Error when name is Blank`() {
        givenSubject()
        whenCreatingSucceeds("   ", 100)
        thenErrorIsReported()
    }

    @Test
    fun `it should report Error when name is Empty`() {
        givenSubject()
        whenCreatingSucceeds("", 100)
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = CreatePlayerUsecase(mockRepo, bg, fg)
    }

    private fun whenCreatingSucceeds(name: String, double: Int) {
        whenever(mockRepo.create(eq(name.toLowerCase()), any())).thenReturn(givenId)
        givenRequest = CreatePlayerRequest(name, double)
        subject.exec(givenRequest, mockDone, mockFail)
    }

    private fun whenCreatingFails(name: String, double: Int) {
        whenever(mockRepo.create(any(), any())).thenThrow(IllegalStateException("oops"))
        givenRequest = CreatePlayerRequest(name, double)
        subject.exec(givenRequest, mockDone, mockFail)
    }

    private fun thenSuccessIsReported(){
        verify(mockDone).invoke(okCaptor.capture())
        assertEquals(givenRequest.name.toLowerCase(), okCaptor.lastValue.name)
        assertEquals(givenRequest.double, okCaptor.lastValue.prefs.favoriteDouble)
        assertEquals(givenId, okCaptor.lastValue.id)
    }

    private fun thenErrorIsReported(){
        verify(mockFail).invoke(any())
    }

    private fun andReturnedPlayerNameIs(expected: String) {
        assertEquals(expected, okCaptor.lastValue.name)
    }

}