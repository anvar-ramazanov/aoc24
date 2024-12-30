import java.util.LinkedList
import java.util.Queue

fun main() {

    data class Rule (
        val leftRegister: String,
        val rightRegister: String,
        val operand: String,
        val result: String
    )

    fun getResult(left: Int, operand: String, right: Int): Int {
        when (operand) {
            "AND" -> return if (left == 1 && right == 1) 1 else 0
            "OR"  -> return if (left == 1 || right == 1) 1 else 0
            "XOR" -> return if (left != right) 1 else 0
        }
        throw Exception("Unknown operand")
    }

    fun part1(input: List<String>): Long {
        val registers = mutableMapOf<String, Int>()
        var ind = -1
        for (line in input) {
            ind++
            if (line == "") {
                break
            }
            val tokens = line.split(":")
            registers[tokens[0].trim()] = tokens[1].trim().toInt()
        }

        val rules = input.subList(ind + 1, input.size)
        val queue: Queue<Rule> = LinkedList()
        for (rule in rules) {
            val tokens = rule.split("->")
            val left = tokens[0].trim().split(" ")
            val leftRegister = left[0].trim()
            val operand = left[1].trim()
            val rightRegister = left[2].trim()
            val result = tokens[1].trim()
            val rule = Rule(leftRegister, rightRegister, operand, result)
            queue.add(rule)
        }

        while (queue.isNotEmpty()) {
            val rule = queue.remove()
            if (registers.containsKey(rule.leftRegister) && registers.containsKey(rule.rightRegister)) {
                val result = getResult(registers[rule.leftRegister]!!, rule.operand, registers[rule.rightRegister]!!)
                registers[rule.result] = result
            } else {
                queue.add(rule)
            }
        }

        val zRegisters = registers
            .filter { it.key.startsWith("z") }
            .toSortedMap()
            .map { it.value }
            .joinToString("")
            .reversed()

        val result = zRegisters.toLong(2)

        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput) == 2024L)
    //check(part2(testInput) == 8)


    val input = readInput("Day24")
    part1(input).println()
    //part2(input).println()
}
