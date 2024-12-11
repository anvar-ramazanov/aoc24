fun main() {
    fun parseInput(input: List<String>) : List<Long> {
        var line = input[0]
        return line.split(" ").map { it.toLong() }
    }

    fun blink(stones: List<Long>) : List<Long> {
        var result = mutableListOf<Long>()

        for(stone in stones) {
            val stoneStr = stone.toString()
            if (stone == 0L) {
                result.add(1)
            }
            else if (stone.toString().length % 2 == 0) {
                val newStone1 = stoneStr.substring(0, stoneStr.length / 2).toLong()
                val newStone2 = stoneStr.substring(stoneStr.length / 2).toLong()
                result.add(newStone1)
                result.add(newStone2)
            }
            else {
                result.add(stone * 2024)
            }
        }

        return result
    }

    fun part1(input: List<String>): Int {
        var stones = parseInput(input)

        (0 until 25).forEach { i ->
            stones = blink(stones)
        }

        return stones.size
    }

    fun part2(input: List<String>): Long {
        var stones = parseInput(input)

        val map = mutableMapOf<Long, List<Long>>()
        val queue = ArrayDeque<Long>()
        for (stone in stones) {
            queue.add(stone)
        }

       while (queue.isNotEmpty()) {
           val stone = queue.removeFirst()
           if (map.containsKey(stone)) continue
           var tempStones = listOf<Long>(stone)
           (0 until 25).forEach { i ->
               tempStones = blink(tempStones)
           }
           for (tempStone in tempStones){
               queue.add(tempStone)
           }
           map[stone] = tempStones
        }

        var result = 0L
        for (stone in stones) {
            var sum = 0L
            val stonesOn25 = map[stone]
            for (stoneOn25 in stonesOn25!!) {
                val stoneOn50 = map[stoneOn25]
                for (stoneOn50 in stoneOn50!!) {
                    sum += map[stoneOn50]!!.size
                }
            }
            result += sum
        }

        return result
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312)
    //check(part2(testInput) == 55312L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
