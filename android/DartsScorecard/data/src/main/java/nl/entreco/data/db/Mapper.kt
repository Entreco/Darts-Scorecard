package nl.entreco.data.db

/**
 * Created by Entreco on 16/12/2017.
 */
interface Mapper<in F, out T> {

    fun to(from: F): T

    fun to(froms: List<F>): List<T> {
        val list = ArrayList<T>()
        return froms.mapTo(list) { to(it) }
    }
}