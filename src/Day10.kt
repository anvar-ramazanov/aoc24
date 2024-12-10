fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        var result = mutableListOf<List<Int>>()
        for (line in input) {
            var row = mutableListOf<Int>()
            for (i in 0 until line.length) {
                row.add(line[i].digitToInt())
            }
            result.add(row)
        }
        return result
    }

    fun getNeighbours(i: Int, j: Int, map: List<List<Int>>): List<Pair<Int, Int>> {
        val current = map[i][j]
        val result = mutableListOf<Pair<Int, Int>>()
        if (i > 0 && map[i-1][j] - current == 1) result.add(Pair(i - 1, j))
        if (i < map.size - 1 && map[i + 1][j] - current == 1)  result.add(Pair(i + 1, j))
        if (j > 0 && map[i][j - 1] - current == 1 ) result.add(Pair(i, j - 1))
        if (j < map[i].size - 1 && map[i][j + 1] - current == 1) result.add(Pair(i, j + 1))
        return result
    }

    fun getReachedDestinations(i: Int, j: Int, lists: List<List<Int>>, reached: MutableSet<Pair<Int, Int>>) {
        val current = lists[i][j]
        if (current == 9) {
            reached.add(Pair(i, j))
            return
        }
        val neighbours = getNeighbours(i, j, lists)
        for (n in neighbours) {
            getReachedDestinations(n.first, n.second, lists, reached)
        }
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val map = parseInput(input)
        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                if (map[i][j] == 0) {
                    val reached = mutableSetOf<Pair<Int, Int>>()
                    getReachedDestinations(i, j, map, reached)
                    result += reached.size
                }
            }
        }
        return result
    }

    fun getNumberOfTrails(i: Int, j: Int, lists: List<List<Int>>) : Int {
        val current = lists[i][j]
        if (current == 9) {
            return 1
        }
        val neighbours = getNeighbours(i, j, lists)
        var result = 0
        for (n in neighbours) {
            result  += getNumberOfTrails(n.first, n.second, lists)
        }
        return result
    }


    fun part2(input: List<String>): Int {
        var result = 0
        val map = parseInput(input)
        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                if (map[i][j] == 0) {
                    result += getNumberOfTrails(i, j, map)
                }
            }
        }
        return result
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
