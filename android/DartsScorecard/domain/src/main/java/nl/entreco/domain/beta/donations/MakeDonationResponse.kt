package nl.entreco.domain.beta.donations

import android.app.PendingIntent

/**
 * Created by entreco on 09/02/2018.
 */
sealed class MakeDonationResponse{
    object Purchased : MakeDonationResponse()
    object Success : MakeDonationResponse()
    object Error : MakeDonationResponse()
    object Cancelled : MakeDonationResponse()
    object Unknown : MakeDonationResponse()
}