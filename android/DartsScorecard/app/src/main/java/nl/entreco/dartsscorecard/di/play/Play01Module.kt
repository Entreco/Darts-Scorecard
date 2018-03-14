package nl.entreco.dartsscorecard.di.play

import android.content.Context
import android.support.v7.app.AlertDialog
import dagger.Module
import dagger.Provides
import nl.entreco.dartsscorecard.di.viewmodel.ActivityScope
import nl.entreco.data.sound.LocalSoundRepository
import nl.entreco.domain.repository.SoundRepository

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module {

    @Provides
    @Play01Scope
    fun provideAlertDialogBuilder(@ActivityScope context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }

    @Provides
    @Play01Scope
    fun provideSoundRepository(@ActivityScope context: Context): SoundRepository {
        return LocalSoundRepository(context)
    }
}
