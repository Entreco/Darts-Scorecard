package nl.entreco.domain.repository

interface SignallingRepository {
    fun connect()
    fun cleanDisconnectOrders(done: () -> Unit, fail: (Throwable) -> Unit)
    fun setStreamerOnline(connectCode: String, done: (String?) -> Unit, fail: (Throwable) -> Unit)
    fun setReceiverOnline(connectCode: String, done: (String) -> Unit, fail: (Throwable) -> Unit)
}