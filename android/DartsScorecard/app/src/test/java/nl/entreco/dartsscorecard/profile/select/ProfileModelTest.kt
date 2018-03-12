package nl.entreco.dartsscorecard.profile.select

import nl.entreco.domain.model.players.PlayerPrefs
import nl.entreco.domain.profile.Profile
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by entreco on 05/03/2018.
 */
class ProfileModelTest {

    private lateinit var subject: ProfileModel

    @Before
    fun setUp() {
        subject = ProfileModel(Profile("name", 42, "some image", PlayerPrefs(180)))
    }

    @Test
    fun getId() {
        assertEquals(42, subject.id)
    }

    @Test
    fun getName() {
        assertEquals("name", subject.name.get())
    }

    @Test
    fun getFav() {
        assertEquals(180, subject.fav.get())
    }

    @Test
    fun getImage() {
        assertEquals("some image", subject.image.get())
    }

}
