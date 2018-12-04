package nl.entreco.dartsscorecard.base

import androidx.appcompat.app.AlertDialog
import com.nhaarman.mockito_kotlin.*
import nl.entreco.domain.model.players.Team
import nl.entreco.domain.repository.RatingPrefRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 20/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class DialogHelperTest {

    @Mock private lateinit var mockDialog: AlertDialog
    @Mock private lateinit var mockBuilder: AlertDialog.Builder
    @Mock private lateinit var mockSelect: (Int) -> Unit
    @Mock private lateinit var mockRatingPrefs: RatingPrefRepository
    private lateinit var subject: DialogHelper

    private var givenTeams: MutableList<Team> = mutableListOf()


    @Before
    fun setUp() {
        whenever(mockBuilder.create()).thenReturn(mockDialog)
        whenever(mockBuilder.setNegativeButton(any<Int>(), any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setPositiveButton(any<Int>(), any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setSingleChoiceItems(anyArray(), any(), any())).thenReturn(mockBuilder)
        whenever(mockBuilder.setTitle(any<Int>())).thenReturn(mockBuilder)
        subject = DialogHelper(mockBuilder, mockRatingPrefs)
    }

    @Test
    fun `it should show single choice dialog, when 'revanche' with more than 1 team`() {
        givenTeams(2)
        whenRevanche()
        thenDialogIsShown()
    }

    @Test
    fun `it should directly select first team, when 'revanche' with 1 team`() {
        givenTeams(1)
        whenRevanche()
        thenSelectedItemIs(0)
        thenDialogisNotShown()
    }

    @Test
    fun `it should do nothing, when 'revanche' with 0 team`() {
        givenTeams(0)
        whenRevanche()
        thenDialogisNotShown()
    }

    private fun givenTeams(numberOfTeams: Int) {
        (0 until numberOfTeams).forEach {
            givenTeams.add(mock())
        }
    }

    private fun whenRevanche() {
        subject.revanche(0, givenTeams.toTypedArray(), mockSelect)
    }

    private fun thenDialogIsShown() {
        verify(mockDialog).show()
    }

    private fun thenDialogisNotShown() {
        verify(mockDialog, never()).show()
    }

    private fun thenSelectedItemIs(expected: Int) {
        verify(mockSelect).invoke(expected)
    }

}
