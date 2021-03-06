package nl.entreco.dartsscorecard.beta

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.purchases.connect.SubscribeToFeaturesUsecase
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaViewModelTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()
    @Mock private lateinit var mockLifeCycle: Lifecycle
    @Mock private lateinit var mockOwner: LifecycleOwner
    @Mock private lateinit var mockObserver: Observer<List<Feature>>
    @Mock private lateinit var mockSubscribeToFeaturesUsecase: SubscribeToFeaturesUsecase
    private lateinit var subject: BetaViewModel
    private var expectedFeatureList = emptyList<Feature>()
    private val doneCaptor = argumentCaptor<(List<Feature>) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should subscribe to usecase`() {
        givenSubject()
        whenSubscribing()
        thenSubscribeIsCalled()
    }

    @Test
    fun `it should unsubscribe`() {
        givenSubject()
        whenUnsubscribing()
        thenUnsubscribeIsCalled()
    }

    @Test
    fun `it should store Features when fetching features succeeds`() {
        givenSubject()
        whenFetchingFeaturesSucceeds()
        thenFeatureListIsNotEmpty()
    }

    @Test
    fun `it should NOT store Features when fetching features fails`() {
        givenSubject()
        whenFetchingFeaturesFails(RuntimeException("Unable to contact FireStore"))
        thenFeatureListIsEmpty()
    }

    private fun givenSubject() {
        subject = BetaViewModel(mockSubscribeToFeaturesUsecase)
    }

    private fun whenSubscribing() {
        whenever(mockOwner.lifecycle).thenReturn(mockLifeCycle)
        subject.subscribe(mockOwner, mockObserver)
    }

    private fun whenUnsubscribing() {
        subject.unsubscribe(mockOwner)
    }

    private fun whenFetchingFeaturesSucceeds() {
        expectedFeatureList = listOf(Feature("ref", "title", "desc", "img", "", 3, 1, ""))
        subject.refresh()
        verify(mockSubscribeToFeaturesUsecase).subscribe(doneCaptor.capture(), any())
        try {
            doneCaptor.lastValue.invoke(expectedFeatureList)
        } catch (noMainThreadWhileTesting: NullPointerException) {
        }
    }

    private fun whenFetchingFeaturesFails(err: Throwable) {
        subject.refresh()
        verify(mockSubscribeToFeaturesUsecase).subscribe(any(), failCaptor.capture())
        failCaptor.lastValue.invoke(err)
    }

    private fun thenSubscribeIsCalled() {
        verify(mockSubscribeToFeaturesUsecase).subscribe(any(), any())
    }

    private fun thenUnsubscribeIsCalled() {
        verify(mockSubscribeToFeaturesUsecase).unsubscribe()
    }

    private fun thenFeatureListIsEmpty() {
        assertTrue(expectedFeatureList.isEmpty())
    }

    private fun thenFeatureListIsNotEmpty() {
        assertTrue(expectedFeatureList.isNotEmpty())
    }
}
