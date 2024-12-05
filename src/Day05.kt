fun main() {
    fun isValidUpdate(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        for (i in update.indices) {
            val rule = rules[update[i]]
            for (j in 0..i) {
                if (rule?.contains(update[j]) == true) {
                    return false
                }
            }
        }
        return true
    }

    fun parseInput(input: List<String>): Pair<Map<Int, List<Int>>, List<List<Int>>> {
        var isMapReading = true
        var rules = mutableMapOf<Int, MutableList<Int>>()
        var updates = mutableListOf<MutableList<Int>>()
        for (line in input) {
            if (line.isEmpty()) {
                isMapReading = false
                continue
            }
            if (isMapReading) {
                val tokens = line.split("|")
                val left = tokens[0].trim().toInt()
                val right = tokens[1].trim().toInt()
                if (rules.containsKey(left)) {
                    rules[left]?.add(right)
                } else {
                    rules.put(left, mutableListOf(right))
                }
            } else {
                val tokens = line.split(",")
                updates.add(tokens.map { it.trim().toInt() }.toMutableList())
            }
        }
        return Pair(rules, updates)
    }

    fun part1(input: List<String>): Int {
        var (rules, updates) = parseInput(input)
        var result = 0
        for (update in updates) {
            if (isValidUpdate(update, rules)) {
                val mid = update.size / 2
                result += update[mid]
            }
        }
        return result
    }

    fun getValidUpdate(update: List<Int>, rules: Map<Int, List<Int>>):  List<Int> {
        var result = mutableListOf<Int>()

        result.add(update[0])

        for (i in 1..update.size - 1) {
            var isAdded = false
            for (j in 0..result.size - 1) {
                var temp = mutableListOf<Int>()
                temp.addAll(result)
                temp.add(j, update[i])
                if (isValidUpdate(temp, rules)) {
                    result.add(j, update[i])
                    isAdded = true
                    break
                }
            }
            if (!isAdded) {
                result.add(update[i])
            }
        }

        return result
    }

    fun part2(input: List<String>): Int {
        var (rules, updates) = parseInput(input)
        var result = 0
        for (update in updates) {
            if (!isValidUpdate(update, rules)) {
                val valid = getValidUpdate(update, rules)
                val mid = valid.size / 2
                result += valid[mid]
            }
        }
        return result
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)


    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
