package nl.entreco.domain.beta.connect

import com.nhaarman.mockito_kotlin.verify
import nl.entreco.domain.common.executors.TestBackground
import nl.entreco.domain.common.executors.TestForeground
import nl.entreco.domain.repository.BillingRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 09/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class ConnectToBillingUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()

    @Mock private lateinit var mockDone: (Boolean) -> Unit
    @Mock private lateinit var mockBillingRepository: BillingRepository
    private lateinit var subject: ConnectToBillingUsecase

    @Test
    fun bind() {
        givenSubject()
        whenBinding()
        itShouldBindService()
    }

    @Test
    fun unbind() {
        givenSubject()
        whenUnbinding()
        itShouldUnbindService()
    }

    private fun givenSubject() {
        subject = ConnectToBillingUsecase(mockBillingRepository, bg, fg)
    }

    private fun whenBinding() {
        subject.bind(mockDone)
    }

    private fun whenUnbinding() {
        subject.unbind()
    }

    private fun itShouldBindService() {
        verify(mockBillingRepository).bind(mockDone)
    }

    private fun itShouldUnbindService() {
        verify(mockBillingRepository).unbind()
    }

}