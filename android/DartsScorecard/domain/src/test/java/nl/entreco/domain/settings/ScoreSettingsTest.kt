package nl.entreco.domain.settings

import nl.entreco.domain.model.Score
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Entreco on 18/12/2017.
 */
class ScoreSettingsTest {

    @Test
    fun score() {
        assertEquals(Score(501, 0,0), ScoreSettings(501,3,2,0).score())
    }

}