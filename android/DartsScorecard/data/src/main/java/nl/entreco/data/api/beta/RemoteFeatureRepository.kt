package nl.entreco.data.api.beta

import com.google.firebase.firestore.*
import nl.entreco.domain.Logger
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
class RemoteFeatureRepository(private val db: FirebaseFirestore, private val logger: Logger) : FeatureRepository, EventListener<QuerySnapshot> {
    private val featureRef = db.collection("features")
    private val listener = featureRef.addSnapshotListener(this)
    internal var onChange: (List<Feature>) -> Unit = {}
    private val features = mutableMapOf<String, Feature>()

    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
        if (p1 != null) {
            logger.w("Unable to read /features from Firestore")
            return
        }

        features.clear()

        p0?.documents?.forEach { doc ->
            convertToFeature(doc)
        }.also { onChange(ArrayList<Feature>(features.values)) }
    }

    private fun convertToFeature(doc: DocumentSnapshot) {
        try {
            val feature = doc.toObject(FeatureApiData::class.java)
            features[doc.id] = Feature(doc.id, feature.title, feature.description, feature.image, feature.progress
                    ?: "", feature.goal, feature.count)
        } catch (oops: Exception) {
            logger.e("Unable to convert snapshot to feature( $doc ) $oops")
        }
    }

    override fun subscribe(onChange: (List<Feature>) -> Unit): List<Feature> {
        this.onChange = onChange
        return ArrayList<Feature>(features.values)
    }

    override fun submitVote(featureId: String, amount: Int) {
        val feature = featureRef.document(featureId)
        db.runTransaction { transaction ->
            val max = transaction.get(feature).getLong("goal")
            val count = transaction.get(feature).getLong("count") + amount
            if (count <= max) {
                transaction.update(feature, "count", count)
            } else {
                transaction.update(feature, "count", max)
            }
        }
    }

    override fun unsubscribe() {
        this.onChange = {}
        this.listener.remove()
    }
}
