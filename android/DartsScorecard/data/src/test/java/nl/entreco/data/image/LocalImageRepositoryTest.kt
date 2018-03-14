package nl.entreco.data.image

import android.content.ContentResolver
import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 11/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class LocalImageRepositoryTest {

    @Mock private lateinit var mockContentResolver: ContentResolver
    @Mock private lateinit var mockContext: Context
    private lateinit var subject: LocalImageRepository
    private var actualResult: String? = null

    @Test
    fun `it should scale image`() {
        givenSuject()
        whenCopyingImageToPrivateFolder("some.img")
        thenImageUriIs("some.img")
    }

    private fun givenSuject() {
        subject = LocalImageRepository(mockContext, mockContentResolver)
    }

    private fun whenCopyingImageToPrivateFolder(expected: String) {
        actualResult = subject.copyImageToPrivateAppData(expected, 200F)
    }

    private fun thenImageUriIs(expected: String) {
        assertEquals(expected, actualResult)
    }

}
