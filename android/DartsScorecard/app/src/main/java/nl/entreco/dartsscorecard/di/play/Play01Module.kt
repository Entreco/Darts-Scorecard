package nl.entreco.dartsscorecard.di.play

import android.content.Context
import android.support.v7.app.AlertDialog
import dagger.Module
import dagger.Provides

/**
 * Created by Entreco on 14/11/2017.
 */
@Module
class Play01Module {

    @Provides
    @Play01Scope
    fun provideAlertDialogBuilder(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
    }
}
