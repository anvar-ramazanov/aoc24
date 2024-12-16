fun main() {
    data class State (val maze:  Map<Pair<Int, Int>, Char>, val startPos: Pair<Int, Int>, val endPos: Pair<Int, Int>, val maxRow: Int, val maxCol: Int )

    fun parseMaze(input: List<String>): State {
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
        return State(maze, startPos, endPos, input.size - 1, input[0].length - 1)
    }

    fun clockwise(direction: Pair<Int, Int>): Pair<Int, Int> {
        when (direction) {
            Pair(0, 1) -> return Pair(1, 0)
            Pair(1, 0) -> return Pair(0, -1)
            Pair(0, -1) -> return Pair(-1, 0)
            Pair(-1, 0) -> return Pair(0, 1)
        }
        return direction
    }

    fun counterClockwise(direction: Pair<Int, Int>): Pair<Int, Int> {
        when (direction) {
            Pair(0, 1) -> return Pair(-1, 0)
            Pair(-1, 0) -> return Pair(0, -1)
            Pair(0, -1) -> return Pair(1, 0)
            Pair(1, 0) -> return Pair(0, 1)
        }
        return direction
    }

    data class Move(val position: Pair<Int, Int>, val direction: Pair<Int, Int>, val cost: Int)

    fun calcMinCostsMap(state: State, costMap: MutableMap<Pair<Int, Int>, Int>) {
        val queue = ArrayDeque<Move>()
        queue.add(Move(state.startPos, Pair(0, 1), 0))

        while (queue.isNotEmpty()) {
            val (currentPosition, currentDirection, currentCost) = queue.removeFirst()

            if (costMap.getValue(currentPosition) < currentCost){
                continue
            }

            costMap[currentPosition] = currentCost

            if (currentPosition == state.endPos) continue

            // forward
            val forwardPosition = Pair(currentPosition.first + currentDirection.first, currentPosition.second + currentDirection.second)
            if (state.maze[forwardPosition] != '#') {
                queue.add(Move(forwardPosition, currentDirection, currentCost + 1))
            }

            // clockwise
            val clockwiseDirection = clockwise(currentDirection)
            val clockwisePosition = Pair(currentPosition.first + clockwiseDirection.first, currentPosition.second + clockwiseDirection.second)
            if (state.maze[clockwisePosition] != '#') {
                queue.add(Move(clockwisePosition, clockwiseDirection, currentCost + 1001))
            }

            // counterclockwise
            val counterClockwiseDirection = counterClockwise(currentDirection)
            val counterClockwisePosition = Pair(currentPosition.first + counterClockwiseDirection.first, currentPosition.second + counterClockwiseDirection.second)
            if (state.maze[counterClockwisePosition] != '#') {
                queue.add(Move(counterClockwisePosition, counterClockwiseDirection, currentCost + 1001))
            }
        }
    }

    fun part1(input: List<String>): Int {
        val state = parseMaze(input)

        val costMap = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }

        calcMinCostsMap(state, costMap)

        val result = costMap.getValue(state.endPos)

        return result
    }

    fun calcPaths(state: State, minCost: Int) : Int {
        val start = state.startPos
        val end = state.endPos
        val paths = mutableListOf<List<Pair<Int, Int>>>()

        fun dfs(currentPosition: Pair<Int, Int>, currentDirection: Pair<Int, Int>, currentCost: Int, currentPath: MutableSet<Pair<Int, Int>>, paths: MutableList<List<Pair<Int, Int>>>) {
            if (currentPosition == end) {
                paths.add(currentPath.toList())
                return
            }

            if (currentCost >= minCost) return

            if (currentPath.contains(currentPosition)) return

            currentPath.add(currentPosition)

            // forward
            val forwardPosition = Pair(currentPosition.first + currentDirection.first, currentPosition.second + currentDirection.second)
            if (state.maze[forwardPosition] != '#') {
                dfs(forwardPosition, currentDirection, currentCost + 1, currentPath.toMutableSet(), paths)
            }

            // clockwise
            val clockwiseDirection = clockwise(currentDirection)
            val clockwisePosition = Pair(currentPosition.first + clockwiseDirection.first, currentPosition.second + clockwiseDirection.second)
            if (state.maze[clockwisePosition] != '#') {
                dfs(clockwisePosition, clockwiseDirection, currentCost + 1001, currentPath.toMutableSet(), paths)
            }

            // counterclockwise
            val counterClockwiseDirection = counterClockwise(currentDirection)
            val counterClockwisePosition = Pair(currentPosition.first + counterClockwiseDirection.first, currentPosition.second + counterClockwiseDirection.second)
            if (state.maze[counterClockwisePosition] != '#') {
                dfs(counterClockwisePosition, counterClockwiseDirection, currentCost + 1001, currentPath.toMutableSet(), paths)
            }
        }

        dfs(start, Pair(0, 1), 0, mutableSetOf(), paths)

        val uniqueTails = mutableSetOf<Pair<Int, Int>>()
        for (path in paths) {
            for (p in path) {
                uniqueTails.add(p)
            }
        }

        return uniqueTails.size + 1
    }


    fun part2(input: List<String>): Int {
        val state = parseMaze(input)

        val costMap = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }

        calcMinCostsMap(state, costMap)

        val minCost = costMap.getValue(state.endPos)

        return 0
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 11048)
    check(part2(testInput) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
