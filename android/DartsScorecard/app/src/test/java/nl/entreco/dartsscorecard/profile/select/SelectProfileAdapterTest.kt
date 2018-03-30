package nl.entreco.dartsscorecard.profile.select

import nl.entreco.domain.profile.Profile
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by entreco on 12/03/2018.
 */
@RunWith(MockitoJUnitRunner::class)
class SelectProfileAdapterTest {

    @Mock private lateinit var mockNavigator: SelectProfileNavigator
    private lateinit var subject: SelectProfileAdapter

    @Test
    fun `it should set items (0)`() {
        givenSubject()
        whenSettingItems()
        thenItemCountIs(0)
    }

    @Test
    fun `it should set items (1)`() {
        givenSubject()
        whenSettingItems("one")
        thenItemCountIs(1)
    }

    @Test
    fun `it should set items (2)`() {
        givenSubject()
        whenSettingItems("one", "two")
        thenItemCountIs(2)
    }

    @Test
    fun `it should remote item`() {
        givenSubject()
        whenSettingItems("one", "two")
        whenRemovingItem(0)
        thenItemCountIs(1)
    }

    private fun givenSubject() {
        subject = SelectProfileAdapter(mockNavigator)
    }

    private fun whenSettingItems(vararg items: String) {
        subject.setItems(items.map { Profile(it, image = "image") })
    }

    private fun whenRemovingItem(position: Int) {
        subject.removeAt(position)
    }

    private fun thenItemCountIs(expected: Int) {
        assertTrue(subject.itemCount == expected)
    }

}