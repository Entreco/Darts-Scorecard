package nl.entreco.domain.model.players

open class Bot(val displayName: String, val level: Float, _id: Long = 0) : Player(displayName, _id) {
    override fun toString(): String {
        return displayName
    }

    object Beginner : Bot("\uD83E\uDD16 Beginner", 0.4F)
    object Medium : Bot("\uD83E\uDD16 Medium", 1F)
    object Hard : Bot("\uD83E\uDD16 Hard", 2F)
    object Pro : Bot("\uD83E\uDD16 Pro", 3F)
}