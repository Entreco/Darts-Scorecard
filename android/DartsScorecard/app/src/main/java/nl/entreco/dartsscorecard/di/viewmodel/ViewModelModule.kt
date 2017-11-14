package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import dagger.Module
import dagger.Provides

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(val activity: Activity) {
    @Provides
    fun context() = activity
}