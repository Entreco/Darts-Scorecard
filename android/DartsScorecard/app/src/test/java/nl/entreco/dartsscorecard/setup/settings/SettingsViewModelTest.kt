package nl.entreco.dartsscorecard.setup.settings

import android.widget.Adapter
import android.widget.AdapterView
import android.widget.SeekBar
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import nl.entreco.domain.setup.usecase.FetchPreferredSettingsUsecase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 29/12/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @Mock private lateinit var mockFetchSettings: FetchPreferredSettingsUsecase
    @Mock private lateinit var mockAdapterView: AdapterView<*>
    @Mock private lateinit var mockAdapter: Adapter
    @Mock private lateinit var mockSeekbar: SeekBar

    private lateinit var subject: SettingsViewModel

    @Test
    fun `it should update startIndex`() {
        givenSetupViewModel()
        givenOnStartScoreSelected(0, arrayOf("501"))
        thenStartScoreEquals(501)
        thenStartScoreIndexEquals(0)
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
        thenNumberOfSetsIs(subject.numSets.get())
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
        thenNumberOfLegsIs(subject.numLegs.get())
    }

    @Test
    fun `it should update legs progress`() {
        givenSetupViewModel()
        whenLegsChanged(6)
        verify(mockSeekbar).progress = 6
    }

    @Test
    fun `it should update sets progress`() {
        givenSetupViewModel()
        whenSetsChanged(8)
        verify(mockSeekbar).progress = 8
    }

    @Test
    fun `it should generate correct request`() {
        givenSetupViewModel()
        givenOnStartScoreSelected(0, arrayOf("501"))
        whenLegProgressChanged(6)
        whenSetProgressChanged(8)
        thenRequestIs(0, 501, 6 + 1, 8 + 1)
    }

    private fun givenSetupViewModel() {
        subject = SettingsViewModel(mockFetchSettings)
    }

    private fun givenOnStartScoreSelected(index: Int, array: Array<String>) {
        whenever(mockAdapterView.getItemAtPosition(index)).thenReturn(array[index])
        subject.onStartScoreSelected(mockAdapterView, index)
    }

    private fun whenSetProgressChanged(progress: Int) {
        subject.onSetsProgressChanged(progress)
    }

    private fun whenLegProgressChanged(progress: Int) {
        subject.onLegsProgressChanged(progress)
    }

    private fun whenLegsChanged(delta: Int) {
        subject.onLegsChanged(mockSeekbar, delta)
    }

    private fun whenSetsChanged(delta: Int) {
        subject.onSetsChanged(mockSeekbar, delta)
    }

    private fun thenStartScoreEquals(expected: Int) {
        assertEquals(expected, subject.startScore.get())
    }

    private fun thenStartScoreIndexEquals(expected: Int) {
        assertEquals(expected, subject.startScoreIndex.get())
    }

    private fun thenNumberOfSetsIs(expected: Int) {
        assertEquals(expected, subject.numSets.get())
    }

    private fun thenNumberOfLegsIs(expected: Int) {
        assertEquals(expected, subject.numLegs.get())
    }

    private fun thenRequestIs(index: Int, score: Int, legs: Int, sets: Int) {
        val request = subject.setupRequest()
        assertEquals(index, request.startIndex)
        assertEquals(score, request.startScore)
        assertEquals(legs, request.numLegs)
        assertEquals(sets, request.numSets)
    }
}