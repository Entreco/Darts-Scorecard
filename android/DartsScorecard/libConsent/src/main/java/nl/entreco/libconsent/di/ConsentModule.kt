package nl.entreco.libconsent.di

import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.libconsent.Consent
import nl.entreco.libconsent.ConsentPrefs

@Module
object ConsentModule {

    @Provides
    @JvmStatic
    fun provideConsentPrefs(context: Context): ConsentPrefs {
        val prefs = context.getSharedPreferences(Consent.Prefs, Context.MODE_PRIVATE)
        return ConsentPrefs(prefs)
    }
}