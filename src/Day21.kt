import java.util.LinkedList
import kotlin.math.abs

fun main() {
    data class Point(val point: Pair<Int, Int>, val path: String)

    fun getNumericPosition(code: Char): Pair<Int, Int> {
        return when (code) {
            '7' -> return Pair(0, 0)
            '8' -> return Pair(0, 1)
            '9' -> return Pair(0, 2)
            '4' -> return Pair(1, 0)
            '5' -> return Pair(1, 1)
            '6' -> return Pair(1, 2)
            '1' -> return Pair(2, 0)
            '2' -> return Pair(2, 1)
            '3' -> return Pair(2, 2)
            '0' -> return Pair(3, 1)
            'A' -> return Pair(3, 2)
             else -> {
                 throw Exception("Invalid code")
             }
        }
    }

    fun getDirectionalPosition(code: Char): Pair<Int, Int> {
        return when (code) {
            '^' -> return Pair(0, 1)
            'A' -> return Pair(0, 2)
            '<' -> return Pair(1, 0)
            'v' -> return Pair(1, 1)
            '>' -> return Pair(1, 2)
            else -> {
                throw Exception("Invalid code")
            }
        }
    }

    fun findShortestPath(
        from: Pair<Int, Int>,
        to: Pair<Int, Int>,
        skip: Pair<Int, Int>,
        width: Int,
        height: Int
    ): List<String> {
        val directions = listOf(
            Pair(-1, 0) to '^',
            Pair(1, 0) to 'v',
            Pair(0, -1) to '<',
            Pair(0, 1) to '>'
        )
        val queue = LinkedList<Point>()
        var shortestPaths = mutableListOf<String>()

        queue.add(Point(from, ""))
        var shortestPathLength = abs(from.first - to.first) + abs(from.second - to.second)

        while (queue.isNotEmpty()) {
            val (current, currentPath) = queue.poll()

            if (current.first == to.first && current.second == to.second) {
                if (currentPath.length == shortestPathLength) {
                    shortestPaths.add(currentPath + "A")
                }
                continue
            }

            if (currentPath.length >= shortestPathLength) continue

            for ((direction, symbol) in directions) {
                val newY = current.first + direction.first
                val newX = current.second + direction.second

                if (newX in 0 until width && newY in 0 until height && !(newX == skip.second && newY == skip.first)) {
                    queue.add(Point(Pair(newY, newX), currentPath + symbol))
                }
            }
        }

        return shortestPaths
    }

    val cacheTransformedLength = mutableMapOf<Triple<String, Int, Int>, Long>()

    fun getTransformedLengthOnDepth(path: String, currentDepth: Int, maxDepth: Int): Long {
        val cacheKey = Triple(path, currentDepth, maxDepth)
        cacheTransformedLength[cacheKey]?.let { return it }

        var prev = getDirectionalPosition('A')
        var result = 0L

        for (c in path) {
            val curr = getDirectionalPosition(c)

            val paths = findShortestPath(prev, curr, Pair(0, 0), 3, 2)
            var min = Long.MAX_VALUE

            if (currentDepth < maxDepth) {
                for (path in paths) {
                    val pathLength = getTransformedLengthOnDepth(path, currentDepth + 1, maxDepth)
                    if (pathLength < min) {
                        min = pathLength
                    }
                }
            } else if (currentDepth == maxDepth) {
                min = paths.minOf { it.length }.toLong()
            }
            result += min
            prev = curr
        }

        cacheTransformedLength[cacheKey] = result
        return result
    }

    fun getCodeLength(code: String, maxDepth: Int): Long {
        var prev = getNumericPosition('A')
        var result = 0L

        for (s in code) {
            val curr = getNumericPosition(s)
            val numericPaths = findShortestPath(prev, curr, Pair(3, 0), 3, 4)

            var min = Long.MAX_VALUE
            for (path in numericPaths) {
                val pp = getTransformedLengthOnDepth(path, 1, maxDepth)
                if (pp < min) {
                    min = pp
                }
            }

            result += min
            prev = curr
        }

        return result
    }

    fun getNumberFromCode(code: String): Int {
        val sb = StringBuilder()
        for (c in code) {
            if (c.isDigit()) {
                sb.append(c)
            }
        }
        return sb.toString().toInt()
    }

    fun part1(input: List<String>): Long {
        var result = 0L
        val codes = input.map { it.trim() }.toList()
        for (code in codes) {
            val length = getCodeLength(code, 2)
            result += (length * getNumberFromCode(code))
        }

        return result
    }

    fun part2(input: List<String>): Long {
        var result = 0L
        val codes = input.map { it.trim() }.toList()
        for (code in codes) {
            val length = getCodeLength(code, 25)
            result += (length * getNumberFromCode(code))
        }

        return result
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 126384L)
    //check(part2(testInput) == 0)


    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}
