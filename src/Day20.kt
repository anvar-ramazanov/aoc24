import java.util.PriorityQueue

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

    fun getCheats(position: Pair<Int, Int>, path: Set<Pair<Int, Int>>, maze: Maze) : List<Pair<Int, Int>> {
        var result: MutableList<Pair<Int, Int>> = mutableListOf()
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        for (direction in directions) {
            val next = Pair(position.first + direction.first, position.second + direction.second)
            val next2 = Pair(position.first + 2 * direction.first, position.second + 2 * direction.second)
            if (maze.maze.getValue(next) == '#' && (path.contains(next2) || maze.endPos == next2) ) {
                result.add(next)
            }
        }
        return result
    }

    fun getMazeWithCheat(cheat: Pair<Int, Int>, maze: Maze): Maze {
        val mazeWithCheat =  mutableMapOf<Pair<Int, Int>, Char>()
        for ((i, j) in maze.maze.keys) {
            if (i == cheat.first && j == cheat.second) {
                mazeWithCheat[cheat] = '.'
            } else {
                mazeWithCheat[i to j] = maze.maze.getValue(i to j)
            }
        }
        return Maze(mazeWithCheat, maze.startPos, maze.endPos, maze.maxRow, maze.maxCol)
    }

    fun part1(input: List<String>): Int {

        val maze = parseMaze(input)

        val (shortestPath, path) = getShortestPath(maze.startPos, maze.endPos, maze.maze)

        val cheatBenefits: MutableList<Int> = mutableListOf()
        val checkedCheats: MutableSet<Pair<Int, Int>> = mutableSetOf()
        for (pos in path) {
            val cheats = getCheats(pos, path, maze)
            for (cheat in cheats) {
                if (checkedCheats.contains(cheat)) continue
                checkedCheats.add(cheat)
                val mazeWithCheat = getMazeWithCheat(cheat, maze)
                val (cheatShortestPath, _) = getShortestPath(mazeWithCheat.startPos, mazeWithCheat.endPos, mazeWithCheat.maze)
                if (shortestPath - cheatShortestPath > 0) {
                    cheatBenefits.add(shortestPath - cheatShortestPath)
                }
            }
        }

        return cheatBenefits.count { it >= 100 }
    }

    fun part2(input: List<String>): Long {
       return 0
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 0)
    //check(part2(testInput) == 16L)


    val input = readInput("Day20")
    part1(input).println()
    //part2(input).println()
}
