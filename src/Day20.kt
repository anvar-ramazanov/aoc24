import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    data class Maze(
        val maze: Map<Pair<Int, Int>, Char>,
        val startPos: Pair<Int, Int>,
        val endPos: Pair<Int, Int>,
        val maxRow: Int,
        val maxCol: Int
    )

    fun parseMaze(input: List<String>): Maze {
        var startPos = Pair(0, 0)
        var endPos = Pair(0, 0)
        val maze = mutableMapOf<Pair<Int, Int>, Char>()
        for ((i, line) in input.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (c == 'S') {
                    startPos = Pair(i, j)
                }
                if (c == 'E') {
                    endPos = Pair(i, j)
                }
                maze[Pair(i, j)] = c
            }
        }
        return Maze(maze, startPos, endPos, input.size - 1, input[0].length - 1)
    }

    data class Move(val position: Pair<Int, Int>, val cost: Int, val prevPosition: Pair<Int, Int>) : Comparable<Move> {
        override fun compareTo(other: Move): Int = this.cost - other.cost
    }

    fun getPath(
        moves: MutableMap<Pair<Int, Int>, Pair<Int, Int>>,
        start: Pair<Int, Int>,
        end: Pair<Int, Int>
    ): Set<Pair<Int, Int>> {
        val path = mutableListOf<Pair<Int, Int>>()
        path.add(end)
        var current = moves[end]
        while (current != null && current != start) {
            path.add(current)
            current = moves[current]
        }
        path.add(start)
        path.reverse()
        return path.toSet()
    }

    fun getShortestPath(start: Pair<Int, Int>, end: Pair<Int, Int>, maze: Map<Pair<Int, Int>, Char>): Pair<Int, Set<Pair<Int, Int>>> {
        val costMap: MutableMap<Pair<Int, Int>, Int> = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
        val movesMap: MutableMap<Pair<Int, Int>, Pair<Int, Int>> = mutableMapOf()
        val queue = PriorityQueue<Move>()
        queue.add(Move(start, 0, Pair(-1, -1)))

        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

        while (queue.isNotEmpty()) {
            val (currentPosition, currentCost, prevPosition) = queue.poll()

            if (costMap.getValue(currentPosition) <= currentCost) {
                continue
            }

            costMap[currentPosition] = currentCost
            movesMap[currentPosition] = prevPosition

            if (currentPosition == end) break

            for (direction in directions) {
                val next = Pair(currentPosition.first + direction.first, currentPosition.second + direction.second)
                if (maze.getValue(next) != '#') {
                    if (costMap.getValue(next) > currentCost + 1) {
                        queue.add(Move(next, currentCost + 1, currentPosition))
                    }
                }
            }
        }

        val path = getPath(movesMap, start, end)

        return Pair(costMap.getValue(end), path)
    }

    fun getCheats(position: Pair<Int, Int>, path: Set<Pair<Int, Int>>, maze: Maze) : List<Pair<Pair<Int, Int>, Pair<Int,Int>>> {
        var result: MutableList<Pair<Pair<Int, Int>, Pair<Int,Int>>> = mutableListOf()
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        for (direction in directions) {
            val next = Pair(position.first + direction.first, position.second + direction.second)
            val next2 = Pair(position.first + 2 * direction.first, position.second + 2 * direction.second)
            if (maze.maze.getValue(next) == '#' && path.contains(next2)) {
                result.add(Pair(next, next2))
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val maze = parseMaze(input)

        val (shortestPath, path) = getShortestPath(maze.startPos, maze.endPos, maze.maze)

        val cheatBenefits: MutableList<Int> = mutableListOf()
        val checkedCheats: MutableSet<Pair<Int, Int>> = mutableSetOf()
        for (pos in path) {
            if (pos == maze.endPos) continue
            val cheats = getCheats(pos, path, maze)
            for (cheat in cheats) {
                val (cheatSkip, cheatExit) = cheat
                if (checkedCheats.contains(cheatSkip)) continue
                checkedCheats.add(cheatSkip)

                val cut = path.indexOf(cheatExit) - path.indexOf(pos)
                val pathLengthWithCheat = path.size - cut

                if (pathLengthWithCheat < shortestPath) cheatBenefits.add(shortestPath - pathLengthWithCheat)
            }
        }

        return cheatBenefits.count { it >= 100 }
    }

    fun part2(input: List<String>): Int {
        val maze = parseMaze(input)

        val (shortestPath, path) = getShortestPath(maze.startPos, maze.endPos, maze.maze)

        val cheatBenefits: MutableList<Int> = mutableListOf()
        val checkedCheats: MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> = mutableSetOf()

        for ((ind, currentPos) in path.withIndex()) {
            val cheats = mutableSetOf<Pair<Int, Int>>()
            for (i in -20 .. 20) {
                for (j in -20 .. 20) {
                    val exitPoint = Pair(currentPos.first + i, currentPos.second + j)
                    if (path.contains(exitPoint) && exitPoint != currentPos && abs(i) + abs(j) <= 20) cheats.add(exitPoint)
                }
            }

            for (cheat in cheats) {
                if (checkedCheats.contains(Pair(currentPos, cheat))) continue
                checkedCheats.add(Pair(currentPos, cheat))

                val cut = path.indexOf(cheat) - ind
                val add = abs(cheat.first - currentPos.first) + abs(cheat.second - currentPos.second)
                val pathLengthWithCheat = (path.size - 1 - cut) + add

                if (pathLengthWithCheat < shortestPath) {
                    cheatBenefits.add(shortestPath - pathLengthWithCheat)
                }
            }
        }

        //val a = cheatBenefits.filter { it >= 50}.groupBy { it }.mapValues { it.value.size }.toSortedMap()

        return cheatBenefits.count { it >= 100 }
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)


    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
