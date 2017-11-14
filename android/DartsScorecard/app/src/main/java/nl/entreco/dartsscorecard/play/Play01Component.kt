package nl.entreco.dartsscorecard.play

import dagger.Subcomponent

/**
 * Created by Entreco on 14/11/2017.
 */
@Subcomponent(modules = arrayOf(Play01Module::class))
interface Play01Component {
    fun inject(a: Play01Activity)
}