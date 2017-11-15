package nl.entreco.dartsscorecard.play

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(Play01Module::class))
interface Play01Component {
    fun inject(activity: Play01Activity)
}