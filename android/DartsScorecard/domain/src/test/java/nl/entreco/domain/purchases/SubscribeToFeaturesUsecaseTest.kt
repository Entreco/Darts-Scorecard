package nl.entreco.domain.purchases

import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.purchases.connect.SubscribeToFeaturesUsecase
import nl.entreco.domain.repository.FeatureRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by entreco on 06/02/2018.
 */
class SubscribeToFeaturesUsecaseTest {

    private val mockDone: (List<Feature>) -> Unit = mock()
    private val mockFail: (Throwable) -> Unit = mock()
    private val mockFeatureRepo: FeatureRepository = mock()

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
        theOrderOfTitlesIs("feature 3", "feature 4", "feature 1", "feature 2")
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
        return Feature("reference", title, "desc", "image", "", required, votes, "")
    }
}
