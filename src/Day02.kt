import kotlin.math.abs

fun main() {
    fun isSafe(lst: List<Int>): Boolean {
        var step = lst[1] - lst[0]
        if (abs(step) > 3 || abs(step) < 1) {
            return false
        }
        for (i in 2 until lst.size) {
            val currentStep = lst[i] - lst[i - 1]
            if (abs(currentStep) > 3 || abs(currentStep) < 1) {
                return false
            }
            if ((currentStep xor step) < 0) {
                return false
            }
            step = currentStep
        }
        return true
    }

    fun <T> getAllListsExceptOne(originalList: List<T>): List<List<T>> {
        return originalList.mapIndexed { index, _ ->
            originalList.filterIndexed { i, _ -> i != index }
        }
    }

    fun part1(input: List<String>): Int {
        var safe = 0
        for (line in input) {
            var lst = mutableListOf<Int>()
            val tokens = line.split(" ")
            for (token in tokens) {
                lst.add(token.toInt())
            }
            if (isSafe(lst)) {
                safe++
            }
        }

        return safe
    }

    fun part2(input: List<String>): Int {
        var safe = 0
        for (line in input) {
            var lst = mutableListOf<Int>()
            val tokens = line.split(" ")
            for (token in tokens) {
                lst.add(token.toInt())
            }
            val allListsExceptOne = getAllListsExceptOne(lst)
            for (list in allListsExceptOne) {
                if (isSafe(list)) {
                    safe++
                    break
                }
            }
        }

        return safe
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
