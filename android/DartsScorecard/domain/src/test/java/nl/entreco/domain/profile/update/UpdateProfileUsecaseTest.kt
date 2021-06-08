package nl.entreco.domain.profile.update

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.repository.ImageRepository
import nl.entreco.domain.repository.ProfileInfoRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 28/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class UpdateProfileUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockProfile: Profile
    @Mock private lateinit var mockDone: (UpdateProfileResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockImageRepository: ImageRepository
    @Mock private lateinit var mockProfileInfoService: ProfileInfoRepository
    private lateinit var subject: UpdateProfileUsecase

    @Test
    fun `it should return updatedProfile on success`() {
        givenSubject()
        whenUpdatingProfileSucceeds("Remco")
        thenProfileIsReported()
    }

    @Test
    fun `it should return updated profile after success`() {
        givenSubject()
        whenUpdatingProfileSucceeds(image = "img.jpg")
        thenProfileIsReported()
    }

    @Test
    fun `it should use ImageRepository for updating image`() {
        givenSubject()
        whenUpdatingProfileSucceeds(image = "img.jpg")
        thenImageServiceIsUsed("img.jpg")
    }

    private fun givenSubject() {
        subject = UpdateProfileUsecase(mockImageRepository, mockProfileInfoService, bg, fg)
    }

    private fun whenUpdatingProfileSucceeds(name: String = "name", image: String = "image") {
        whenever(mockImageRepository.copyImageToPrivateAppData(any(), any())).thenReturn(image)
        whenever(mockProfileInfoService.update(12, name, image, "12")).thenReturn(mockProfile)
        subject.exec(UpdateProfileRequest(12, name, "12", image, 200F), mockDone, mockFail)
        verify(mockProfileInfoService).update(12, name, image, "12")
    }

    private fun thenProfileIsReported() {
        verify(mockDone).invoke(UpdateProfileResponse(mockProfile))
    }

    private fun thenImageServiceIsUsed(expected: String) {
        verify(mockImageRepository).copyImageToPrivateAppData(expected, 200F)
    }

}
