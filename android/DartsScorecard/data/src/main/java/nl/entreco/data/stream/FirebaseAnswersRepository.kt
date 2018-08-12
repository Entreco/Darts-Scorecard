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

    private fun deviceAnswersPath(deviceUuid: String) = ANSWERS_PATH.plus(deviceUuid)

    override fun create(localSessionDescription: DscSessionDescription) {
        val reference = db.getReference(deviceAnswersPath(localSessionDescription.uuid))
        reference.onDisconnect().removeValue()

        val aha = SessionDescriptionFirebaseApiData()
        aha.uuid = currentDeviceUuid
        aha.type = localSessionDescription.type
        aha.description = localSessionDescription.description

        reference.setValue(aha)
    }

    override fun listenForNewAnswers(onChange: (DscSessionDescription) -> Unit) {
        db.getReference(deviceAnswersPath(currentDeviceUuid)).addValueEventListener(object :
                ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val description = p0.getValue(SessionDescriptionFirebaseApiData::class.java)
                onChange(DscSessionDescription(currentDeviceUuid, description?.type ?: -1,
                        description?.description ?: ""))
            }
        })


    }
}