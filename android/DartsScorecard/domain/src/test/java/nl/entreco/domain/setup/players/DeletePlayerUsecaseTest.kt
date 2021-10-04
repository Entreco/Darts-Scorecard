package nl.entreco.domain.setup.players

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.PlayerRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 17/03/2018.
 */
class DeletePlayerUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val mockDone: (DeletePlayerResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val mockPlayerRepo: PlayerRepository = mock()
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