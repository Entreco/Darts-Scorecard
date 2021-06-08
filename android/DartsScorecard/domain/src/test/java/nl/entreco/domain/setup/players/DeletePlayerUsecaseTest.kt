package nl.entreco.domain.setup.players

import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.repository.PlayerRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 17/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DeletePlayerUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockDone: (DeletePlayerResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockPlayerRepo: PlayerRepository
    private lateinit var subject: DeletePlayerUsecase


    @Test
    fun `it should delete player using repo`() {
        givenSubject()
        whenDeletingPlayerSucceeds()
        thenNoErrorIsReported()
    }

    @Test
    fun `it should report error when deleting player fails`() {
        givenSubject()
        whenDeletingPlayerThrows()
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = DeletePlayerUsecase(mockPlayerRepo, bg, fg)
    }


    private fun whenDeletingPlayerSucceeds() {
        subject.delete(DeletePlayerRequest(arrayOf(12L).toLongArray()), mockDone, mockFail)
    }

    private fun whenDeletingPlayerThrows() {
        whenever(mockPlayerRepo.deleteById(12L)).thenThrow(RuntimeException("Player does not exist"))
        subject.delete(DeletePlayerRequest(arrayOf(12L).toLongArray()), mockDone, mockFail)
    }

    private fun thenNoErrorIsReported() {
        verify(mockFail, never()).invoke(any())
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }
}