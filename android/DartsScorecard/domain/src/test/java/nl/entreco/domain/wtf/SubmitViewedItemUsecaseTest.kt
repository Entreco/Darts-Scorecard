package nl.entreco.domain.wtf

import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import nl.entreco.domain.Analytics
import nl.entreco.libcore.threading.TestBackground
import nl.entreco.libcore.threading.TestForeground
import nl.entreco.domain.repository.WtfRepository
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SubmitViewedItemUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    @Mock private lateinit var mockRepo: WtfRepository
    @Mock private lateinit var mockAnalytics: Analytics
    private lateinit var subject : SubmitViewedItemUsecase


    @Before
    fun setUp() {
        subject = SubmitViewedItemUsecase(mockRepo, mockAnalytics, bg, fg)
    }

    @Test
    fun `it should use repo`() {
        whenExecuting("some id")
        verify(mockRepo).viewedItem("some id")
    }

    @Test
    fun `it should track analytics`() {
        whenExecuting("another Id")
        verify(mockAnalytics).trackViewFaq(any())
    }

    private fun whenExecuting(docId: String) {
        subject.exec(SubmitViewedItemRequest(WtfItem(docId, "tit", "desc", "img", "vid", 2)))
    }
}