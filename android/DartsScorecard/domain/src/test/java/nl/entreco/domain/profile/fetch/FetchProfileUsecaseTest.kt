package nl.entreco.domain.profile.fetch

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.ProfileInfoRepository
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 26/02/2018.
 */
class FetchProfileUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val mockProfileInfoRepo: ProfileInfoRepository = mock()
    private val mockDone: (FetchProfileResponse) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()

    private lateinit var subject: FetchProfileUsecase

    @Test
    fun `it should report ok when fetch profile succeeds`() {
        givenSubject()
        whenFetchingProfileSucceeds()
        thenOkIsReported()
    }

    @Test
    fun `it should report failure when fetch profile fails`() {
        givenSubject()
        whenFetchingProfileFails()
        thenFailIsReported()
    }

    private fun givenSubject() {
        subject = FetchProfileUsecase(mockProfileInfoRepo, bg, fg)
    }

    private fun whenFetchingProfileSucceeds() {
        val players = LongArray(4)
        whenever(mockProfileInfoRepo.fetchAll(players)).thenReturn(emptyList())
        subject.exec(FetchProfileRequest(players), mockDone, mockFail)
    }

    private fun whenFetchingProfileFails() {
        whenever(mockProfileInfoRepo.fetchAll(any())).thenThrow(RuntimeException("Unable to fetch profile"))
        subject.exec(FetchProfileRequest(LongArray(4)), mockDone, mockFail)
    }

    private fun thenOkIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenFailIsReported() {
        verify(mockFail).invoke(any())
    }

}
