package nl.entreco.dartsscorecard.di.viewmodel

import android.app.Activity
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.App

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class ViewModelModule(private val activity: Activity) {
    @Provides @ActivityScope fun context() = activity
    @Provides @ActivityScope fun application(app: App) = app.applicationContext
}