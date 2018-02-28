package nl.entreco.domain.profile.update

import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.profile.Profile
import nl.entreco.domain.repository.ProfileRepository
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
    @Mock private lateinit var mockProfileService: ProfileRepository
    private lateinit var subject : UpdateProfileUsecase

    @Test
    fun `it should return updatedProfile on success`() {
        givenSubject()
        whenUpdatingProfileSucceeds("Remco")
        thenProfileIsReported()
    }

    private fun givenSubject() {
        subject = UpdateProfileUsecase(mockProfileService, bg, fg)
    }

    private fun whenUpdatingProfileSucceeds(name: String) {
        whenever(mockProfileService.update(12, name, null, null)).thenReturn(mockProfile)
        subject.exec(UpdateProfileRequest(12, name, null, null), mockDone, mockFail)
        verify(mockProfileService).update(12, name, null, null)
    }

    private fun thenProfileIsReported() {
        verify(mockDone).invoke(UpdateProfileResponse(mockProfile))
    }

}
