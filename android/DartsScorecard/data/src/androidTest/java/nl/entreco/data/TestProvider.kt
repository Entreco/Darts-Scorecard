package nl.entreco.data

import nl.entreco.data.db.player.PlayerTable
import java.util.*

/**
 * Created by Entreco on 16/12/2017.
 */
class TestProvider {
    companion object {
        fun createPlayer(name: String, double: Int): PlayerTable {
            val table = PlayerTable()
            table.name = name
            table.fav = double.toString()
            table.uid = UUID.randomUUID().toString()
            return table
        }

    }
}