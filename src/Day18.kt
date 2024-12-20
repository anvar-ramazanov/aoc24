import java.util.PriorityQueue

fun main() {
    fun parseToPairSet(input: List<String>): Set<Pair<Int, Int>> {
        return input.map { line ->
            val (first, second) = line.split(",").map { it.trim().toInt() }
            Pair(second, first)
        }.toSet()
    }

    data class Move(val position: Pair<Int, Int>, val cost: Int) : Comparable<Move> {
        override fun compareTo(other: Move): Int = this.cost - other.cost
    }

    fun getShortestPath(start: Pair<Int, Int>, end: Pair<Int, Int>, occupied: Set<Pair<Int, Int>>): Int {
        val costMap: MutableMap<Pair<Int, Int>, Int> = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
        val queue = PriorityQueue<Move>()
        queue.add(Move(start, 0))

        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

        while (queue.isNotEmpty()) {
            val (currentPosition, currentCost) = queue.poll()

            if (costMap.getValue(currentPosition) <= currentCost) {
                continue
            }

            costMap[currentPosition] = currentCost

            if (currentPosition == end) break

            for (direction in directions) {
                val next = Pair(currentPosition.first + direction.first, currentPosition.second + direction.second)
                if (next.first >= 0 && next.first <= end.first && next.second >= 0 && next.second <= end.second && !occupied.contains(next)) {
                    if (costMap.getValue(next) > currentCost + 1) {
                        queue.add(Move(next, currentCost + 1))
                    }
                }
            }
        }

        return costMap.getValue(end)
    }

    fun part1(input: List<String>, max: Int, count: Int): Int {
        val coords = parseToPairSet(input).take(count).toSet()

        val start = Pair(0, 0)
        val end = Pair(max, max)

        var result = getShortestPath(start, end, coords)

        return result
    }

    fun existsPath(start: Pair<Int, Int>, end: Pair<Int, Int>, occupied: Set<Pair<Int, Int>>): Boolean {
        val queue = PriorityQueue<Move>()
        queue.add(Move(start, 0))

        val visited = mutableSetOf<Pair<Int, Int>>()

        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

        while (queue.isNotEmpty()) {
            val (currentPosition, currentCost) = queue.poll()

            if (visited.contains(currentPosition)) {
                continue
            }
            visited.add(currentPosition)

            if (currentPosition == end) {
                return true
            }

            for (direction in directions) {
                val next = Pair(currentPosition.first + direction.first, currentPosition.second + direction.second)

                if (next.first >= 0 && next.first <= end.first && next.second >= 0 && next.second <= end.second && !occupied.contains(next)) {
                    queue.add(Move(next, currentCost + 1))
                }
            }
        }

        return false
    }

    fun part2(input: List<String>, max: Int): Pair<Int, Int> {
        val coords = parseToPairSet(input).toSet()

        val start = Pair(0, 0)
        val end = Pair(max, max)

        var result: Pair<Int, Int> = Pair(-1, -1)
        for ((i, c) in coords.withIndex()) {
            val coordsToCheck = coords.take(i + 1).toSet()
            val isPathExists = existsPath(start, end, coordsToCheck)
            if (!isPathExists) {
                result = c
                break
            }
        }

        return result
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput, 6, 12) == 22)
    check(part2(testInput, 6) == Pair(1, 6))


    val input = readInput("Day18")
    part1(input, 70, 1024).println()
    part2(input, 70).println()
}
