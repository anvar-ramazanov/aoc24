fun main() {
    fun part1(input: List<String>): Int {
        val pattern = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
        var sum = 0
        for (line in input) {
            val matches = pattern.findAll(line)
            for (match in matches) {
                val str = match.groups[0]?.value!!
                val tokens = str.replace("mul(", "").replace(")", "").split(",")
                sum += (tokens[0].toInt() * tokens[1].toInt())
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val pattern = Regex("""mul\(\d{1,3},\d{1,3}\)|don't\(\)|do\(\)""")
        var result = 0
        for (line in input) {
            val matches = pattern.findAll(line)
            var isSkip = false
            for (match in matches) {
                val str = match.groups[0]?.value!!
                if (str == "don't()") {
                    isSkip = true
                    continue
                }
                else if (str == "do()") {
                    isSkip = false
                    continue
                }
                if (!isSkip) {
                    val tokens = str.replace("mul(", "").replace(")", "").split(",")
                    result += (tokens[0].toInt() * tokens[1].toInt())
                }
            }
        }
        return result
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)


    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
