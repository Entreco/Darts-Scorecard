package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import dagger.Subcomponent
import nl.entreco.dartsscorecard.play.Play01Component
import nl.entreco.dartsscorecard.play.Play01Module
import javax.inject.Singleton

/**
 * Created by Entreco on 14/11/2017.
 */
@Singleton
@Subcomponent(modules = arrayOf(ViewModelModule::class))
interface ViewModelComponent {
    fun inject(activity: Activity)
    fun plus(module: Play01Module) : Play01Component
}