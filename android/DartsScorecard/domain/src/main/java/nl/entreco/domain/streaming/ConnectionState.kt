package nl.entreco.domain.streaming

sealed class ConnectionState

object Unknown : ConnectionState()
object Initializing : ConnectionState()
object ReadyToConnect : ConnectionState()
object Connecting : ConnectionState()
object Connected : ConnectionState()
object Disconnecting : ConnectionState()
object Disconnected : ConnectionState()
object Killing : ConnectionState()
