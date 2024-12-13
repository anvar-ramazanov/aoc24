fun main() {
    data class Arcade (val ax: Long, val ay: Long, val bx: Long, val by: Long, val prizeX: Long, val prizeY: Long)

    fun parseInput(input: List<String>): List<Arcade> {
        val arcadeList = mutableListOf<Arcade>()
        for (i in input.indices step 4) {
            val aLine = input[i]
            var parts = aLine.split(": ", ", ")
            val ax = parts[1].removePrefix("X+").toLong()
            val ay = parts[2].removePrefix("Y+").toLong()
            val bLine = input[i + 1]
            parts = bLine.split(": ", ", ")
            val bx = parts[1].removePrefix("X+").toLong()
            val by = parts[2].removePrefix("Y+").toLong()
            val prizeLine = input[i + 2]
            parts = prizeLine.split(": ", ", ")
            val prizeX = parts[1].removePrefix("X=").toLong()
            val prizeY = parts[2].removePrefix("Y=").toLong()
            arcadeList.add(Arcade(ax, ay, bx, by, prizeX, prizeY))
        }
        return arcadeList
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val arcadeList = parseInput(input)
        for (arcade in arcadeList) {
            val prices = mutableListOf<Int>()
            for (i in 0 until 100) {
                for (j in 0 until 100) {
                    if (arcade.ax * i  + arcade.bx * j  == arcade.prizeX &&
                        arcade.ay * i + arcade.by * j == arcade.prizeY) {
                        prices.add((i * 3) + j)
                    }
                }
            }
            result += if (prices.isNotEmpty()) prices.min() else 0
        }
        return result
    }

    fun solveSystemKramer(
        a1: Long, b1: Long, c1: Long,
        a2: Long, b2: Long, c2: Long
    ): Pair<Long?, Long?> {
        val d = a1 * b2 - a2 * b1

        if (d == 0L) {
            return null to null
        }

        val dx = c1 * b2 - c2 * b1
        val dy = a1 * c2 - a2 * c1

        if (dx % d != 0L || dy % d != 0L) {
            return null to null
        }

        val x = dx / d
        val y = dy / d

        if (x < 0 || y < 0) {
            return null to null
        }

        return x to y
    }

    fun part2(input: List<String>): Long {
        var result = 0L
        val arcadeList = parseInput(input)

        for (arcade in arcadeList) {
            val s = solveSystemKramer(arcade.ax, arcade.bx, arcade.prizeX + 10000000000000, arcade.ay, arcade.by, arcade.prizeY + 10000000000000)
            if (s.first != null && s.second != null) {
                result += (s.first!! * 3) + s.second!!
            }
        }
        return result
    }
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480)
    //check(part2(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
