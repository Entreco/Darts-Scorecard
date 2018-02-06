package nl.entreco.dartsscorecard.beta

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.SubscribeToFeaturesUsecase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaViewModelTest{

    @Mock private lateinit var mockLifeCycle: Lifecycle
    @Mock private lateinit var mockOwner: LifecycleOwner
    @Mock private lateinit var mockObserver: Observer<List<Feature>>
    @Mock private lateinit var mockSubscribeToFeaturesUsecase: SubscribeToFeaturesUsecase
    private lateinit var subject: BetaViewModel

    @Test
    fun `it should subscribe to usecase`() {
        givenSubject()
        whenSubscribing()
        thenSubscribeIsCalled()
    }

    @Test(expected = NullPointerException::class)
    fun `it should unsubscribe`() {
        givenSubject()
        whenUnsubscribing()
        thenUnsubscribeIsCalled()
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

    private fun thenSubscribeIsCalled() {
        verify(mockSubscribeToFeaturesUsecase).subscribe(any(), any())
    }

    private fun thenUnsubscribeIsCalled() {
        verify(mockSubscribeToFeaturesUsecase).unsubscribe()
    }
}
