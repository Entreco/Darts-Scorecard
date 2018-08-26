package nl.entreco.dartsscorecard.di.viewmodel

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule
import nl.entreco.dartsscorecard.di.launch.LaunchComponent
import nl.entreco.dartsscorecard.di.launch.LaunchModule
import nl.entreco.dartsscorecard.di.play.Play01Component
import nl.entreco.dartsscorecard.di.play.Play01Module
import nl.entreco.dartsscorecard.di.profile.*
import nl.entreco.dartsscorecard.di.setup.EditPlayerComponent
import nl.entreco.dartsscorecard.di.setup.EditPlayerModule
import nl.entreco.dartsscorecard.di.setup.Setup01Component
import nl.entreco.dartsscorecard.di.setup.Setup01Module
import nl.entreco.dartsscorecard.di.viewmodel.ad.AdModule
import nl.entreco.dartsscorecard.di.viewmodel.api.FaqApiModule
import nl.entreco.dartsscorecard.di.viewmodel.api.FeatureApiModule
import nl.entreco.dartsscorecard.di.viewmodel.db.*
import nl.entreco.dartsscorecard.di.viewmodel.threading.ThreadingModule
import nl.entreco.dartsscorecard.di.faq.WtfComponent
import nl.entreco.dartsscorecard.di.faq.WtfModule
import nl.entreco.dartsscorecard.di.streaming.StreamComponent
import nl.entreco.dartsscorecard.di.streaming.StreamModule
import nl.entreco.dartsscorecard.di.tv.TvLaunchComponent
import nl.entreco.dartsscorecard.di.tv.TvLaunchModule
import nl.entreco.dartsscorecard.di.viewmodel.tv.TvModule

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = [(ViewModelModule::class), (ThreadingModule::class), (AdModule::class),
    (GameDbModule::class), (PlayerDbModule::class), (TurnDbModule::class),
    (MetaDbModule::class), (StatDbModule::class), (FeatureApiModule::class),
    (FaqApiModule::class), (TvModule::class)])
interface ViewModelComponent {

    // Where can this be used
    fun plus(module: LaunchModule): LaunchComponent
    fun plus(module: BetaModule): BetaComponent
    fun plus(module: WtfModule): WtfComponent
    fun plus(module: Setup01Module): Setup01Component
    fun plus(module: EditPlayerModule): EditPlayerComponent
    fun plus(module: Play01Module): Play01Component
    fun plus(module: SelectProfileModule): SelectProfileComponent
    fun plus(module: ProfileModule): ProfileComponent
    fun plus(module: EditPlayerNameModule): EditPlayerNameComponent
    fun plus(module: TvLaunchModule): TvLaunchComponent
    fun plus(module: StreamModule) : StreamComponent
}
