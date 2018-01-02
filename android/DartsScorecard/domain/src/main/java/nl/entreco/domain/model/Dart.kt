package nl.entreco.domain.model

/**
 * Created by Entreco on 24/11/2017.
 */
enum class Dart(private val description: String, private val number: Int, private val multiplier: Int) {
    ZERO("0", 0, 1),
    SINGLE_1("1", 1, 1),
    SINGLE_2("2", 2, 1),
    SINGLE_3("3", 3, 1),
    SINGLE_4("4", 4, 1),
    SINGLE_5("5", 5, 1),
    SINGLE_6("6", 6, 1),
    SINGLE_7("7", 7, 1),
    SINGLE_8("8", 8, 1),
    SINGLE_9("9", 9, 1),
    SINGLE_10("10", 10, 1),
    SINGLE_11("11", 11, 1),
    SINGLE_12("12", 12, 1),
    SINGLE_13("13", 13, 1),
    SINGLE_14("14", 14, 1),
    SINGLE_15("15", 15, 1),
    SINGLE_16("16", 16, 1),
    SINGLE_17("17", 17, 1),
    SINGLE_18("18", 18, 1),
    SINGLE_19("19", 19, 1),
    SINGLE_20("20", 20, 1),
    DOUBLE_1("D1", 1, 2),
    DOUBLE_2("D2", 2, 2),
    DOUBLE_3("D3", 3, 2),
    DOUBLE_4("D4", 4, 2),
    DOUBLE_5("D5", 5, 2),
    DOUBLE_6("D6", 6, 2),
    DOUBLE_7("D7", 7, 2),
    DOUBLE_8("D8", 8, 2),
    DOUBLE_9("D9", 9, 2),
    DOUBLE_10("D10", 10, 2),
    DOUBLE_11("D11", 11, 2),
    DOUBLE_12("D12", 12, 2),
    DOUBLE_13("D13", 13, 2),
    DOUBLE_14("D14", 14, 2),
    DOUBLE_15("D15", 15, 2),
    DOUBLE_16("D16", 16, 2),
    DOUBLE_17("D17", 17, 2),
    DOUBLE_18("D18", 18, 2),
    DOUBLE_19("D19", 19, 2),
    DOUBLE_20("D20", 20, 2),
    TRIPLE_1("T1", 1, 3),
    TRIPLE_2("T2", 2, 3),
    TRIPLE_3("T3", 3, 3),
    TRIPLE_4("T4", 4, 3),
    TRIPLE_5("T5", 5, 3),
    TRIPLE_6("T6", 6, 3),
    TRIPLE_7("T7", 7, 3),
    TRIPLE_8("T8", 8, 3),
    TRIPLE_9("T9", 9, 3),
    TRIPLE_10("T10", 10, 3),
    TRIPLE_11("T11", 11, 3),
    TRIPLE_12("T12", 12, 3),
    TRIPLE_13("T13", 13, 3),
    TRIPLE_14("T14", 14, 3),
    TRIPLE_15("T15", 15, 3),
    TRIPLE_16("T16", 16, 3),
    TRIPLE_17("T17", 17, 3),
    TRIPLE_18("T18", 18, 3),
    TRIPLE_19("T19", 19, 3),
    TRIPLE_20("T20", 20, 3),
    SINGLE_BULL("S.BULL", 25, 1),
    BULL("BULL", 25, 2),

    // Excluded from Dart.random()
    NONE("", 0, 0),
    TEST_501("test_501", 501, 1),
    TEST_D250("Dtest_250", 250, 2);

    fun points(): Int {
        return number * multiplier
    }

    fun number(): Int {
        return number
    }

    fun multiplier(): Int {
        return multiplier
    }

    fun desc(): String {
        return description
    }

    fun isDouble(): Boolean {
        return multiplier == 2
    }

    companion object {
        fun fromString(dart: String): Dart {
            Dart.values()
                    .filter { it.desc() == dart }
                    .forEach { return it }
            return Dart.NONE
        }

        fun fromInt(number: Int, multiplier: Int): Dart {
            if (multiplier < 0) throw IllegalStateException("illegal multiplier($multiplier) should be 0,1,2 or 3")
            if (multiplier == 0) return Dart.NONE
            Dart.values()
                    .filter { it.multiplier() == multiplier }
                    .filter { it.number() == number }
                    .forEach { return it }
            return Dart.ZERO
        }
    }
}