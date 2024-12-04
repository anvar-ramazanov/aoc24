fun main() {

    fun isValid(p: Pair<Int, Int>, input: List<String>): Boolean {
        return p.first >= 0 && p.first < input.size && p.second >= 0 && p.second < input[0].length
    }

    fun getXmasCount(i: Int, j: Int, input: List<String>, directions: List<Pair<Int, Int>>): Int {
        if (input[i][j] != 'X') return 0

        var result = 0

        for (direction in directions) {
            val p1 = Pair(i + direction.first, j + direction.second)
            val p2 = Pair(i + direction.first * 2, j + direction.second * 2)
            val p3 = Pair(i + direction.first * 3, j + direction.second * 3)
            if (isValid(p1, input) && isValid(p2, input) && isValid(p3, input)) {
                if (input[p1.first][p1.second] == 'M' &&
                    input[p2.first][p2.second] == 'A' &&
                    input[p3.first][p3.second] == 'S'
                ) {
                    result++
                }
            }
        }

        return result
    }

    fun isValidPair(pointA: Pair<Int, Int>, pointB: Pair<Int, Int>, input: List<String>): Boolean {
        return (input[pointA.first][pointA.second] == 'M' && input[pointB.first][pointB.second] == 'S') ||
                (input[pointA.first][pointA.second] == 'S' && input[pointB.first][pointB.second] == 'M')
    }

    fun getXCount(i: Int, j: Int, input: List<String>): Int {
        if (input[i][j] != 'A') return 0

        var result = 0

        var p1 = Pair(i - 1, j - 1)
        var p2 = Pair(i - 1, j + 1)
        var p3 = Pair(i + 1, j - 1)
        var p4 = Pair(i + 1, j + 1)

        if (isValid(p1, input) && isValid(p2, input) && isValid(p3, input) && isValid(p4, input)) {
            if (isValidPair(p1, p4, input) && isValidPair(p2, p3, input)) {
                result++
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val directions = listOf(
            Pair(0, 1),
            Pair(0, -1),
            Pair(1, 0),
            Pair(-1, 0),
            Pair(1, 1),
            Pair(1, -1),
            Pair(-1, 1),
            Pair(-1, -1)
        )
        for (i in input.indices) {
           for (j in input[i].indices) {
               result += getXmasCount(i, j, input, directions)
           }
       }
        return result
    }

    fun part2(input: List<String>): Int {
        var result = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                result += getXCount(i, j, input)
            }
        }
        return result
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)


    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
