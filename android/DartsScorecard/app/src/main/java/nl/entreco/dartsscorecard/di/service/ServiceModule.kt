package nl.entreco.dartsscorecard.di.service

import android.app.Service
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(private val service: Service) {

    @Provides
    @ServiceScope
    fun context(): Context = service
}