package nl.entreco.dartsscorecard.beta

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.isNull
import com.nhaarman.mockito_kotlin.verify
import nl.entreco.dartsscorecard.TestBackground
import nl.entreco.dartsscorecard.TestForeground
import nl.entreco.domain.beta.Feature
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 26/02/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class BetaAdapterTest {

    @Mock private lateinit var mockFeature: Feature
    @Mock private lateinit var mockBetaView: BetaView
    private lateinit var subject: BetaAdapter

    @Before
    fun setUp() {
        subject = BetaAdapter(TestBackground(), TestForeground())
    }

    @Test
    fun getItemCount() {
        assertEquals(0, subject.itemCount)
    }


    @Test(expected = NullPointerException::class) // DiffUtils not mockable
    fun onBindViewHolder() {
        subject.onChanged(listOf(mockFeature))
        subject.onBindViewHolder(mockBetaView, 0)
        verify(mockBetaView).bind(any(), isNull())
    }

    @Test
    fun onChanged() {
        subject.onChanged(emptyList())
        assertEquals(0, subject.itemCount)
    }

}
