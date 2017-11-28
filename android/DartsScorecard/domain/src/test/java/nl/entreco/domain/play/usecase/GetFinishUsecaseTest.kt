package nl.entreco.domain.play.usecase

import nl.entreco.domain.Logger
import nl.entreco.domain.play.model.Turn
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Entreco on 24/11/2017.
 */
@RunWith(MockitoJUnitRunner::class)
class GetFinishUsecaseTest {

    @Mock private lateinit var mockLogger: Logger
    @InjectMocks lateinit var subject : GetFinishUsecase

    @Test
    fun `it should return empty for scores higher than 170`() {
        assertEquals("", subject.calculateInBack(171, Turn(), 20))
    }

    @Test
    fun `it should return T20 T20 BULL for score of 170`() {
        assertEquals("T20 T20 BULL", subject.calculateInBack(170, Turn(), 20))
    }
}