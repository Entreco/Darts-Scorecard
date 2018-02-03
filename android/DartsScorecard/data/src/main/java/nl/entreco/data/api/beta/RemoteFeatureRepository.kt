package nl.entreco.data.api.beta

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
class RemoteFeatureRepository(db: FirebaseDatabase) : FeatureRepository, ValueEventListener {

    private val ref = db.getReference("features")
    private val features = mutableMapOf<String, Feature>()
    private var onChange : (List<Feature>) -> Unit = {}

    init {
        ref.addListenerForSingleValueEvent(this)
    }

    override fun onDataChange(p0: DataSnapshot?) {
        p0?.children?.forEach {
            val key: String = it.key
            val data: FeatureApiData = it.getValue(FeatureApiData::class.java)!!
            val feature = Feature(data.title, data.description, data.img, data.votes, data.required)
            features[key] = feature
        }
        onChange(ArrayList<Feature>(features.values))
    }

    override fun onCancelled(p0: DatabaseError?) {}

    override fun fetchAll(onChange: (List<Feature>)->Unit): List<Feature> {
        this.onChange = onChange
        return ArrayList<Feature>(features.values)
    }
}