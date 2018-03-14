package nl.entreco.domain.play.mastercaller

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.Logger
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.SoundRepository
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
        thenRepositoryIsUsed(Fx01())
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

    private fun whenStopping() {
        subject.stop()
    }

    private fun thenRepositoryIsUsed() {
        verify(mockSoundRepository).play(any())
    }

    private fun thenRepositoryIsReleased() {
        verify(mockSoundRepository).release()
    }

}