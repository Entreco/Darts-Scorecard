package nl.entreco.dartsscorecard.beta.votes

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import android.view.View
import com.nhaarman.mockito_kotlin.*
import nl.entreco.dartsscorecard.beta.BetaModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.Donation
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.beta.vote.SubmitVoteResponse
import nl.entreco.domain.beta.vote.SubmitVoteUsecase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 07/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class VoteViewModelTest {

    @Mock private lateinit var mockObservableField: ObservableField<String>
    @Mock private lateinit var mockVoteUsecase: SubmitVoteUsecase
    @Mock private lateinit var mockAnalytics: Analytics
    @Mock private lateinit var mockContext: Context
    @Mock private lateinit var mockModel: BetaModel
    @Mock private lateinit var mockView: View
    private lateinit var subject: VoteViewModel

    private lateinit var givenDonation: Donation
    private val doneCaptor = argumentCaptor<(SubmitVoteResponse) -> Unit>()
    private val failCaptor = argumentCaptor<(Throwable) -> Unit>()

    @Test
    fun `it should set selected feature in field`() {
        givenSubject()
        whenFeatureSelected(mockModel)
        thenSelectedFeatureIs(mockModel)
    }

    @Test
    fun `it should track achievement when feature selected`() {
        givenSubject()
        whenFeatureSelected(mockModel)
        thenAchievementIsTracked()
    }

    @Test
    fun `it should submit vote when submitting Donation`() {
        givenDonation(1)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonation()
        thenVoteIsSubmitted()
    }

    @Test
    fun `it should trackAchievement when voting success`() {
        givenDonation(5)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds()
        thenAchievementIsTracked()
    }

    @Test
    fun `it should set 'didAlreadyVote(true)' when voting success`() {
        givenDonation(2)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds()
        thenDidAlreadyVoteIs(true)
    }

    @Test
    fun `it should track 'View Feature' when voting success`() {
        givenDonation(1)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds()
        thenViewFeatureIsTracked(1)
    }

    @Test
    fun `it should remove votes when voting fails`() {
        givenDonation(1)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationFails()
        thenVotesDoesNotContain("ref")
    }

    @Test
    fun `it should set 'didAlreadyVote(false)' when voting fails`() {
        givenDonation(10)
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationFails()
        thenDidAlreadyVoteIs(false)
    }

    @Test(expected = NullPointerException::class) // Meaning Uri.parse() called
    fun `it should launch video for valid url`() {
        givenSubject()
        whenLaunchingVideo("http://www.url.com")
        thenVideoIsLaunched()
    }

    private fun givenDonation(amount: Int) {
        givenDonation = Donation("title", "desc", "sku", "price", amount, "e", "12222")
    }

    private fun givenSubject() {
        subject = VoteViewModel(mockVoteUsecase, mockAnalytics)
    }

    private fun whenFeatureSelected(feature: BetaModel) {
        whenever(mockObservableField.get()).thenReturn("Feature Title")
        whenever(mockModel.feature).thenReturn(Feature("reference", "feature title", "feature description", "some image", "updates", 10, 2, ""))
        whenever(mockModel.video).thenReturn(mockObservableField)
        whenever(mockModel.title).thenReturn(mockObservableField)
        subject.onFeatureSelected(feature)
    }

    private fun whenLaunchingVideo(url: String) {
        val feature = Feature("reference", "feature title", "feature description", "some image", "updates", 10, 2, url)
        subject.onFeatureSelected(BetaModel(feature))
        subject.launchVideo(mockView)
    }

    private fun whenSubmittingDonation() {
        whenever(mockModel.votable).thenReturn(ObservableBoolean(true))
        subject.submitDonation(givenDonation)
    }

    private fun whenSubmittingDonationSucceeds() {
        whenever(mockModel.votable).thenReturn(ObservableBoolean(true))

        subject.submitDonation(givenDonation)

        verify(mockVoteUsecase).exec(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(SubmitVoteResponse(true))
    }

    private fun whenSubmittingDonationFails() {
        whenever(mockModel.votable).thenReturn(ObservableBoolean(true))

        subject.submitDonation(givenDonation)

        verify(mockVoteUsecase).exec(any(), any(), failCaptor.capture())
        failCaptor.lastValue.invoke(Throwable("oops"))
    }

    private fun thenSelectedFeatureIs(expected: BetaModel) {
        assertEquals(expected, subject.feature.get())
    }

    private fun thenAchievementIsTracked() {
        verify(mockAnalytics, atLeastOnce()).trackAchievement(any())
    }

    private fun thenVoteIsSubmitted() {
        verify(mockVoteUsecase).exec(any(), any(), any())
    }

    private fun thenDidAlreadyVoteIs(expected: Boolean) {
        assertEquals(expected, subject.didAlreadyVote.get())
    }

    private fun thenVotesDoesNotContain(expected: String) {
        assertFalse(subject.votes.contains(expected))
    }

    private fun thenViewFeatureIsTracked(amount: Int) {
        verify(mockAnalytics).trackViewFeature(any(), eq(amount))
    }

    private fun thenVideoIsLaunched() {
        verify(mockView).context
    }
}
