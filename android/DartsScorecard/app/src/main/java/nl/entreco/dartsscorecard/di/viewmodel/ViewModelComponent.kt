package nl.entreco.dartsscorecard.di.viewmodel

import dagger.Subcomponent
import nl.entreco.dartsscorecard.di.beta.BetaComponent
import nl.entreco.dartsscorecard.di.beta.BetaModule
import nl.entreco.dartsscorecard.di.faq.WtfComponent
import nl.entreco.dartsscorecard.di.faq.WtfModule
import nl.entreco.dartsscorecard.di.hiscore.HiscoreComponent
import nl.entreco.dartsscorecard.di.hiscore.HiscoreModule
import nl.entreco.dartsscorecard.di.launch.LaunchComponent
import nl.entreco.dartsscorecard.di.launch.LaunchModule
import nl.entreco.dartsscorecard.di.play.Play01Component
import nl.entreco.dartsscorecard.di.play.Play01Module
import nl.entreco.dartsscorecard.di.profile.EditPlayerNameComponent
import nl.entreco.dartsscorecard.di.profile.EditPlayerNameModule
import nl.entreco.dartsscorecard.di.profile.ProfileComponent
import nl.entreco.dartsscorecard.di.profile.ProfileModule
import nl.entreco.dartsscorecard.di.profile.SelectProfileComponent
import nl.entreco.dartsscorecard.di.profile.SelectProfileModule
import nl.entreco.dartsscorecard.di.settings.SettingsComponent
import nl.entreco.dartsscorecard.di.settings.SettingsModule
import nl.entreco.dartsscorecard.di.setup.EditPlayerComponent
import nl.entreco.dartsscorecard.di.setup.EditPlayerModule
import nl.entreco.dartsscorecard.di.setup.Setup01Component
import nl.entreco.dartsscorecard.di.setup.Setup01Module
import nl.entreco.dartsscorecard.di.viewmodel.api.FaqApiModule
import nl.entreco.dartsscorecard.di.viewmodel.api.FeatureApiModule
import nl.entreco.dartsscorecard.di.viewmodel.db.BotDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.GameDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.MetaDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.PlayerDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.StatDbModule
import nl.entreco.dartsscorecard.di.viewmodel.db.TurnDbModule
import nl.entreco.dartsscorecard.di.viewmodel.threading.ThreadingModule
import nl.entreco.shared.scopes.ActivityScope

/**
 * Created by Entreco on 14/11/2017.
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class, ThreadingModule::class,
    GameDbModule::class, PlayerDbModule::class, BotDbModule::class,
    TurnDbModule::class, MetaDbModule::class, StatDbModule::class,
    FeatureApiModule::class, FaqApiModule::class])
interface ViewModelComponent {

    // Where can this be used
    fun plus(module: LaunchModule): LaunchComponent

    fun plus(module: SettingsModule): SettingsComponent
    fun plus(module: BetaModule): BetaComponent
    fun plus(module: HiscoreModule): HiscoreComponent
    fun plus(module: WtfModule): WtfComponent
    fun plus(module: Setup01Module): Setup01Component
    fun plus(module: EditPlayerModule): EditPlayerComponent
    fun plus(module: Play01Module): Play01Component
    fun plus(module: SelectProfileModule): SelectProfileComponent
    fun plus(module: ProfileModule): ProfileComponent
    fun plus(module: EditPlayerNameModule): EditPlayerNameComponent
}
