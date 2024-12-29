fun main() {

    fun parseToMap(input: List<String>): Map<String, Set<String>> {
        val map = mutableMapOf<String, MutableSet<String>>()

        for (line in input) {
            val tokens = line.split("-")
            if (map.containsKey(tokens[0])) {
                map[tokens[0]]!!.add(tokens[1])
            } else {
                map[tokens[0]] = mutableSetOf(tokens[1])
            }

            if (map.containsKey(tokens[1])) {
                map[tokens[1]]!!.add(tokens[0])
            } else {
                map[tokens[1]] = mutableSetOf(tokens[0])
            }
        }

        return map
    }

    fun part1(input: List<String>): Int {
        val map = parseToMap(input)

        val sets = mutableSetOf<Set<String>>()
        for (key in map.keys) {
            for (next in map[key]!!) {
                val nextNodes = map[next]!!
                for (next2 in nextNodes) {
                    if (map[next2]!!.contains(key)) {
                        sets.add(setOf(key, next, next2))
                    }
                }
            }
        }

        var cnt = 0
        for (set in sets) {
            for (key in set) {
                if (key.startsWith("t")) {
                    cnt++
                    break
                }
            }
        }

        return cnt

    }

    fun part2(input: List<String>): String {
        val map = parseToMap(input)

        fun dfs(key: String, set: MutableSet<String>) {
            val nextNodes = map[key]!!
            for (next in nextNodes) {
                val nextNodes2 = map[next]!!
                if (nextNodes2.containsAll(set)) {
                    set.add(next)
                    dfs(next, set)
                }
            }
        }

        val sets = mutableSetOf<Set<String>>()
        for (key in map.keys) {
            for (next in map[key]!!) {
                val set = mutableSetOf<String>()
                set.add(key)
                set.add(next)
                dfs(next, set)
                sets.add(set)
            }
        }

        val biggest = sets.maxBy { it.size }

        val result = biggest.sortedBy { it }.joinToString(",")

        return result
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")


    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
