package nl.entreco.libconsent.di

import android.content.Context
import dagger.Module
import dagger.Provides
import nl.entreco.libconsent.Consent
import nl.entreco.libconsent.ConsentPrefs
import nl.entreco.libconsent.ask.AskConsentUsecase
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import nl.entreco.libconsent.store.StoreCurrentConsentUsecase
import nl.entreco.shared.scopes.AppContext

@Module
object FetchConsentModule {

    @Provides
    @JvmStatic
    fun provideConsentPrefs(@AppContext context: Context): ConsentPrefs {
        val prefs = context.getSharedPreferences(Consent.Prefs, Context.MODE_PRIVATE)
        return ConsentPrefs(prefs)
    }

    @Provides
    @JvmStatic
    fun provideAskConsent(prefs: ConsentPrefs): AskConsentUsecase {
        val store = StoreCurrentConsentUsecase(prefs)
        return AskConsentUsecase(store)
    }
}