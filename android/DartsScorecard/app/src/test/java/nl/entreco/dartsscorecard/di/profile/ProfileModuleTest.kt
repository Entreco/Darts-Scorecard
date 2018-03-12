package nl.entreco.dartsscorecard.di.profile

import android.content.ContentResolver
import android.content.Context
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.ProfileMapper
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 21/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ProfileModuleTest {

    @Mock private lateinit var mockDatabase: DscDatabase
    @Mock private lateinit var mockMapper: ProfileMapper
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockContentResolver: ContentResolver

    @Test
    fun `should not be null`() {
        assertNotNull(ProfileModule())
    }

    @Test
    fun `it should provide mapper`() {
        assertNotNull(ProfileModule().provideMapper())
    }

    @Test
    fun `it should provide contentResolver`() {
        whenever(mockContext.contentResolver).thenReturn(mockContentResolver)
        assertNotNull(ProfileModule().provideContentResolver(mockContext))
    }

    @Test
    fun `it should provide imageRepository`() {
        assertNotNull(ProfileModule().provideImageRepository(mockContext, mockContentResolver))
    }

    @Test
    fun `it should provide profileRepository`() {
        assertNotNull(ProfileModule().provideProfileRepository(mockDatabase, mockMapper))
    }
}
