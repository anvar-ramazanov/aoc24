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

    fun part2(input: List<String>): String {
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
        var ruleList = mutableListOf<Rule>()
        val queue: Queue<Rule> = LinkedList()
        for (rule in rules) {
            val tokens = rule.split("->")
            val left = tokens[0].trim().split(" ")
            val leftRegister = left[0].trim()
            val operand = left[1].trim()
            val rightRegister = left[2].trim()
            val result = tokens[1].trim()
            val rule = Rule(leftRegister, rightRegister, operand, result)
            ruleList.add(rule)
            queue.add(rule)
        }

       val xRegisters = registers
            .filter { it.key.startsWith("x") }
            .toSortedMap()
            .map { it.value }
            .joinToString("")
            .reversed()

        val x = xRegisters.toLong(2)

        val yRegisters = registers
            .filter { it.key.startsWith("y") }
            .toSortedMap()
            .map { it.value }
            .joinToString("")
            .reversed()

        val y = yRegisters.toLong(2)


        println((x+y).toString(2))

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

        println(zRegisters)

        for (rule in ruleList) {
            println("${rule.leftRegister} -- ${rule.operand} ---> ${rule.result}")
            println("${rule.rightRegister} -- ${rule.operand} ---> ${rule.result}")
        }

        val lst = listOf("z33", "hgj", "z19", "cph", "z13", "npf", "nnt", "gws")

        val result =  lst.sortedBy { it }.joinToString(",")

        return result
    }

    //check(part2(testInput) == 8)


    val input = readInput("Day24")
    // part1(input).println()
    part2(input).println()
}
