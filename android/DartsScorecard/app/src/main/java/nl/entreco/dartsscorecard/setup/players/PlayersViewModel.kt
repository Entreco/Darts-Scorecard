package nl.entreco.dartsscorecard.setup.players

import nl.entreco.dartsscorecard.base.BaseViewModel
import nl.entreco.domain.launch.TeamNamesString
import javax.inject.Inject

/**
 * Created by Entreco on 29/12/2017.
 */
class PlayersViewModel @Inject constructor(val adapter: PlayerAdapter) : BaseViewModel() {

    fun setupTeams(): TeamNamesString {
        return randomTeam()
    }

    private fun randomTeam(): TeamNamesString {
        return when ((Math.random() * 10).toInt()) {
            0 -> TeamNamesString("Remco,Charlie|Eva,Guusje")
            1 -> TeamNamesString("Remco,Eva,Guusje|Boeffie,Beer,Charlie")
            2 -> TeamNamesString("Boeffie|Beer")
            3 -> TeamNamesString("Remco|Eva|Guusje")
            4 -> TeamNamesString("Remco|Eva|Guusje|Boeffie|Beer|Charlie")
            5 -> TeamNamesString("Rob|Geert|Boy")
            6 -> TeamNamesString("Rob,Allison|Geert,Mikka|Boy,Linda")
            7 -> TeamNamesString("Guusje|Eva")
            8 -> TeamNamesString("Guusje,Eva,Beer,Boeffie,Charlie|Co")
            9 -> TeamNamesString("Entreco|Bonske")
            else -> TeamNamesString("Co")
        }
    }
}