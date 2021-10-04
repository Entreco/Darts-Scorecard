package nl.entreco.libcore

import nl.entreco.libcore.threading.Background
import nl.entreco.libcore.threading.Foreground
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/*************************************************************************
 *
 * ONWARD CONFIDENTIAL
 * __________________
 *
 * [2021] GTX medical
 * All Rights Reserved.
 *
 */
class BaseUsecaseTest {

    private val mockError: (Throwable) -> Unit = mock()
    private val mockFunction: () -> Unit = mock()
    private val mockFg: Foreground = mock()
    private val mockBg: Background = mock()
    private lateinit var subject: BaseUsecase
    private val functionCaptor = argumentCaptor<Runnable>()

    @Test
    fun `it should post on background`() {
        givenSubject()
        whenPostingOnBg(mockFunction, mockError)
        thenFunctionIsPostedOnBg()
        verify(mockFunction).invoke()
    }

    @Test
    fun `it should post on Ui thread_valid`() {
        givenSubject()
        whenPostingOnUi(mockFunction)
        thenFunctionIsPostedOnUi()
    }

    private fun givenSubject() { subject =MockBaseUseCase(mockBg, mockFg) }

    private fun whenPostingOnUi(f: () -> Unit) {
        subject.onUi(f)
    }

    private fun whenPostingOnBg(f: () -> Unit, err: (Throwable) -> Unit) {
        subject.onBackground(f, err)
    }

    private fun thenFunctionIsPostedOnBg() {
        verify(mockBg).post(functionCaptor.capture())
        functionCaptor.lastValue.run()
    }

    private fun thenFunctionIsPostedOnUi() {
        verify(mockFg).post(functionCaptor.capture())
        functionCaptor.lastValue.run()
    }


    inner class MockBaseUseCase(
        bg: Background,
        fg: Foreground,
    ) : BaseUsecase(bg, fg) {

    }
}