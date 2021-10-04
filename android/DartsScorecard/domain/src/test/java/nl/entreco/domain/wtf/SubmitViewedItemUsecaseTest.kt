package nl.entreco.domain.wtf

import nl.entreco.domain.Analytics
import nl.entreco.domain.TestBackground
import nl.entreco.domain.TestForeground
import nl.entreco.domain.repository.WtfRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class SubmitViewedItemUsecaseTest {

    private val bg = TestBackground()
    private val fg = TestForeground()
    private val mockRepo: WtfRepository = mock()
    private val mockAnalytics: Analytics = mock()
    private lateinit var subject: SubmitViewedItemUsecase

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