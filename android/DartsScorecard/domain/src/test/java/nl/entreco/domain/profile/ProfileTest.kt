package nl.entreco.domain.profile

import nl.entreco.domain.model.players.PlayerPrefs
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 17/03/2018.
 */
class ProfileTest {

    private lateinit var subject: Profile

    @Test
    fun getName() {
        givenSubject(name = "Remco")
        assertEquals("Remco", subject.name)
    }

    @Test
    fun getId() {
        givenSubject(id = 180)
        assertEquals(180, subject.id)
    }

    @Test
    fun getImage() {
        givenSubject(image = "content://some/image.jpg")
        assertEquals("content://some/image.jpg", subject.image)
    }

    @Test
    fun getPrefs() {
        givenSubject(fav = 20)
        assertEquals(20, subject.prefs.favoriteDouble)
    }

    private fun givenSubject(name: String = "player", id: Long = -2, image: String = "img", fav: Int = -2) {
        subject = Profile(name, id, image, PlayerPrefs(fav))
    }

}