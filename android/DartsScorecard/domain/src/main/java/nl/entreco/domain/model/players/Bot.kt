package nl.entreco.domain.model.players

open class Bot(val displayName: String,  _id: Long = 0) : Player(displayName, _id) {
    override fun toString(): String {
        return displayName
    }

    object Easy : Bot("\uD83E\uDD16 Easy")
    object Pro : Bot("\uD83E\uDD16 Pro")
}