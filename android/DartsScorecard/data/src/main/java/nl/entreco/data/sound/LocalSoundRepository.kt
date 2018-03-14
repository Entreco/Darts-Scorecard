package nl.entreco.data.sound

import android.content.Context
import android.media.SoundPool
import nl.entreco.domain.play.mastercaller.Sound
import nl.entreco.domain.repository.AudioPrefRepository
import nl.entreco.domain.repository.SoundRepository
import java.util.*

/**
 * Created by entreco on 14/03/2018.
 */
class LocalSoundRepository(private val context: Context,
                           private val soundPool: SoundPool,
                           private val prefs: AudioPrefRepository,
                           private val mapper: SoundMapper) : SoundRepository {

    private val priorityNormal = 1
    internal val queue: Deque<Int> = ArrayDeque<Int>()
    internal val sounds: HashMap<Sound, Int> = hashMapOf()
    private val ready: HashMap<Sound, Int> = hashMapOf()

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            when (status) {
                0 -> storeSound(sampleId)
            }
        }
    }

    override fun play(sound: Sound) {
        if(!prefs.isMasterCallerEnabled()) return

        if (ready.containsKey(sound)) {
            ready[sound]?.let { soundID ->
                playSoundWithId(soundID)
            }
        } else {
            val res = mapper.toRaw(sound)
            queueSoundWithId(soundPool.load(context, res, priorityNormal), sound)
        }
    }

    override fun release() {
        try {
            soundPool.setOnLoadCompleteListener(null)
            soundPool.release()
        } catch (ignore: Throwable) { }
    }

    internal fun storeSound(soundId: Int) {
        val sound: Sound = sounds.entries.find { it.value == soundId }!!.key
        ready[sound] = soundId
        playLastSound()
    }

    private fun queueSoundWithId(soundID: Int, sound: Sound) {
        queue.addFirst(soundID)
        sounds[sound] = soundID
    }

    private fun playSoundWithId(soundID: Int) {
        queue.addFirst(soundID)
        soundPool.play(soundID, 1.0F, 1.0F, priorityNormal, 0, 1.0F)
    }

    private fun playLastSound() {
        val soundID = queue.pop()
        queue.clear()
        playSoundWithId(soundID)
    }
}