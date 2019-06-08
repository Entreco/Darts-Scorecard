package nl.entreco.libconsent.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import nl.entreco.libconsent.Consent
import nl.entreco.libconsent.ask.AskConsentUsecase
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import nl.entreco.libconsent.store.StoreCurrentConsentUsecase
import nl.entreco.shared.scopes.AppContext

@Module
object FetchConsentModule {

    @Provides
    @JvmStatic
    internal fun providePrefs(@AppContext context: Context): SharedPreferences = context.getSharedPreferences(Consent.Prefs, Context.MODE_PRIVATE)

    @Provides
    @JvmStatic
    fun provideFetchConsent(prefs: SharedPreferences): FetchCurrentConsentUsecase = FetchCurrentConsentUsecase(prefs)

    @Provides
    @JvmStatic
    fun provideStoreConsent(prefs: SharedPreferences): StoreCurrentConsentUsecase = StoreCurrentConsentUsecase(prefs)

    @Provides
    @JvmStatic
    fun provideAskConsent(store: StoreCurrentConsentUsecase): AskConsentUsecase = AskConsentUsecase(store)

}