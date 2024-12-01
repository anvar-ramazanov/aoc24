import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {

        var leftArr: MutableList<Int> = mutableListOf()
        var rightArr: MutableList<Int> = mutableListOf()
        for (i in input.indices) {
            val tokens = input.getOrNull(i)?.split("   ")!!
            leftArr.add(tokens[0].toInt())
            rightArr.add(tokens[1].toInt())
        }

        leftArr.sort()
        rightArr.sort()

        var sum = 0
        for (i in leftArr.indices) {
            sum += abs(leftArr[i] - rightArr[i])
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var leftArr: MutableList<Int> = mutableListOf()
        var rightMap: MutableMap<Int, Int> = mutableMapOf()
        for (i in input.indices) {
            val tokens = input.getOrNull(i)?.split("   ")!!
            leftArr.add(tokens[0].toInt())
            var right = tokens[1].toInt()

            if (rightMap.containsKey(right)) {
                rightMap.put(right, rightMap[right]!! + 1)
            } else {
                rightMap.put(right, 1)
            }
        }

        var similarity = 0
        for (left in leftArr) {
            similarity += left * if (rightMap.containsKey(left)) rightMap.getValue(left) else 0
        }

        return similarity
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
