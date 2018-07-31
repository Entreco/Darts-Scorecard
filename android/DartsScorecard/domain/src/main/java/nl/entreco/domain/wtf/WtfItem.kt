package nl.entreco.domain.wtf

data class WtfItem(
        val docId: String,
        val title: String,
        val description: String,
        val image: String?,
        val video: String?,
        val viewed: Long
)