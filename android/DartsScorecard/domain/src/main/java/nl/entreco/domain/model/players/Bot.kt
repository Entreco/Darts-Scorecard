package nl.entreco.domain.model.players

open class Bot(val displayName: String, val level: Float, _id: Long = 0) : Player(displayName, _id) {
    override fun toString(): String {
        return displayName
    }

    object Beginner : Bot("\uD83E\uDD16 Beginner", 0.7F)
    object Average : Bot("\uD83E\uDD16 Average", 1.3F)
    object Trained : Bot("\uD83E\uDD16 Trained", 1.8F)
    object Pro : Bot("\uD83E\uDD16 Pro", 2.6F)
}