package nl.entreco.dartsscorecard.play.input

/**
 * Created by Entreco on 09/12/2017.
 */
class HintKeyProvider(singles: Boolean) {

    val hint1: String = if (singles) "10" else "21"
    val hint2: String = if (singles) "12" else "22"
    val hint3: String = if (singles) "14" else "25"
    val hint4: String = if (singles) "15" else "26"
    val hint5: String = if (singles) "16" else "30"
    val hint6: String = if (singles) "17" else "41"
    val hint7: String = if (singles) "18" else "45"
    val hint8: String = if (singles) "19" else "60"
    val hint9: String = if (singles) "20" else "100"
    val hint0: String = if (singles) "25" else "140"

    fun getHintForKey(hint: Int): String {
        return when(hint){
            -1 -> "0" // Bust
            0 -> hint0
            1 -> hint1
            2 -> hint2
            3 -> hint3
            4 -> hint4
            5 -> hint5
            6 -> hint6
            7 -> hint7
            8 -> hint8
            9 -> hint9
            else -> ""
        }
    }
}