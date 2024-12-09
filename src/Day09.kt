fun main() {
    fun unarchived(d: String): List<Int> {
        var id = 0
        var rs = mutableListOf<Int>()
        for (i in d.indices) {
            val count = d[i].digitToInt()
            if (i % 2 != 0) {
                (0 until count).forEach { j ->
                    rs.add(-1)
                }
            } else {
                (0 until count).forEach { j ->
                    rs.add(id)
                }
                id++
            }
        }
        return rs
    }

    fun defragmentation(lst: List<Int>): List<Int> {
        var freeSpaceInd = 0
        var blockInd = lst.size - 1

        val sb = mutableListOf<Int>()

        while (freeSpaceInd != blockInd) {
            if (lst[freeSpaceInd] != -1) {
                sb.add(lst[freeSpaceInd])
                freeSpaceInd++
            } else {
                while (lst[blockInd] == -1) {
                    blockInd--
                }
                sb.add(lst[blockInd])
                blockInd--
                freeSpaceInd++
            }
        }
        sb.add(lst[blockInd])

        return sb
    }

    fun calcCheckSum(lst: List<Int>): Long {
        var sum = 0L
        for (i in lst.indices) {
            if (lst[i] == -1) continue
            sum += (i * lst[i])
        }
        return sum
    }

    class Block(val position: Int, val size: Int, val content: Int)

    fun parseBlocks(input: String): List<Block> {
        var currentId = 0
        var currentPosition = 0
        var blocks = mutableListOf<Block>()
        for (i in input.indices) {
            val size = input[i].digitToInt()
            if (i % 2 != 0) {
                blocks.add(Block(currentPosition, size, -1))
            } else {
                blocks.add(Block(currentPosition, size, currentId))
                currentId++
            }
            currentPosition += size
        }
        return blocks
    }

    fun defragmentationBlocks(lst: List<Block>): List<Block> {
        var result = lst.toMutableList()
        var moved = mutableSetOf<Block>()
        var checkedPlaces = mutableSetOf<Int>()
        while (true) {
            var placeCandidate = -1
            for (i in result.indices) {
                if (result[i].content == -1 && !checkedPlaces.contains(i)) {
                    placeCandidate = i
                    checkedPlaces.add(i)
                    break
                }
            }
            if (placeCandidate == -1) break
            var candidates = mutableListOf<Pair<Block, Int>>()
            for (i in result.size - 1 downTo placeCandidate + 1) {
                if (result[i].content != -1 && !moved.contains(result[i]) && result[i].size <= result[placeCandidate].size) {
                    candidates.add(Pair(result[i], i))
                }
            }
            candidates.sortBy { it.first.content }
            var candidate = candidates.lastOrNull()
            if (candidate == null) {
                continue
            }
            val pos = candidate.second
            val blockToMove = candidate.first
            val emptySpace = result[placeCandidate].size - blockToMove.size

            result[placeCandidate] = Block(result[placeCandidate].position, blockToMove.size, blockToMove.content)
            result[pos] = Block(blockToMove.position, blockToMove.size, -1)
            if (emptySpace > 0) {
                result.add(placeCandidate + 1, Block(result[placeCandidate].position + emptySpace, emptySpace, -1))
            }

            moved.add(blockToMove)

        }
        return result
    }

    fun calcCheckSumBlocks(lst: List<Block>): Long {
        var sum = 0L
        var ind = 0
        for (i in lst) {
            (0 until i.size).forEach { j ->
                if (i.content != -1) {
                    sum += (ind * i.content)
                }
                ind++
            }
        }
        return sum
    }

    fun part1(input: List<String>): Long {
        val items = input[0]
        val unarchived = unarchived(items)
        val compacted = defragmentation(unarchived)
        val checkSum = calcCheckSum(compacted)
        return checkSum
    }

    fun part2(input: List<String>): Long {
        val items = input[0]
        val blocks = parseBlocks(items)
        val compacted = defragmentationBlocks(blocks)
        val checkSum = calcCheckSumBlocks(compacted)

        return checkSum
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
