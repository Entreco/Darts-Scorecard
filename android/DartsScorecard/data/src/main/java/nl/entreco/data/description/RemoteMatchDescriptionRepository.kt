package nl.entreco.data.description

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import nl.entreco.domain.play.description.MatchDescription
import nl.entreco.domain.repository.MatchDescriptionRepository

class RemoteMatchDescriptionRepository(
        private val remoteConfig: FirebaseRemoteConfig
) : MatchDescriptionRepository {

    init {
        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activate()
            }
        }
    }

    override fun fetchIt(): MatchDescription {
        val description = remoteConfig.getString("match_description")
        return MatchDescription(description)
    }
}