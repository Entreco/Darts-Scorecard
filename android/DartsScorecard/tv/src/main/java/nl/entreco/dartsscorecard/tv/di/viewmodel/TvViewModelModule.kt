package nl.entreco.dartsscorecard.tv.di.viewmodel

import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import dagger.Module
import dagger.Provides

@Module
class TvViewModelModule(private val activity: FragmentActivity) {

    @Provides
    @TvActivityScope
    fun context(): Context = activity

    @Provides
    @TvActivityScope
    fun resources(): Resources = activity.resources

}