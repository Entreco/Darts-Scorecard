package nl.entreco.dartsscorecard.beta.votes

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
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
    @Mock private lateinit var mockModel: BetaModel
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
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonation(1)
        thenVoteIsSubmitted()
    }

    @Test
    fun `it should trackAchievement when voting success`() {
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds(1)
        thenAchievementIsTracked()
    }

    @Test
    fun `it should set 'didAlreadyVote(true)' when voting success`() {
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds(1)
        thenDidAlreadyVoteIs(true)
    }

    @Test
    fun `it should track 'View Feature' when voting success`() {
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationSucceeds(1)
        thenViewFeatureIsTracked()
    }

    @Test
    fun `it should remove votes when voting fails`() {
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationFails(1)
        thenVotesDoesNotContain("ref")
    }

    @Test
    fun `it should set 'didAlreadyVote(false)' when voting fails`() {
        givenDonation()
        givenSubject()
        whenFeatureSelected(mockModel)
        whenSubmittingDonationFails(1)
        thenDidAlreadyVoteIs(false)
    }

    private fun givenDonation() {
        givenDonation = Donation("title", "desc", "sku", "price", 10, "e", "12222")
    }


    private fun givenSubject() {
        subject = VoteViewModel(mockVoteUsecase, mockAnalytics)
    }

    private fun whenFeatureSelected(feature: BetaModel) {
        whenever(mockObservableField.get()).thenReturn("Feature Title")
        whenever(mockModel.feature).thenReturn(Feature("reference", "feature title", "feature description", "some image", "updates", 10, 2))
        whenever(mockModel.title).thenReturn(mockObservableField)
        subject.onFeatureSelected(feature)
    }

    private fun whenSubmittingDonation(amount: Int) {
        whenever(mockModel.votable).thenReturn(ObservableBoolean(true))
        subject.submitDonation(givenDonation)
    }

    private fun whenSubmittingDonationSucceeds(amount: Int) {
        whenever(mockModel.votable).thenReturn(ObservableBoolean(true))

        subject.submitDonation(givenDonation)

        verify(mockVoteUsecase).exec(any(), doneCaptor.capture(), any())
        doneCaptor.lastValue.invoke(SubmitVoteResponse(true))
    }

    private fun whenSubmittingDonationFails(amount: Int) {
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

    private fun thenViewFeatureIsTracked() {
        verify(mockAnalytics).trackViewFeature(any())
    }

}
