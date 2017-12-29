package nl.entreco.dartsscorecard.setup.settings

import android.widget.Adapter
import android.widget.AdapterView
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.dartsscorecard.setup.Setup01ViewModel
import nl.entreco.domain.launch.TeamNamesString
import nl.entreco.domain.repository.CreateGameRequest
import nl.entreco.domain.repository.RetrieveGameRequest
import nl.entreco.domain.repository.TeamIdsString
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @Mock private lateinit var mockAdapterView: AdapterView<*>
    @Mock private lateinit var mockAdapter: Adapter

    private lateinit var subject: SettingsViewModel

    @Test
    fun `it should update startIndex`() {
        givenSetupViewModel()
        givenOnStartScoreSelected(0, arrayOf("501"))
        thenStartScoreEquals(501)
    }

    @Test
    fun `it should update number of sets when in range`() {
        givenSetupViewModel()
        whenSetProgressChanged(10)
        thenNumberOfSetsIs(11)
    }

    @Test
    fun `it should NOT update number of sets when out of range`() {
        givenSetupViewModel()
        whenSetProgressChanged(-1)
        thenNumberOfSetsIs(subject.startSets)
    }

    @Test
    fun `it should update number of legs when in range`() {
        givenSetupViewModel()
        whenLegProgressChanged(2)
        thenNumberOfLegsIs(3)
    }

    @Test
    fun `it should NOT update number of legs when out of range`() {
        givenSetupViewModel()
        whenLegProgressChanged(21)
        thenNumberOfLegsIs(subject.startLegs)
    }

    private fun givenSetupViewModel() {
        subject = SettingsViewModel()
    }

    private fun givenOnStartScoreSelected(index: Int, array: Array<String>) {
        whenever(mockAdapterView.adapter).thenReturn(mockAdapter)
        whenever(mockAdapter.getItem(index)).thenReturn(array[index])
        subject.onStartScoreSelected(mockAdapterView, index)
    }

    private fun whenSetProgressChanged(progress: Int) {
        subject.onSetsProgressChanged(progress)
    }

    private fun whenLegProgressChanged(progress: Int) {
        subject.onLegsProgressChanged(progress)
    }

    private fun thenStartScoreEquals(expected: Int) {
        assertEquals(expected, subject.startScore.get())
    }

    private fun thenNumberOfSetsIs(expected: Int) {
        assertEquals(expected, subject.numSets.get())
    }
    private fun thenNumberOfLegsIs(expected: Int) {
        assertEquals(expected, subject.numLegs.get())
    }
}