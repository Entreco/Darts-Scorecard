package nl.entreco.dartsscorecard.play.score

import android.view.View
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.databinding.TeamScoreViewBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 16/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class TeamScoreViewTest {

    @Mock private lateinit var mockTeamScoreViewModel: TeamScoreViewModel
    @Mock private lateinit var mockView: View
    @Mock private lateinit var mockBinding: TeamScoreViewBinding
    private lateinit var subject: TeamScoreView

    @Before
    fun setUp() {
        whenever(mockBinding.root).thenReturn(mockView)
        subject = TeamScoreView(mockBinding)
    }

    @Test
    fun `it should set viewmodel and execute pending bindings when binding`() {
        subject.bind(mockTeamScoreViewModel)
        verify(mockBinding).viewModel = mockTeamScoreViewModel
        verify(mockBinding).executePendingBindings()
    }

}