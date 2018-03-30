package nl.entreco.domain.profile.fetch

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.ProfileRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FetchProfileUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockProfileRepo: ProfileRepository
    @Mock private lateinit var mockDone: (FetchProfileResponse) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit

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
        subject = FetchProfileUsecase(mockProfileRepo, bg, fg)
    }

    private fun whenFetchingProfileSucceeds() {
        val players = LongArray(4)
        whenever(mockProfileRepo.fetchAll(players)).thenReturn(emptyList())
        subject.exec(FetchProfileRequest(players), mockDone, mockFail)
    }

    private fun whenFetchingProfileFails() {
        whenever(mockProfileRepo.fetchAll(any())).thenThrow(RuntimeException("Unable to fetch profile"))
        subject.exec(FetchProfileRequest(LongArray(4)), mockDone, mockFail)
    }

    private fun thenOkIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenFailIsReported() {
        verify(mockFail).invoke(any())
    }

}
