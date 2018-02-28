package nl.entreco.domain.profile.update

import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.ProfileRepository
import org.junit.Test
import org.mockito.Mock

/**
 * Created by entreco on 28/02/2018.
 */
class UpdateProfileUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockProfileService: ProfileRepository
    private lateinit var subject : UpdateProfileUsecase

    @Test
    fun `it should return updatedProfile on success`() {
        givenSubject()
    }


    private fun givenSubject() {
        subject = UpdateProfileUsecase(mockProfileService, bg, fg)
    }

}
