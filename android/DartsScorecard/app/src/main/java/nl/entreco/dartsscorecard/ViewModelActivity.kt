package nl.entreco.dartsscorecard

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelComponent
import nl.entreco.dartsscorecard.di.viewmodel.ViewModelModule

/**
 * Created by Entreco on 14/11/2017.
 */
abstract class ViewModelActivity : AppCompatActivity() {

    val Activity.app: App
        get() = application as App

    private val viewmodelComponent by lazy { app.appComponent.plus(ViewModelModule(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(viewmodelComponent)
        super.onCreate(savedInstanceState)
    }

    abstract fun inject(component: ViewModelComponent)
}