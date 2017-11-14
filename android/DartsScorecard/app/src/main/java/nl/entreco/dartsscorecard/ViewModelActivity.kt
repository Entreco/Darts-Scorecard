package nl.entreco.dartsscorecard

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

/**
 * Created by Entreco on 14/11/2017.
 */
abstract class ViewModelActivity : AppCompatActivity() {

    val Activity.app: App
        get() = application as App

    protected val viewmodelComponent by lazy { app.appComponent.plus(ViewModelModule(this)) }
}