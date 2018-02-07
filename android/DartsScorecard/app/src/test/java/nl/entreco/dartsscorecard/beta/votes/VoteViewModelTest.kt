package nl.entreco.dartsscorecard.beta.votes

import android.databinding.ObservableField
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.beta.BetaModel
import nl.entreco.domain.Analytics
import nl.entreco.domain.beta.SubmitVoteUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 07/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class VoteViewModelTest {

    @Mock private lateinit var mockObservableField : ObservableField<String>
    @Mock private lateinit var mockVoteUsecase : SubmitVoteUsecase
    @Mock private lateinit var mockAnalytics : Analytics
    @Mock private lateinit var mockFeature: BetaModel
    private lateinit var subject: VoteViewModel

    @Test
    fun `it should set selected feature in field`() {
        givenSubject()
        whenFeatureSelected(mockFeature)
        thenSelectedFeatureIs(mockFeature)
    }

    @Test
    fun `it should track achievement when feature selected`() {
        givenSubject()
        whenFeatureSelected(mockFeature)
        thenAchievementIsTracked()
    }

    private fun givenSubject() {
        subject = VoteViewModel(mockVoteUsecase, mockAnalytics)
    }

    private fun whenFeatureSelected(feature: BetaModel) {
        whenever(mockObservableField.get()).thenReturn("Feature Title")
        whenever(mockFeature.title).thenReturn(mockObservableField)
        subject.onFeatureSelected(feature)
    }

    private fun thenSelectedFeatureIs(expected: BetaModel) {
        assertEquals(expected, subject.feature.get())
    }

    private fun thenAchievementIsTracked() {
        verify(mockAnalytics).trackAchievement(any())
    }

}