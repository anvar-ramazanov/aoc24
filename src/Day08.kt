fun main() {
    fun parseMap(input: List<String>) : List<List<Char>> {
        val map = mutableListOf<List<Char>>()
        for (i in input.indices) {
            var line = mutableListOf<Char>()
            for (j in input[i].indices) {
                line.add(input[i][j])
            }
            map.add(line)
        }
        return map
    }

    fun getSecondPoints(x: Int, y: Int, map: List<List<Char>>): List<Pair<Int, Int>> {
        val c = map[x][y]
        val pairs = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                if (i == x && j == y) continue
                if (map[i][j] == c) pairs.add(Pair(i, j))
            }
        }
        return pairs
    }

    fun addNewPairIfValid(pair: Pair<Int, Int>, mapRows: Int, mapCols: Int, antiNodes: MutableSet<Pair<Int, Int>>) {
        val (x, y) = pair
        if (x in 0 until mapRows && y in 0 until mapCols) {
            antiNodes.add(pair)
        }
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)
        val antiNodes = mutableSetOf<Pair<Int, Int>>()

        val mapRows = map.size
        val mapCols = if (map.isNotEmpty()) map[0].size else 0

        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                if (map[i][j] != '.') {
                    val secondPoints = getSecondPoints(i, j, map)
                    for (secondPoint in secondPoints) {
                        val dx = secondPoint.first - i
                        val dy = secondPoint.second - j
                        addNewPairIfValid(Pair(i - dx, j - dy), mapRows, mapCols, antiNodes)
                        addNewPairIfValid(Pair(secondPoint.first + dx, secondPoint.second + dy), mapRows, mapCols, antiNodes)
                    }
                }
            }
        }

        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)
        val antiNodes = mutableSetOf<Pair<Int, Int>>()

        val mapRows = map.size
        val mapCols = if (map.isNotEmpty()) map[0].size else 0

        fun addPointsInDirection(x: Int, y: Int, dx: Int, dy: Int, vertical: Boolean) {
            var currentX = x
            var currentY = y
            while (currentX >= 0 && currentX < mapRows && currentY < mapCols && currentY >= 0) {
                if (vertical) {
                    currentX += dx
                    currentY += dy
                } else {
                    currentX -= dx
                    currentY -= dy
                }
                addNewPairIfValid(Pair(currentX, currentY), mapRows, mapCols, antiNodes)
            }
        }

        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                if (map[i][j] != '.') {
                    val secondPoints = getSecondPoints(i, j, map)
                    for (secondPoint in secondPoints) {
                        val dx = secondPoint.first - i
                        val dy = secondPoint.second - j
                        addPointsInDirection(i, j, dx, dy, true)
                        addPointsInDirection(i, j, dx, dy, false)
                    }
                }
            }
        }

        return antiNodes.size
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)


    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
