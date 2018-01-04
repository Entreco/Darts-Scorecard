package nl.entreco.domain.setup.usecase

import android.database.sqlite.SQLiteDatabaseLockedException
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.repository.PreferenceRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 04/01/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class FetchPreferredSettingsUsecaseTest {

    @Mock private lateinit var mockDone: (FetchSettingsResponse) -> Unit
    @Mock private lateinit var mockPrefRepo: PreferenceRepository
    private lateinit var subject: FetchPreferredSettingsUsecase
    private val responseCaptor = argumentCaptor<FetchSettingsResponse>()

    @Test
    fun `it should return default settings when something goes wrong`() {
        givenSubject()
        whenFetchingFails(SQLiteDatabaseLockedException("oops"))
        thenDefaultResponseIsReturned()
    }

    @Test
    fun `it should return preferred settings`() {
        givenSubject()
        whenFetchingSucceeds(1, 2, 3, 4, 5)
        thenResponseIs(1, 2, 3, 4, 5)
    }

    private fun givenSubject() {
        subject = FetchPreferredSettingsUsecase(mockPrefRepo)
    }

    private fun whenFetchingFails(err: Exception) {
        whenever(mockPrefRepo.fetchPreferredSetup()).thenThrow(err)
        subject.exec(mockDone)
        verify(mockDone).invoke(responseCaptor.capture())
    }

    private fun whenFetchingSucceeds(legs: Int, sets: Int, min: Int, max: Int, score: Int) {
        whenever(mockPrefRepo.fetchPreferredSetup()).thenReturn(FetchSettingsResponse(sets, legs, min, max, score))
        subject.exec(mockDone)
        verify(mockDone).invoke(responseCaptor.capture())
    }

    private fun thenDefaultResponseIsReturned() {
        assertEquals(FetchSettingsResponse(), responseCaptor.lastValue)
    }

    private fun thenResponseIs(legs: Int, sets: Int, min: Int, max: Int, score: Int) {
        assertEquals(legs, responseCaptor.lastValue.startLegs)
        assertEquals(sets, responseCaptor.lastValue.startSets)
        assertEquals(score, responseCaptor.lastValue.startScore)
        assertEquals(min, responseCaptor.lastValue.min)
        assertEquals(max, responseCaptor.lastValue.max)
    }

}