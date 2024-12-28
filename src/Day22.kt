import kotlin.math.floor

fun main() {

    fun part1(input: List<String>): Long {
        val steps = 2000
        val secrets = input.map { it.trim().toLong() }

        var result = 0L

        for (secret in secrets) {
            var s1 = secret
            (0 until steps).forEach { i ->
                s1 = ((s1 * 64) xor s1) % 16777216
                s1 = (floor(s1 / 32.0).toLong() xor s1) % 16777216
                s1 = ((s1 * 2048) xor s1) % 16777216
            }
            result += s1
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val steps = 2000
        val secrets = input.map { it.trim().toLong() }

        val customerPrices: MutableMap<Long, List<Int>> = mutableMapOf()

        for (secret in secrets) {
            var s1 = secret
            val prices = mutableListOf<Int>()
            prices.add(s1.toInt() % 10)
            (0 until steps).forEach { i ->
                s1 = ((s1 * 64) xor s1) % 16777216
                s1 = (floor(s1 / 32.0).toLong() xor s1) % 16777216
                s1 = ((s1 * 2048) xor s1) % 16777216
                prices.add((s1 % 10).toInt())
            }
            customerPrices[secret] = prices
        }

        val deltas = mutableListOf<List<Int>>()
        for (secret in secrets) {
            val currentDelta = mutableListOf<Int>()
            for (i in 1 until steps) {
                val delta = customerPrices[secret]!![i] - customerPrices[secret]!![i-1]
                currentDelta.add(delta)
            }
            deltas.add(currentDelta)
        }

        val quartetMap = mutableMapOf<List<Int>, MutableList<Pair<Int, Int>>>()
        val quartetPrices = mutableMapOf<List<Int>, Int>()
        var maxSum = 0

        for ((i, _) in secrets.withIndex()) {
            val currentDelta = deltas[i]
            for (j in 3 until (steps - 1)) {
                val quartet = listOf(
                    currentDelta[j - 3],
                    currentDelta[j - 2],
                    currentDelta[j - 1],
                    currentDelta[j]
                )
                quartetMap.getOrPut(quartet) { mutableListOf() }.add(i to j)
            }
        }


        for ((quartet, indices) in quartetMap) {
            var sumPrices = 0
            val seenSecrets = mutableSetOf<Int>()

            for ((secretIndex, j) in indices) {
                if (secretIndex !in seenSecrets) {
                    seenSecrets.add(secretIndex)
                    val price = customerPrices[secrets[secretIndex]]!![j + 1]
                    sumPrices += price
                }
            }

            quartetPrices[quartet] = sumPrices
            if (sumPrices > maxSum) {
                maxSum = sumPrices
            }
        }

        return maxSum
    }

    val testInput = readInput("Day22_test")
    check(part1(testInput) == 37327623L)
    check(part2(listOf("1", "2", "3", "2024")) == 23)


    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
