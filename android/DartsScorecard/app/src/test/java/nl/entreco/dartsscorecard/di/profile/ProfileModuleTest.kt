package nl.entreco.dartsscorecard.di.profile

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.data.db.DscDatabase
import nl.entreco.data.db.profile.ProfileMapper
import nl.entreco.data.db.profile.ProfileStatMapper
import nl.entreco.domain.beta.donations.MakePurchaseResponse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 21/02/2018.
 */
class ProfileModuleTest {

    private val mockDatabase: DscDatabase = mock()
    private val mockProfileMapper: ProfileMapper = mock()
    private val mockProfileStatMapper: ProfileStatMapper = mock()
    private val mockContext: Context = mock()
    private val mockContentResolver: ContentResolver = mock()

    private val mockListener: (MakePurchaseResponse) -> Unit = mock()
    private val mockActivity: Activity = mock()

    @Test
    fun `should not be null`() {
        assertNotNull(givenModule())
    }

    @Test
    fun `it should provide profile mapper`() {
        assertNotNull(givenModule().provideProfileMapper())
    }

    @Test
    fun `it should provide profile stat mapper`() {
        assertNotNull(givenModule().provideProfileStatMapper())
    }
    @Test
    fun `it should provide mapper`() {
        assertNotNull(givenModule().provideProfileStatRepository(mockDatabase, mockProfileStatMapper))
    }

    @Test
    fun `it should provide contentResolver`() {
        whenever(mockContext.contentResolver).thenReturn(mockContentResolver)
        assertNotNull(givenModule().provideContentResolver(mockContext))
    }

    @Test
    fun `it should provide imageRepository`() {
        assertNotNull(givenModule().provideImageRepository(mockContext, mockContentResolver))
    }

    @Test
    fun `it should provide profileRepository`() {
        assertNotNull(givenModule().provideProfileRepository(mockDatabase, mockProfileMapper))
    }

    private fun givenModule() = ProfileModule(mockActivity, mockListener)
}
