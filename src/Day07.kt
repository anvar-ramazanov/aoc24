fun main() {
    fun parse(input: List<String>): List<Pair<Long, List<Long>>> {
        val result = mutableListOf<Pair<Long, List<Long>>>()
        for (line in input) {
            val tokens = line.split(":")
            val goal = tokens[0].trim().toLong()
            val items = tokens[1].trim().split(" ").map { it.trim().toLong() }
            result.add(Pair(goal, items))
        }
        return result
    }

    fun addIfValid(list: MutableList<Long>, value: Long, limit: Long) {
        if (value <= limit) {
            list.add(value)
        }
    }

    fun couldBeCalibrationResult(item: Pair<Long, List<Long>>, considerConcat: Boolean = false): Boolean {
        val (target, numbers) = item
        var currentSums = mutableListOf(numbers.first())

        for (num in numbers.drop(1)) {
            val newSums = mutableListOf<Long>()
            for (sum in currentSums) {
                addIfValid(newSums, sum + num, target)
                addIfValid(newSums, sum * num, target)
                if (considerConcat) {
                    addIfValid(newSums, (sum.toString() + num.toString()).toLong(), target)
                }
            }
            currentSums = newSums
        }

        return target in currentSums
    }

    fun part1(input: List<String>): Long {
        val parsed = parse(input)
        var result: Long = 0
        for (item in parsed) {
            if (couldBeCalibrationResult(item)) {
                result += item.first
            }
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val parsed = parse(input)
        var result: Long = 0
        for (item in parsed) {
            if (couldBeCalibrationResult(item, true)) {
                result += item.first
            }
        }
        return result
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)


    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
