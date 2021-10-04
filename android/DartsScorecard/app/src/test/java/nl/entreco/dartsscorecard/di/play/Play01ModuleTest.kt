package nl.entreco.dartsscorecard.di.play

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManager
import nl.entreco.dartsscorecard.play.Play01Activity
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import nl.entreco.domain.repository.AudioPrefRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

/**
 * Created by Entreco on 02/01/2018.
 */
class Play01ModuleTest {

    private val mockPrefs: AudioPrefRepository = mock()
    private val mockSplitInstallManager: SplitInstallManager = mock()
    private val mockListener: (MakePurchaseResponse) -> Unit = mock()
    private val mockContext: Context = mock()
    private val mockActivity: Play01Activity = mock()

    @Test
    fun `it should not be null`() {
        assertNotNull(subject())
    }

    @Test
    fun `it should provide Play01Activity`() {
        assertEquals(mockActivity, subject().provide01Activity())
    }

    @Test
    fun `it should provide SoundRepository`() {
        assertNotNull(subject().provideSoundRepository(mockContext, mockSplitInstallManager, mockPrefs))
    }

    @Test
    fun `it should provide ArchiveServiceLauncher`() {
        assertNotNull(subject().provideArchiveServiceLauncher(mockContext))
    }

    private fun subject(): Play01Module {
        return Play01Module(mockActivity, mockListener)
    }
}
