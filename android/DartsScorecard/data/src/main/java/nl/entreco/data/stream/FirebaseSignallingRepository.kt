package nl.entreco.data.stream

import com.google.firebase.database.*
import nl.entreco.domain.repository.SignallingRepository
import nl.entreco.shared.log.Logger

class FirebaseSignallingRepository(private val db: FirebaseDatabase,
                                   private val logger: Logger,
                                   private val currentDeviceUuid: String) : SignallingRepository {

    companion object {
        private const val RECEIVER_DEVICES_PATH = "receiving_devices/"
        private const val STREAMER_DEVICES_PATH = "streaming_devices/"
        private const val DISCONNECT_PATH = "should_disconnect/"
    }

    private val onlineReceivers = RECEIVER_DEVICES_PATH.plus(currentDeviceUuid)
    private val onlineStreamers = STREAMER_DEVICES_PATH.plus(currentDeviceUuid)
    private fun deviceDisconnectPath(uuid: String) = DISCONNECT_PATH.plus(uuid)

    init {
        logger.w("UUID: $currentDeviceUuid")
        logger.w("UUID: onlineReceivers $onlineReceivers")
    }


    override fun connect() {
        db.goOnline()
    }

    override fun disconnect() {
        db.goOffline()
    }

    override fun listenForDisconnects(done: () -> Unit) {
        disconnecter = Disconnecter(done)
        disconnecter?.let {
            db.getReference(deviceDisconnectPath(currentDeviceUuid))
                    .addChildEventListener(it)
        }
    }

    override fun stopListenForDisconnects() {
        disconnecter?.let {
            db.getReference(deviceDisconnectPath(currentDeviceUuid))
                    .removeEventListener(it)
        }
        disconnecter = null
    }

    override fun sendDisconnectOrderToOtherParty(uuid: String, done: () -> Unit) {
        val reference = db.getReference(deviceDisconnectPath(uuid))
        reference.setValue(DisconnectFirebaseData(currentDeviceUuid)) { databaseError, _ ->
            if (databaseError != null) {
                logger.e(databaseError.toString())
            } else {
                logger.e("Disconnected Order ")
            }

            done()
        }
    }

    override fun cleanDisconnectOrders(done: () -> Unit, fail: (Throwable) -> Unit) {
        val reference = db.getReference(deviceDisconnectPath(currentDeviceUuid))
        reference.onDisconnect().removeValue()
        reference.removeValue { dbError, _ ->
            if (dbError != null) {
                fail(dbError.toException())
            } else {
                done()
            }
        }
    }

    override fun setReceiverOnline(connectCode: String, done: (String) -> Unit,
                                   fail: (Throwable) -> Unit) {
        val firebaseOnlineReference = db.getReference(onlineReceivers)
        with(firebaseOnlineReference) {
            onDisconnect().removeValue()
            setValue(ReceiverFirebaseData(connectCode, false)) { _, _ ->
                done(connectCode)
            }
        }
    }

    override fun setStreamerOnline(connectCode: String, done: (String?) -> Unit,
                                   fail: (Throwable) -> Unit) {
        val firebaseOnlineReference = db.getReference(onlineStreamers)
        with(firebaseOnlineReference) {
            onDisconnect().removeValue()
            setValue(StreamerFirebaseData(connectCode, true)) { _, _ ->
                findReceiverDevice(connectCode, done, fail)
            }
        }
    }

    private fun findReceiverDevice(connectCode: String, done: (String?) -> Unit,
                                   fail: (Throwable) -> Unit) {
        var receiverUuid: String? = null
        db.getReference(RECEIVER_DEVICES_PATH).runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val genericTypeIndicator = object :
                        GenericTypeIndicator<MutableMap<String, ReceiverFirebaseApiData>>() {}
                val availableDevices = mutableData.getValue(genericTypeIndicator)
                        ?: return Transaction.success(mutableData)
                if (!availableDevices.isEmpty()) {
                    receiverUuid = deleteDeviceFromAvailable(connectCode, availableDevices)
                    mutableData.value = availableDevices
                }

                return Transaction.success(mutableData)
            }

            private fun deleteDeviceFromAvailable(connectCode: String,
                                                  availableDevices: MutableMap<String, ReceiverFirebaseApiData>): String? {
                val remoteDeviceUuid = availableDevices.entries.firstOrNull { it.value.code == connectCode }
                        ?.key
                logger.d("Device number $remoteDeviceUuid was chosen because of code $connectCode.")
                availableDevices.remove(remoteDeviceUuid)
                return remoteDeviceUuid
            }

            override fun onComplete(databaseError: DatabaseError?, completed: Boolean,
                                    p2: DataSnapshot?) {
                if (databaseError != null) {
                    fail(databaseError.toException())
                } else if (completed) {
                    done(receiverUuid)
                }
            }
        })
    }

    private var disconnecter: ChildEventListener? = null

    private class Disconnecter(private val done: () -> Unit) : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            done()
        }

        override fun onChildRemoved(p0: DataSnapshot) {}
    }
}