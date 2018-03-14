package nl.entreco.data.sound

import nl.entreco.data.R
import nl.entreco.domain.play.mastercaller.Fx00
import nl.entreco.domain.play.mastercaller.Fx01
import nl.entreco.domain.play.mastercaller.FxTest
import nl.entreco.domain.play.mastercaller.None
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class SoundMapperTest {

    private val subject = SoundMapper()

    @Test
    fun `it should map None`() {
        assertEquals(0, subject.toRaw(None()))
    }

    @Test
    fun `it should map Fx00`() {
        assertEquals(R.raw.dsc_pro0, subject.toRaw(Fx00()))
    }

    @Test
    fun `it should map Fx01`() {
        assertEquals(R.raw.dsc_pro1, subject.toRaw(Fx01()))
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw when mapping unknown sounds`() {
        assertEquals(R.raw.dsc_pro0, subject.toRaw(FxTest()))
    }

}