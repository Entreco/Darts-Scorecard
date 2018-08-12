package nl.entreco.data.stream

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.entreco.domain.repository.AnswersRepository
import nl.entreco.domain.streaming.ice.DscSessionDescription
import nl.entreco.shared.log.Logger

class FirebaseAnswersRepository(
        private val db: FirebaseDatabase,
        private val logger: Logger,
        private val currentDeviceUuid: String
) : AnswersRepository {

    companion object {
        private const val ANSWERS_PATH = "answers/"
    }

    private fun deviceAnswersPath(uuid: String) = ANSWERS_PATH.plus(uuid)

    override fun create(recipientUuid: String, localSessionDescription: DscSessionDescription) {
        val reference = db.getReference(deviceAnswersPath(recipientUuid))
        reference.onDisconnect().removeValue()

        val aha = SessionDescriptionFirebaseApiData()
        aha.uuid = currentDeviceUuid
        aha.type = localSessionDescription.type
        aha.description = localSessionDescription.description

        logger.w("WEBRTC: create Answer $aha")

        reference.setValue(aha)
    }

    override fun listenForNewAnswers(onChange: (DscSessionDescription) -> Unit) {
        val reference = db.getReference(deviceAnswersPath(currentDeviceUuid))
        val valueListener = object :
                ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                p0.getValue(SessionDescriptionFirebaseApiData::class.java)?.let { description ->

                    logger.w("WEBRTC: listenForNewAnswers $description")
                    val session = DscSessionDescription(description.type, description.description ?: "")
                    onChange(session)
                }
            }
        }
        reference.addValueEventListener(valueListener)
    }
}