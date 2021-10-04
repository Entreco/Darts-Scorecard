package nl.entreco.domain.mastercaller

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.liblog.Logger
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class MasterCallerTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockSoundRepository: SoundRepository
    private lateinit var subject : MasterCaller

    @Test
    fun `it should play sound using sound repo`() {
        givenSubject()
        whenPlayingSound(1)
        thenRepositoryIsUsed()
    }

    @Test
    fun `it should log error when playing sound throws`() {
        givenSubject()
        whenPlayingSoundThrows()
        thenErrorIsLogged()
    }

    @Test
    fun `it should release SoundRepo when stopping`() {
        givenSubject()
        whenStopping()
        thenRepositoryIsReleased()
    }

    private fun givenSubject() {
        subject = MasterCaller(mockLogger, mockSoundRepository, bg, fg)
    }

    private fun whenPlayingSound(scored: Int) {
        subject.play(MasterCallerRequest(scored))
    }

    private fun whenPlayingSoundThrows() {
        whenever(mockSoundRepository.play(any())).thenThrow(RuntimeException("audio not available"))
        subject.play(MasterCallerRequest(300))
    }

    private fun whenStopping() {
        subject.stop()
    }

    private fun thenRepositoryIsUsed() {
        verify(mockSoundRepository).play(any())
    }

    private fun thenErrorIsLogged() {
        verify(mockLogger).e(any())
    }

    private fun thenRepositoryIsReleased() {
        verify(mockSoundRepository).release()
    }
}