package nl.entreco.data.wtf

import com.google.firebase.firestore.*
import nl.entreco.domain.repository.WtfRepository
import nl.entreco.domain.wtf.WtfItem
import nl.entreco.liblog.Logger

class RemoteWtfRepository(private val db: FirebaseFirestore, private val logger: Logger) : WtfRepository, EventListener<QuerySnapshot> {

    private val faqRef = db.collection("faq")
    private val listener = faqRef.addSnapshotListener(this)
    internal var onChange: (List<WtfItem>) -> Unit = {}
    private val wtfItems = mutableMapOf<String, WtfItem>()

    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
        if (p1 != null) {
            logger.w("Unable to read /faq's from Firestore")
            return
        }

        wtfItems.clear()

        p0?.documents?.forEach { doc ->
            convertToFaqItem(doc)
        }.also { onChange(ArrayList(wtfItems.values)) }
    }

    private fun convertToFaqItem(doc: DocumentSnapshot) {
        try {
            val wtf = doc.toObject(FaqApiData::class.java)!!
            wtfItems[doc.id] = WtfItem(doc.id, wtf.title, wtf.desc, wtf.image, wtf.video, wtf.viewed)
        } catch (oops: Exception) {
            logger.e("Unable to convert snapshot to Faq( $doc ) $oops")
        }
    }

    override fun viewedItem(docId: String) {
        wtfItems[docId]?.also {
            // Update on Remote
            val faqItem = faqRef.document(docId)
            db.runTransaction { transaction ->
                transaction.update(faqItem, "viewed", it.viewed + 1)
            }
        }
    }

    override fun subscribe(onChange: (List<WtfItem>) -> Unit): List<WtfItem> {
        this.onChange = onChange
        return ArrayList(wtfItems.values)
    }

    override fun unsubscribe() {
        this.onChange = {}
        this.listener.remove()
    }
}