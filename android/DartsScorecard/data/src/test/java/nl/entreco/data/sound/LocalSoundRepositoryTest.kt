package nl.entreco.data.sound

import android.content.Context
import nl.entreco.domain.play.mastercaller.Fx01
import nl.entreco.domain.play.mastercaller.Sound
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 14/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalSoundRepositoryTest {

    @Mock private lateinit var mockContext: Context
    private lateinit var subject : LocalSoundRepository

    @Test
    fun `it should player correct sound`() {
        givenSubject()
        whenPlaying(Fx01())
    }

    private fun givenSubject() {
        subject = LocalSoundRepository(mockContext)
    }

    private fun whenPlaying(sound: Sound) {
        subject.play(sound)
    }

}