package nl.entreco.data.api.beta

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import nl.entreco.domain.beta.Feature
import nl.entreco.domain.repository.FeatureRepository

/**
 * Created by entreco on 03/02/2018.
 */
class RemoteFeatureRepository(db: FirebaseDatabase) : FeatureRepository, ChildEventListener {

    private val features: MutableMap<String, Feature> = mutableMapOf()
    private val ref = db.getReference("features")

    init {
        ref.addChildEventListener(this)
    }

    override fun onCancelled(p0: DatabaseError?) {}

    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

    override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
        try {
            val data: FeatureApiData = p0?.getValue(FeatureApiData::class.java)!!
            features[p1!!] = Feature(data.title, data.description, data.image, data.required)
        } catch (oops: Throwable) {
            Log.d("RemoteFeatureRepo", "unable to read data $p0")
        }
    }

    override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
        try {
            val data: FeatureApiData = p0?.getValue(FeatureApiData::class.java)!!
            features[p1!!] = (Feature(data.title, data.description, data.image, data.required))
        } catch (oops: Throwable) {
            Log.d("RemoteFeatureRepo", "unable to read data $p0")
        }
    }

    override fun onChildRemoved(p0: DataSnapshot?) {}

    override fun fetchAll(): List<Feature> {
        return ArrayList<Feature>(features.values)
    }
}