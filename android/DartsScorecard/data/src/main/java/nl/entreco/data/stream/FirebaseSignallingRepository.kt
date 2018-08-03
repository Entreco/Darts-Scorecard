package nl.entreco.data.stream

import com.google.firebase.firestore.FirebaseFirestore
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger

class FirebaseSignallingRepository(db: FirebaseFirestore, private val logger: Logger) : SignallingRepository {

    private val collectionReference = db.collection("stream")

    private val channel = mapOf<String, Any>(Pair("name", "Los Angeles"))

    override fun register(uuid: String) {
        collectionReference.document(uuid).set(channel).addOnCompleteListener {
            logger.d("Yeah -> Firebase COMPLETE: $it")
        }
    }
}