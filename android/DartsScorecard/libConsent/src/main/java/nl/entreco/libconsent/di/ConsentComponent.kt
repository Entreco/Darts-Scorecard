package nl.entreco.libconsent.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import nl.entreco.libconsent.fetch.FetchCurrentConsentUsecase
import nl.entreco.libconsent.retrieve.RetrieveConsentUsecase
import nl.entreco.libconsent.store.StoreCurrentConsentUsecase

@Component(modules = [ConsentModule::class])
interface ConsentComponent {

    fun retrieve(): RetrieveConsentUsecase
    fun store(): StoreCurrentConsentUsecase
    fun fetch() : FetchCurrentConsentUsecase

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): ConsentComponent
    }
}