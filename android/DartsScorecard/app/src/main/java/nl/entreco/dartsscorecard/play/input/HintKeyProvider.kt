package nl.entreco.dartsscorecard.play.input

/**
 * Created by Entreco on 09/12/2017.
 */
class HintKeyProvider(singles: Boolean) {
    val hint1: String = if (singles) "5" else "26"
    val hint2: String = if (singles) "20" else "40"
    val hint3: String = if (singles) "1" else "21"
    val hint4: String = if (singles) "15" else "45"
    val hint5: String = if (singles) "60" else "60"
    val hint6: String = if (singles) "3" else "41"
    val hint7: String = if (singles) "" else ""
    val hint8: String = if (singles) "" else ""
    val hint9: String = if (singles) "" else ""
    val hint0: String = if (singles) "180" else "180"

    fun getHint(hint: Int): String {
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