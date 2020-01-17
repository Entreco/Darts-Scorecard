package nl.entreco.domain.model.players

open class Bot(val displayName: String, val level: Float, _id: Long = 0) : Player(displayName, _id) {
    override fun toString(): String {
        return displayName
    }

    object Rand : Bot("\uD83E\uDD16 Rand 0.5", 0.5F)
    object Noob : Bot("\uD83E\uDD16 Noob 1", 1F)
    object Easy : Bot("\uD83E\uDD16 Easy 1.5", 1.5F)
    object Midi : Bot("\uD83E\uDD16 Midi 2", 2F)
    object Oki : Bot("\uD83E\uDD16 Oki 3", 3F)
    object Gui : Bot("\uD83E\uDD16 Gui 5", 5F)
    object Pro : Bot("\uD83E\uDD16 Pro 10", 10F)
}