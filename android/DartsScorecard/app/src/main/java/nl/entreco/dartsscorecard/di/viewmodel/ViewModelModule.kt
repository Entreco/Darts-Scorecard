package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: Activity) {
    @Provides
    @ActivityScope
    fun context(): Context = activity

    @Provides
    @ActivityScope
    fun resources(): Resources = activity.resources
}