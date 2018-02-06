package nl.entreco.data.api.beta

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import nl.entreco.domain.Logger
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
class RemoteFeatureRepository(db: FirebaseFirestore, private val logger: Logger) : FeatureRepository, EventListener<QuerySnapshot> {
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
            val feature = doc.toObject(FeatureApiData::class.java)
            features[doc.id] = Feature(feature.title, feature.description, feature.image, feature.goal, feature.count)
        }.also { onChange(ArrayList<Feature>(features.values)) }
    }

    override fun unsubscribe() {
        this.onChange = {}
        this.listener.remove()
    }

    override fun subscribe(onChange: (List<Feature>) -> Unit): List<Feature> {
        this.onChange = onChange
        return ArrayList<Feature>(features.values)
    }
}