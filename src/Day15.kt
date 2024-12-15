fun main() {

    data class State (val map: MutableMap<Pair<Int, Int>, Char>, val movements: List<Char>, var robotPos: Pair<Int, Int>, val maxRow: Int, val maxCol: Int)

    fun parseInput(input: List<String>): State {
        val map = mutableMapOf<Pair<Int, Int>, Char>()
        var lastRowInd = -1
        var robotPos = Pair(0, 0)
        for ((i, line) in input.withIndex()) {
            if (line.isEmpty()) {
                lastRowInd = i
                break
            }
            for ((j, c) in line.withIndex()) {
                if (c == '@') {
                    robotPos = Pair(i, j)
                }
                map.put(Pair(i, j), c)
            }
        }

        var movements = mutableListOf<Char>()
        for (i in lastRowInd + 1 until input.size) {
            movements.addAll(input[i].toCharArray().toList())
        }

        return State(map, movements, robotPos, input.size, input[0].length)
    }

    fun getDirection(movement: Char): Pair<Int, Int> {
        when (movement) {
            '>' -> return Pair(0, 1)
            '<' -> return Pair(0, -1)
            '^' -> return Pair(-1, 0)
            'v' -> return Pair(1, 0)
            else -> return Pair(0, 0)
        }
    }

    fun calcMap(inputData: State, box: Char) : Int {
        var result = 0
        for (i in 0..inputData.maxRow) {
            for (j in 0..inputData.maxCol) {
                if (inputData.map.getOrDefault(Pair(i, j), '.') == box) {
                    result += ((i * 100) + j)
                }
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        var state = parseInput(input)

        for (movement in state.movements) {
            val direction = getDirection(movement)
            var currentPos = Pair(state.robotPos.first, state.robotPos.second)
            var steps = 0
            while (true) {
                val pos = Pair(currentPos.first + steps * direction.first, currentPos.second + steps * direction.second)
                steps++
                if (state.map[pos] == '.') {
                    currentPos = pos
                    break
                } else if (state.map[pos] == '#') {
                    break
                }
            }

            if (currentPos == state.robotPos) {
                continue
            }
            else {
                val newRobotPos = Pair(state.robotPos.first + direction.first, state.robotPos.second + direction.second)
                var currentPos2 = newRobotPos
                while (true) {
                    if (currentPos2 == currentPos) break
                    currentPos2 = Pair(currentPos2.first + direction.first, currentPos2.second + direction.second)
                    state.map[currentPos2] = 'O'
                }
                state.map.put(newRobotPos, '@')
                state.map[state.robotPos] = '.'
                state.robotPos = newRobotPos
            }
        }

        return calcMap(state, 'O')
    }

    fun transformState(state: State) : State {
        val newMap = mutableMapOf<Pair<Int, Int>, Char>()
        var newRobotPos = Pair(0, 0)
        for (i in 0..state.maxRow - 1) {
            for (j in 0..state.maxCol - 1) {
                when (state.map.getOrDefault(Pair(i, j), 'X')) {
                    '#' -> {
                        newMap.put(Pair(i, j * 2), '#')
                        newMap.put(Pair(i, j* 2 + 1), '#')
                    }
                    'O' -> {
                        newMap.put(Pair(i, j * 2), '[')
                        newMap.put(Pair(i, j * 2 + 1), ']')
                    }
                    '@'-> {
                        newMap.put(Pair(i, j * 2), '@')
                        newRobotPos = Pair(i, j * 2)
                        newMap.put(Pair(i, j * 2 + 1), '.')
                    }
                    '.' -> {
                        newMap.put(Pair(i, j * 2), '.')
                        newMap.put(Pair(i, j * 2 + 1), '.')
                    }
                }
            }
        }

        return State(newMap, state.movements, newRobotPos, state.maxRow, state.maxCol * 2)
    }

    fun performHorizontalMove(
        newRobotPos: Pair<Int, Int>,
        left: Pair<Int, Int>,
        right: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        inputData: State
    ) {
        val chain = mutableListOf<Pair<Int, Int>>()

        if (direction.second == -1) {
            chain.add(right)
            chain.add(left)


            var nextPos = Pair(left.first, left.second - 1)
            while (inputData.map[nextPos] == '[' || inputData.map[nextPos] == ']') {
                chain.add(nextPos)
                nextPos = Pair(nextPos.first, nextPos.second - 1)
            }

            if (inputData.map[nextPos] == '.') {
                for (i in chain.indices.reversed()) {
                    val pos = chain[i]
                    inputData.map[Pair(pos.first, pos.second - 1)] = inputData.map[pos]!!
                    inputData.map[pos] = '.'
                }
                inputData.map[inputData.robotPos] = '.'
                inputData.robotPos = newRobotPos
                inputData.map[newRobotPos] = '@'
            }
        } else if (direction.second == 1) {
            chain.add(left)
            chain.add(right)

            var nextPos = Pair(right.first, right.second + 1)
            while (inputData.map[nextPos] == '[' || inputData.map[nextPos] == ']') {
                chain.add(nextPos)
                nextPos = Pair(nextPos.first, nextPos.second + 1)
            }

            if (inputData.map[nextPos] == '.') {
                for (i in chain.indices.reversed()) {
                    val pos = chain[i]
                    inputData.map[Pair(pos.first, pos.second + 1)] = inputData.map[pos]!!
                    inputData.map[pos] = '.'
                }
                inputData.map[inputData.robotPos] = '.'
                inputData.robotPos = newRobotPos
                inputData.map[newRobotPos] = '@'
            }
        }
    }

    fun performVerticalMove(newRobotPos: Pair<Int, Int>, left: Pair<Int, Int>, right: Pair<Int, Int>, direction: Pair<Int, Int>, inputData: State) {

        val chain = mutableListOf<List<Pair<Int, Int>>>()

        chain.add(listOf(left, right))
        var currentLevel = 0

        while(true)
        {
            val boxes = chain.get(currentLevel)
            val newLevel = mutableSetOf<Pair<Int, Int>>()
            for (box in boxes) {
                val nextPos = Pair(box.first + direction.first, box.second + direction.second)
                if (inputData.map[nextPos] == '[') {
                    newLevel.add(nextPos)
                    newLevel.add(Pair(nextPos.first, nextPos.second + 1))
                }
                if (inputData.map[nextPos] == ']') {
                    newLevel.add(Pair(nextPos.first, nextPos.second - 1))
                    newLevel.add(nextPos)
                }
            }
            if (newLevel.isEmpty()) {
                break
            }

            chain.add(newLevel.toList())
            currentLevel++
        }

        var canMove = true
        for (level in chain) {
            for (box in level) {
                val nextPos = Pair(box.first + direction.first, box.second + direction.second)
                if (inputData.map[nextPos] == '#') {
                    canMove = false
                    break
                }
            }
        }

        if (!canMove) {
            return
        }

        for (i in chain.indices.reversed()) {
            val level = chain[i]
            for (box in level) {
                val nextPos = Pair(box.first + direction.first, box.second + direction.second)
                inputData.map[nextPos] = inputData.map[box]!!
                inputData.map[box] = '.'
            }
        }

        inputData.map[inputData.robotPos] = '.'
        inputData.robotPos = newRobotPos
        inputData.map[newRobotPos] = '@'
    }

    fun part2(input: List<String>): Int {
        var state = parseInput(input)

        state = transformState(state)

        for (movement in state.movements) {
            val direction = getDirection(movement)
            val newRobotPos = Pair(state.robotPos.first + direction.first, state.robotPos.second + direction.second)

            when (state.map[newRobotPos]) {
                '#' -> {
                    continue
                }
                '.' -> {
                    state.map[state.robotPos] = '.'
                    state.map[newRobotPos] = '@'
                    state.robotPos = newRobotPos
                }
                '[', -> {
                    if (movement == '>' || movement == '<') {
                        performHorizontalMove(newRobotPos, newRobotPos, Pair(newRobotPos.first, newRobotPos.second + 1), direction, state)
                    } else {
                        performVerticalMove(newRobotPos, newRobotPos, Pair(newRobotPos.first, newRobotPos.second + 1), direction, state)
                    }
                }
                ']' -> {
                    if (movement == '>' || movement == '<') {
                        performHorizontalMove(newRobotPos, Pair(newRobotPos.first, newRobotPos.second - 1), newRobotPos, direction, state)
                    } else {
                        performVerticalMove(newRobotPos, Pair(newRobotPos.first, newRobotPos.second - 1), newRobotPos, direction, state)
                    }
                }
            }
        }

        return calcMap(state, '[')
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)


    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
