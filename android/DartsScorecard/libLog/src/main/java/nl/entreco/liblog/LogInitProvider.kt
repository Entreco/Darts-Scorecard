package nl.entreco.liblog

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri

class LogInitProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        // Init library here
        Logger.enabled.set( BuildConfig.DEBUG )
        return true
    }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        if(info == null) throw NullPointerException("info cannot be null")
        // So if the authorities equal the library internal ones, the developer forgot to set his applicationId
        if ("nl.entreco.liblog.loginitprovider" == info.authority) {
            throw IllegalStateException("Incorrect provider authority in manifest. Most likely due to a "
                    + "missing applicationId variable in application\'s build.gradle.");
        }
        super.attachInfo(context, info)
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}