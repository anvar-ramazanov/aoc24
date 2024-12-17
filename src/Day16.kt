fun main() {
    data class State(
        val maze: Map<Pair<Int, Int>, Char>,
        val startPos: Pair<Int, Int>,
        val endPos: Pair<Int, Int>,
        val maxRow: Int,
        val maxCol: Int
    )

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

            if (costMap.getValue(currentPosition) < currentCost) {
                continue
            }

            costMap[currentPosition] = currentCost

            if (currentPosition == state.endPos) continue

            // forward
            val forwardPosition =
                Pair(currentPosition.first + currentDirection.first, currentPosition.second + currentDirection.second)
            if (state.maze[forwardPosition] != '#') {
                queue.add(Move(forwardPosition, currentDirection, currentCost + 1))
            }

            // clockwise
            val clockwiseDirection = clockwise(currentDirection)
            val clockwisePosition = Pair(
                currentPosition.first + clockwiseDirection.first,
                currentPosition.second + clockwiseDirection.second
            )
            if (state.maze[clockwisePosition] != '#') {
                queue.add(Move(clockwisePosition, clockwiseDirection, currentCost + 1001))
            }

            // counterclockwise
            val counterClockwiseDirection = counterClockwise(currentDirection)
            val counterClockwisePosition = Pair(
                currentPosition.first + counterClockwiseDirection.first,
                currentPosition.second + counterClockwiseDirection.second
            )
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

    data class MapKey(val pos: Pair<Int, Int>, val direction: Pair<Int, Int>)

    data class Move2(val position: Pair<Int, Int>, val direction: Pair<Int, Int>, val cost: Int, val prev: MapKey)

    fun calcMinCostsMap2(
        state: State,
        costMap: MutableMap<MapKey, Int>,
        trackMap: MutableMap<MapKey, MutableSet<MapKey>>
    ) {
        val queue = ArrayDeque<Move2>()
        queue.add(Move2(state.startPos, Pair(0, 1), 0, MapKey(state.startPos, Pair(0, 1))))

        while (queue.isNotEmpty()) {
            val (currentPosition, currentDirection, currentCost, prevMove) = queue.removeFirst()

            if (costMap.getValue(MapKey(currentPosition, currentDirection)) < currentCost) {
                continue
            }
            if (costMap.getValue(MapKey(currentPosition, currentDirection)) == currentCost) {
                trackMap.getValue(MapKey(currentPosition, currentDirection)).add(prevMove)
            } else {
                trackMap[MapKey(currentPosition, currentDirection)] = mutableSetOf(prevMove)
            }

            costMap[MapKey(currentPosition, currentDirection)] = currentCost

            if (currentPosition == state.endPos) continue

            // forward
            val forwardPosition =
                Pair(currentPosition.first + currentDirection.first, currentPosition.second + currentDirection.second)
            if (state.maze[forwardPosition] != '#') {
                queue.add(
                    Move2(
                        forwardPosition,
                        currentDirection,
                        currentCost + 1,
                        MapKey(currentPosition, currentDirection)
                    )
                )
            }

            // clockwise
            val clockwiseDirection = clockwise(currentDirection)
            val clockwisePosition = Pair(
                currentPosition.first + clockwiseDirection.first,
                currentPosition.second + clockwiseDirection.second
            )
            if (state.maze[clockwisePosition] != '#') {
                queue.add(
                    Move2(
                        clockwisePosition,
                        clockwiseDirection,
                        currentCost + 1001,
                        MapKey(currentPosition, currentDirection)
                    )
                )
            }

            // counterclockwise
            val counterClockwiseDirection = counterClockwise(currentDirection)
            val counterClockwisePosition = Pair(
                currentPosition.first + counterClockwiseDirection.first,
                currentPosition.second + counterClockwiseDirection.second
            )
            if (state.maze[counterClockwisePosition] != '#') {
                queue.add(
                    Move2(
                        counterClockwisePosition,
                        counterClockwiseDirection,
                        currentCost + 1001,
                        MapKey(currentPosition, currentDirection)
                    )
                )
            }
        }
    }


    fun part2(input: List<String>): Int {
        val state = parseMaze(input)

        val costMap = mutableMapOf<MapKey, Int>().withDefault { Int.MAX_VALUE }
        val trackMap = mutableMapOf<MapKey, MutableSet<MapKey>>()

        calcMinCostsMap2(state, costMap, trackMap)

        val finish = listOf(
            Pair(Pair(0, 1), costMap.getValue(MapKey(state.endPos, Pair(0, 1)))),
            Pair(Pair(0, -1), costMap.getValue(MapKey(state.endPos, Pair(0, -1)))),
            Pair(Pair(1, 0), costMap.getValue(MapKey(state.endPos, Pair(1, 0)))),
            Pair(Pair(-1, 0), costMap.getValue(MapKey(state.endPos, Pair(-1, 0))))
        )

        val minCostDirection = finish.minBy { it.second }.first

        val current = Pair(state.endPos, minCostDirection)
        val visited = mutableSetOf<Pair<Int, Int>>()

        fun dfs(current: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
            val (currentPosition, currentDirection) = current
            visited.add(currentPosition)
            if (currentPosition == state.startPos) return
            val prevPositions = trackMap[MapKey(currentPosition, currentDirection)]!!
            for (prevPosition in prevPositions) {
                dfs(Pair(prevPosition.pos, prevPosition.direction))
            }

        }

        dfs(current)


        return visited.size
    }

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 11048)
    check(part2(testInput) == 64)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
