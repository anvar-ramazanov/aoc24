import kotlin.math.pow

fun main() {

    data class State(
        var a: Long,
        var b: Long,
        var c: Long,
        val program: List<Int>,
        val output: MutableList<Long> = mutableListOf()
    )

    fun parseInput(input: List<String>): State {
        val a = input[0].split(":")[1].trim().toLong()
        val b = input[1].split(":")[1].trim().toLong()
        val c = input[2].split(":")[1].trim().toLong()
        val program = input[4].split(":")[1].trim().split(",").map { it.trim().toInt() }.toList()

        return State(a, b, c, program)
    }

    fun getComboOperand(literalOperand: Int, state: State): Long {
        when (literalOperand) {
            0 -> return 0
            1 -> return 1
            2 -> return 1
            3 -> return 3
            4 -> return state.a
            5 -> return state.b
            6 -> return state.c
            else -> {
                throw Exception("Invalid operand")
            }
        }
    }

    fun calcState(state: State) {
        var i = 0

        while (true) {
            if (i >= state.program.size) {
                break
            }
            val currentInstruction = state.program[i]
            val literalOperand = state.program[i + 1]
            when (currentInstruction) {
                0 -> { // adv
                    val comboOperand = getComboOperand(literalOperand, state)
                    val r = (state.a / 2.0.pow(comboOperand.toDouble())).toLong()
                    state.a = r
                }

                1 -> { // bxl
                    val r = state.b.xor(literalOperand.toLong())
                    state.b = r
                }

                2 -> { // bst
                    val comboOperand = getComboOperand(literalOperand, state)
                    val r = comboOperand % 8
                    state.b = r
                }

                3 -> { // jnz
                    if (state.a != 0L) {
                        i = literalOperand
                        continue
                    }
                }

                4 -> { // bxc
                    var r = state.b.xor(state.c)
                    state.b = r
                }

                5 -> { // out
                    val comboOperand = getComboOperand(literalOperand, state)
                    var r = comboOperand % 8
                    state.output.add(r)
                }

                6 -> { // bdv
                    val comboOperand = getComboOperand(literalOperand, state)
                    val r = (state.a / 2.0.pow(comboOperand.toDouble())).toLong()
                    state.b = r
                }

                7 -> { // cdv
                    val comboOperand = getComboOperand(literalOperand, state)
                    val r = (state.a / 2.0.pow(comboOperand.toDouble())).toLong()
                    state.c = r
                }
            }
            i += 2
        }

    }

    fun part1(input: List<String>): String {
        val state = parseInput(input)
        calcState(state)
        return state.output.joinToString(",")
    }


    fun part2(input: List<String>): Long {
        val state = parseInput(input)

        val toFind = state.program.joinToString("").toLong()

        val start  = (8.0.pow(state.program.size - 1) + (35 * 8.0.pow(state.program.size - 2))).toLong()
        val finish =  (8.0.pow(state.program.size - 1) + (36 * 8.0.pow(state.program.size - 2))).toLong()

        var a = start
        while (a < finish) {
            var currentState = State(a, 0, 0, state.program)

            calcState(currentState)

            val current = currentState.output.joinToString("").toLong()

            if (current == toFind) {
                break
            }
            a += 1

        }

        return a
    }




    val testInput = readInput("Day17_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")

    val testInput2 = readInput("Day17_test2")
    //check(part2(testInput2) == 117440L)

    val input = readInput("Day17")
    part1(input).println()
    //part2(input).println()
}
