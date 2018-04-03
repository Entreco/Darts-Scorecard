package nl.entreco.domain.beta.connect

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.FeatureRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 06/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SubscribeToFeaturesUsecaseTest {

    @Mock private lateinit var mockDone: (List<Feature>) -> Unit
    @Mock private lateinit var mockFail: (Throwable) -> Unit
    @Mock private lateinit var mockFeatureRepo: FeatureRepository

    private lateinit var subject: SubscribeToFeaturesUsecase

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val listCaptor = argumentCaptor<List<Feature>>()

    @Test
    fun `it should subscribe to repo`() {
        givenSubject()
        whenSubscribing()
        thenRepoIsSubscribedTo()
    }

    @Test
    fun `it should unsubscribe from repo`() {
        givenSubject()
        whenUnsubscribing()
        thenRepoIsUnsubscribedFrom()
    }

    @Test
    fun `it should report results after subscribing`() {
        givenSubject()
        whenSubscribingSucceeds()
        thenSuccessIsReported()
    }

    @Test
    fun `it should sort features before reporting results`() {
        givenSubject()
        whenSubscribingSucceeds(f("feature 1", 500, 2), f("feature 2", 500, 500))
        theOrderOfTitlesIs("feature 2", "feature 1")
    }

    @Test
    fun `it should sort features ascending before reporting results`() {
        givenSubject()
        whenSubscribingSucceeds(f("feature 1", 0, 0), f("feature 2", 500, -1), f("feature 3", 1, 500), f("feature 4", -1, -5))
        theOrderOfTitlesIs("feature 3",  "feature 4", "feature 1", "feature 2")
    }

    @Test
    fun `it should report failure when subscribing fails`() {
        givenSubject()
        whenSubscribingFails()
        thenErrorIsReported()
    }

    private fun givenSubject() {
        subject = SubscribeToFeaturesUsecase(mockFeatureRepo, bg, fg)
    }

    private fun whenSubscribing() {
        subject.subscribe(mockDone, mockFail)
    }

    private fun whenUnsubscribing() {
        subject.unsubscribe()
    }

    private fun whenSubscribingSucceeds(vararg features: Feature) {
        whenever(mockFeatureRepo.subscribe(any())).thenReturn(listOf(*features))
        subject.subscribe(mockDone, mockFail)
    }

    private fun whenSubscribingFails() {
        whenever(mockFeatureRepo.subscribe(any())).thenThrow(RuntimeException("Uhh uh"))
        subject.subscribe(mockDone, mockFail)
    }

    private fun thenRepoIsSubscribedTo() {
        verify(mockFeatureRepo).subscribe(any())
    }

    private fun thenRepoIsUnsubscribedFrom() {
        verify(mockFeatureRepo).unsubscribe()
    }

    private fun thenSuccessIsReported() {
        verify(mockDone).invoke(any())
    }

    private fun thenErrorIsReported() {
        verify(mockFail).invoke(any())
    }

    private fun theOrderOfTitlesIs(vararg features: String) {
        verify(mockDone).invoke(listCaptor.capture())
        features.forEachIndexed { index, string ->
            assertEquals(string, listCaptor.lastValue[index].title)
        }
    }

    private fun f(title: String, required: Int, votes: Int): Feature {
        return Feature("reference", title, "desc", "image", "",required, votes, "")
    }
}
