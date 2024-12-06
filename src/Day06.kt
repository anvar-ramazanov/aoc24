fun main() {

    fun parseMap(input: List<String>): MutableList<List<Char>> {
        val map = mutableListOf<List<Char>>();
        for (line in input) {
            val temp = mutableListOf<Char>()
            for (c in line) {
                temp.add(c)
            }
            map.add(temp)
        }
        return map
    }

    fun getStartPosition(map: List<List<Char>>): Pair<Int, Int> {
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == '^')
                    return Pair(i, j)
            }
        }
        return Pair(-1, -1)
    }

    fun rotate(direction: Pair<Int, Int>): Pair<Int, Int> {
        when (direction) {
            Pair(-1, 0) -> return Pair(0,  1)
            Pair( 0, 1) -> return Pair(1,  0)
            Pair(1,  0) -> return Pair(0, -1)
            Pair(0, -1) -> return Pair(-1, 0)
        }
        return direction
    }

    fun isOutsideOfMap(
        direction: Pair<Int, Int>,
        position: Pair<Int, Int>,
        map: MutableList<List<Char>>
    ): Boolean {
        if (direction == Pair(1, 0) && position.first == map.size - 1) return true
        if (direction == Pair(-1, 0) && position.first == 0) return true
        if (direction == Pair(0, 1) && position.second == map[0].size - 1) return true
        if (direction == Pair(0, -1) && position.second == 0) return true
        return false
    }

    fun getAllVisitedPositions(
        startPosition: Pair<Int, Int>,
        startDirection: Pair<Int, Int>,
        map: MutableList<List<Char>>
    ): MutableSet<Pair<Int, Int>> {
        val positions = mutableSetOf<Pair<Int, Int>>()
        var position = startPosition
        var direction = startDirection
        positions.add(position)

        while (true) {
            if (isOutsideOfMap(direction, position, map)) break
            else if (map[position.first + direction.first][position.second + direction.second] == '#') {
                direction = rotate(direction)
            } else {
                position = Pair(position.first + direction.first, position.second + direction.second)
                if (!positions.contains(position)) {
                    positions.add(position)
                }
            }
        }
        return positions
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input)
        var position = getStartPosition(map)
        var direction = Pair(-1, 0)
        val positions = getAllVisitedPositions(position, direction, map)
        return positions.size
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input)

        val startPosition = getStartPosition(map)
        val startDirection = Pair(-1, 0)

        val positions = getAllVisitedPositions(startPosition, startDirection, map)

        var result = 0
        for (position in positions) {

            if (position == startPosition) continue
            var currentRoute =  mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

            var positionInRoute = startPosition
            var directionInRoute = startDirection

            currentRoute.add(Pair(positionInRoute, directionInRoute))

            var isLoop = false

            while (true) {
                if (isOutsideOfMap(directionInRoute, positionInRoute, map)) break
                else if (map[positionInRoute.first + directionInRoute.first][positionInRoute.second + directionInRoute.second] == '#' ||
                    (positionInRoute.first + directionInRoute.first == position.first && positionInRoute.second + directionInRoute.second == position.second)) {
                    directionInRoute = rotate(directionInRoute)
                } else {
                    positionInRoute = Pair(positionInRoute.first + directionInRoute.first, positionInRoute.second + directionInRoute.second)
                    if (!currentRoute.contains(Pair(positionInRoute, directionInRoute))) {
                        currentRoute.add(Pair(positionInRoute, directionInRoute))
                    } else {
                        isLoop = true
                        break
                    }
                }
            }

            if (isLoop){
                result++
            }
        }
        return result
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)


    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
