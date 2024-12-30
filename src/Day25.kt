fun main() {


    fun part1(input: List<String>): Int {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        for (i in 0..input.size step 8) {
            val isLock = input[i].all { it == '#' }
            val pins = mutableListOf<Int>()
            for (k in 0 until 5) {
                var pin = -1
                for (j in 0 until 7) {
                    if (input[i + j][k] == '#') {
                        pin++
                    }
                }
                pins.add(pin)
            }
            if (isLock) {
                locks.add(pins)
            } else {
                keys.add(pins)
            }
        }

        var result = 0
        for (key in keys) {
            for (lock in locks) {
                var isOverlap = false
                for (i in 0 until 5) {
                    if (key[i] + lock[i] > 5) {
                        isOverlap = true
                        break
                    }
                }
                if (!isOverlap) {
                    result++
                }
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == 3)
    //check(part2(testInput) == 8)


    val input = readInput("Day25")
    part1(input).println()
    //part2(input).println()
}
