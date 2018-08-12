package nl.entreco.data.stream

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.entreco.domain.repository.OffersRepository
import nl.entreco.domain.streaming.ice.DscSessionDescription
import nl.entreco.shared.log.Logger

class FirebaseOffersRepository(private val db: FirebaseDatabase,
                               private val logger: Logger,
                               private val currentDeviceUuid: String) : OffersRepository {

    companion object {
        private const val OFFERS_PATH = "offers/"
    }

    private fun deviceOffersPath(uuid: String) = OFFERS_PATH.plus(uuid)

    override fun create(recipientUuid: String, localSessionDescription: DscSessionDescription) {
        val reference = db.getReference(deviceOffersPath(recipientUuid))
        reference.onDisconnect().removeValue()

        val aha = SessionDescriptionFirebaseApiData()
        aha.uuid = currentDeviceUuid
        aha.type = localSessionDescription.type
        aha.description = localSessionDescription.description

        logger.w("WEBRTC: create Offer $aha")

        reference.setValue(aha)
    }

    override fun listenForNewOffersWithUuid(onChange: (String, DscSessionDescription) -> Unit) {
        val reference = db.getReference(deviceOffersPath(currentDeviceUuid))
        val valueListener = object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                p0.getValue(SessionDescriptionFirebaseApiData::class.java)?.let { description ->

                    logger.w("WEBRTC: listenForNewOffersWithUuid $description")
                    val session = DscSessionDescription(description.type, description.description ?: "")
                    onChange(currentDeviceUuid, session)

                }
            }
        }
        reference.addValueEventListener(valueListener)
    }
}