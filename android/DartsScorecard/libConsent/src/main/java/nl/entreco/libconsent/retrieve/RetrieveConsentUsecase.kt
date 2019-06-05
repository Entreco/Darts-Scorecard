package nl.entreco.libconsent.retrieve

import android.content.Context
import com.google.ads.consent.ConsentInfoUpdateListener
import com.google.ads.consent.ConsentInformation
import com.google.ads.consent.ConsentStatus
import nl.entreco.libconsent.R
import javax.inject.Inject


class RetrieveConsentUsecase @Inject constructor(
        private val context: Context
) {

    fun go(done: (RetrieveConsentResponse) -> Unit) {
        val consentInformation = ConsentInformation.getInstance(context)
        val publisherIds = arrayOf(context.resources.getString(R.string.publisher_id))
        consentInformation.requestConsentInfoUpdate(publisherIds, object : ConsentInfoUpdateListener {
            override fun onConsentInfoUpdated(consentStatus: ConsentStatus) {
                done(RetrieveConsentResponse.success(consentStatus.name, consentInformation.isRequestLocationInEeaOrUnknown))
            }

            override fun onFailedToUpdateConsentInfo(errorDescription: String) {
                done(RetrieveConsentResponse.failure(errorDescription))
            }
        })
    }
}