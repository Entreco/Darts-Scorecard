package nl.entreco.data.billing

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.android.vending.billing.IInAppBillingService

/**
 * Created by entreco on 08/02/2018.
 */
class BillingServiceConnection : ServiceConnection {

    private var service : IInAppBillingService? = null
    private var callback : (Boolean)->Unit = {}

    override fun onServiceDisconnected(name: ComponentName?) {
        this.service = null
        this.callback(false)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        this.service = IInAppBillingService.Stub.asInterface(service!!)
        this.callback(true)
    }

    fun setCallback(done: (Boolean)->Unit){
        this.callback = done
    }

    fun getService() : IInAppBillingService? {
        return service
    }
}