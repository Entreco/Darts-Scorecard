package nl.entreco.dartsscorecard.dynamic

interface SoundInstalledCallback {
    fun onComplete()
    fun onProgress(bytes: Long, totalBytes: Long)
    fun onError()
}