package nl.entreco.domain.play.mastercaller

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.Logger
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.SoundRepository
import org.junit.Test
import org.mockito.Mock

/**
 * Created by entreco on 14/03/2018.
 */
class MasterCallerTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockLogger: Logger
    @Mock private lateinit var mockSoundRepository: SoundRepository
    private lateinit var subject : MasterCaller

    @Test
    fun `it should play sound using sound repo`() {
        givenSubject()
        whenPlayingSound(Fx01())
        thenRepositoryIsUsed()
    }

    private fun givenSubject() {
        subject = MasterCaller(mockLogger, mockSoundRepository, bg, fg)
    }

    private fun whenPlayingSound(sound: Sound) {
        subject.play(MasterCallerRequest(sound))
        verify(mockSoundRepository).play(sound)
    }

    private fun thenRepositoryIsUsed() {

    }

}