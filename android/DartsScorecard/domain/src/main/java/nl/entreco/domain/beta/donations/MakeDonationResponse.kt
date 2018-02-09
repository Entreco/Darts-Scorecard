package nl.entreco.domain.beta.donations

import android.app.PendingIntent

/**
 * Created by entreco on 09/02/2018.
 */
data class MakeDonationResponse(val intent: PendingIntent, val payload: String)