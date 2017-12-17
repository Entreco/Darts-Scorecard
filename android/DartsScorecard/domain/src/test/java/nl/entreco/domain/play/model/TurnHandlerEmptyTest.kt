package nl.entreco.domain.play.model

import nl.entreco.domain.play.TestProvider
import nl.entreco.domain.play.model.players.NoPlayer
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Created by Entreco on 18/11/2017.
 */
class TurnHandlerEmptyTest {

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling start()`() {
        TurnHandler().start()
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling next()`() {
        TurnHandler().next(emptyArray())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling nextLeg()`() {
        TurnHandler().nextLeg(emptyArray())
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw exception if teams are not set and calling nextSet()`() {
        TurnHandler().nextSet(emptyArray())
    }

    @Test
    fun `it should return NoPlayer initially`() {
        assertEquals(NoPlayer().toString(), TestProvider().turnHandler().toString())
    }

    @Test
    fun `it should return NoPlayer after starting with no teams`() {
        assertNotEquals(NoPlayer(), TestProvider().turnHandler().start().player)
    }
}