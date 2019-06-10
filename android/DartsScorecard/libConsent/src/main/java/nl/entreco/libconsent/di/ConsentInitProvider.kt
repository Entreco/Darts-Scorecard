package nl.entreco.libconsent.di

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import nl.entreco.libconsent.Consent
import nl.entreco.libconsent.ConsentPrefs
import nl.entreco.libconsent.retrieve.RetrieveConsentResponse
import nl.entreco.libconsent.retrieve.RetrieveConsentUsecase
import nl.entreco.libconsent.ask.StoreCurrentConsentUsecase

class ConsentInitProvider : ContentProvider() {

    private val prefs by lazy { context!!.getSharedPreferences(Consent.Prefs, Context.MODE_PRIVATE) }
    private val consent by lazy { ConsentPrefs(prefs) }
    private val retrieve by lazy { RetrieveConsentUsecase(context!!) }
    private val store by lazy { StoreCurrentConsentUsecase(consent) }

    override fun onCreate(): Boolean {
        context?.let {
            retrieve.go { response ->
                when (response) {
                    is RetrieveConsentResponse.Success -> handleSuccess(response)
                    is RetrieveConsentResponse.Error   -> handleError(response)
                }
            }
        }
        return true
    }

    private fun handleSuccess(response: RetrieveConsentResponse.Success) {
        store.go(response.status, response.eu)
    }

    private fun handleError(response: RetrieveConsentResponse.Error) {
        // Error retrieving Consent Status
        store.clear()
    }

    override fun attachInfo(context: Context?, info: ProviderInfo?) {
        if (info == null) throw NullPointerException("info cannot be null")
        // So if the authorities equal the library internal ones, the developer forgot to set his applicationId
        if ("nl.entreco.libconsent.consentinitprovider" == info.authority) {
            throw IllegalStateException("Incorrect provider authority in manifest. Most likely due to a "
                    + "missing applicationId variable in application\'s build.gradle.")
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