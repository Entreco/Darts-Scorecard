package nl.entreco.domain.play.model

/**
 * Created by Entreco on 24/11/2017.
 */
enum class Dart(private val description: String, private val value: Int) {
    ZERO("", 0),
    SINGLE_1("1", 1),
    SINGLE_2("2", 2),
    SINGLE_3("3", 3),
    SINGLE_4("4", 4),
    SINGLE_5("5", 5),
    SINGLE_6("6", 6),
    SINGLE_7("7", 7),
    SINGLE_8("8", 8),
    SINGLE_9("9", 9),
    SINGLE_10("10", 10),
    SINGLE_11("11", 11),
    SINGLE_12("12", 12),
    SINGLE_13("13", 13),
    SINGLE_14("14", 14),
    SINGLE_15("15", 15),
    SINGLE_16("16", 16),
    SINGLE_17("17", 17),
    SINGLE_18("18", 18),
    SINGLE_19("19", 19),
    SINGLE_20("20", 20),
    DOUBLE_1("D1", 2 * 1),
    DOUBLE_2("D2", 2 * 2),
    DOUBLE_3("D3", 2 * 3),
    DOUBLE_4("D4", 2 * 4),
    DOUBLE_5("D5", 2 * 5),
    DOUBLE_6("D6", 2 * 6),
    DOUBLE_7("D7", 2 * 7),
    DOUBLE_8("D8", 2 * 8),
    DOUBLE_9("D9", 2 * 9),
    DOUBLE_10("D10", 2 * 10),
    DOUBLE_11("D11", 2 * 11),
    DOUBLE_12("D12", 2 * 12),
    DOUBLE_13("D13", 2 * 13),
    DOUBLE_14("D14", 2 * 14),
    DOUBLE_15("D15", 2 * 15),
    DOUBLE_16("D16", 2 * 16),
    DOUBLE_17("D17", 2 * 17),
    DOUBLE_18("D18", 2 * 18),
    DOUBLE_19("D19", 2 * 19),
    DOUBLE_20("D20", 2 * 20),
    TRIPLE_1("T1", 3 * 1),
    TRIPLE_2("T2", 3 * 2),
    TRIPLE_3("T3", 3 * 3),
    TRIPLE_4("T4", 3 * 4),
    TRIPLE_5("T5", 3 * 5),
    TRIPLE_6("T6", 3 * 6),
    TRIPLE_7("T7", 3 * 7),
    TRIPLE_8("T8", 3 * 8),
    TRIPLE_9("T9", 3 * 9),
    TRIPLE_10("T10", 3 * 10),
    TRIPLE_11("T11", 3 * 11),
    TRIPLE_12("T12", 3 * 12),
    TRIPLE_13("T13", 3 * 13),
    TRIPLE_14("T14", 3 * 14),
    TRIPLE_15("T15", 3 * 15),
    TRIPLE_16("T16", 3 * 16),
    TRIPLE_17("T17", 3 * 17),
    TRIPLE_18("T18", 3 * 18),
    TRIPLE_19("T19", 3 * 19),
    TRIPLE_20("T20", 3 * 20),
    SINGLE_BULL("S.BULL", 25),
    BULL("BULL", 50),

    // Excluded from Dart.random()
    NONE("", 0),
    TEST_501("test_501", 501),
    TEST_D250("Dtest_250", 2 * 250);

    fun value(): Int {
        return value
    }

    fun desc(): String {
        return description
    }

    fun next(): Dart {
        return when {
            this == Dart.NONE -> Dart.DOUBLE_1
            this == Dart.DOUBLE_1 -> Dart.DOUBLE_2
            this == Dart.DOUBLE_2 -> Dart.DOUBLE_3
            else -> Dart.NONE
        }
    }

    fun isDouble(): Boolean {
        return description.startsWith("D")
    }
}