fun main() {

    fun parseInput(input: List<String>): Pair<List<String>, List<String>> {
        val dict = input[0].split(",").map { it.trim() }

        val targets = mutableListOf<String>()
        for (i in 2 until input.size) {
            targets.add(input[i].trim())
        }

        return Pair(dict, targets)
    }

    fun canFormTarget(dict: List<String>, target: String): Boolean {
        val cache = mutableMapOf<String, Boolean>()

        fun canBuild(remaining: String): Boolean {
            if (remaining.isEmpty()) return true
            if (cache.containsKey(remaining)) return cache[remaining]!!

            for (str in dict) {
                if (remaining.startsWith(str)) {
                    val next = remaining.removePrefix(str)
                    if (canBuild(next)) {
                        cache[remaining] = true
                        return true
                    }
                }
            }

            cache[remaining] = false
            return false
        }

        return canBuild(target)
    }

    fun part1(input: List<String>): Int {
        val (dict, targets) = parseInput(input)

        var result = 0
        for (target in targets) {
            if (canFormTarget(dict, target)) {
                result++
            }
        }

        return result
    }

    fun countWaysToForm(strings: List<String>, target: String): Long {
        val cache = mutableMapOf<String, Long>()

        fun countWays(remaining: String): Long {
            if (remaining.isEmpty()) return 1
            if (cache.containsKey(remaining)) return cache[remaining]!!

            var ways = 0L
            for (str in strings) {
                if (remaining.startsWith(str)) {
                    val next = remaining.removePrefix(str)
                    ways += countWays(next)
                }
            }

            cache[remaining] = ways
            return ways
        }

        return countWays(target)
    }


    fun part2(input: List<String>): Long {
        val (dict, targets) = parseInput(input)

        var result = 0L
        for (target in targets) {
            result += countWaysToForm(dict, target)
        }

        return result
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)


    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
