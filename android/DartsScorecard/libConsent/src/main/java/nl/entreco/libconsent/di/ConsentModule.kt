package nl.entreco.libconsent.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import nl.entreco.libconsent.Consent

@Module
internal object ConsentModule {

    @Provides
    @JvmStatic
    fun providePrefs(context: Context): SharedPreferences = context.getSharedPreferences(Consent.Prefs, Context.MODE_PRIVATE)
}