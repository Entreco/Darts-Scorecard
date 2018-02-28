package nl.entreco.data.db

import nl.entreco.data.db.player.PlayerTable

/**
 * Created by Entreco on 16/12/2017.
 */
interface Mapper<in F, out T> {

    fun to(from: F): T

    fun to(froms: List<F>): List<T> {
        val list = ArrayList<T>()
        return froms.mapTo(list) { to(it) }
    }

    fun from(id: Long, table: PlayerTable, name: String? = null, image: String? = null, double: String? = null): PlayerTable {
        if(id != table.id) throw IllegalStateException("Trying to update wrong Profile ($id, $table)")
        name?.let { table.name = it }
        double?.let { table.fav = it }
        image?.let { table.image = it }
        return table
    }
}
