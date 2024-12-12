import kotlin.math.abs

fun main() {
    fun getNeighbors(currentPosition: Pair<Int, Int>, map: List<String>): List<Pair<Int, Int>> {
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        val current = map[currentPosition.first][currentPosition.second]
        val result = mutableListOf<Pair<Int, Int>>()
        for (direction in directions) {
            if (currentPosition.first + direction.first < 0 || currentPosition.first + direction.first >= map.size) continue
            if (currentPosition.second + direction.second < 0 || currentPosition.second + direction.second >= map[0].length) continue
            if (map[currentPosition.first + direction.first][currentPosition.second + direction.second] != current) continue
            result.add(Pair(currentPosition.first + direction.first, currentPosition.second + direction.second))
        }
        return result
    }

    fun parseRegion(currentPosition: Pair<Int, Int>, map: List<String>, currentRegion: MutableList<Pair<Int, Int>>, visited: MutableSet<Pair<Int, Int>>) {
        currentRegion.add(currentPosition)
        visited.add(currentPosition)
        val neighbors = getNeighbors(currentPosition, map)
        for (neighbor in neighbors) {
            if (visited.contains(neighbor)) continue
            parseRegion(neighbor, map, currentRegion, visited)
        }
    }

    fun parseRegions(input: List<String>): List<List<Pair<Int, Int>>> {
        val checked = mutableSetOf<Pair<Int, Int>>()
        val result = mutableListOf<List<Pair<Int, Int>>>()
        for ((i, line) in input.withIndex()) {
            for ((j, _) in line.withIndex()) {
                if (checked.contains(Pair(i, j))) continue
                val region = mutableListOf<Pair<Int, Int>>()
                val visited = mutableSetOf<Pair<Int, Int>>()
                parseRegion(Pair(i, j), input, region, visited)
                for (r in region) {
                    checked.add(r)
                }
                result.add(region)
            }
        }
        return result
    }

    fun getNeighborsCount(point: Pair<Int, Int>, region: List<Pair<Int, Int>>): Int {
        var result = 0
        for (p in region) {
            if (p.first == point.first && p.second == point.second) continue
            if (p.first == point.first && abs(p.second - point.second) == 1) result++
            if (p.second == point.second && abs(p.first - point.first) == 1) result++
        }
        return result
    }

    fun getRegionPerimeter(region: List<Pair<Int, Int>>): Int {
        var result = 0
        for (point in region) {
            result += (4 - getNeighborsCount(point, region))
        }
        return result
    }

    fun part1(input: List<String>): Int {
        var result = 0
        val regions = parseRegions(input)

        for (region in regions) {
            result += (region.size * getRegionPerimeter(region))
        }

        return result
    }

    fun getRegionSides(region: List<Pair<Int, Int>>): Int {
       var mapWithFences = mutableMapOf<Pair<Int, Int>, Set<Char>>()

        for (point in region) {
            val fences = mutableSetOf<Char>()
            if (!region.contains(Pair(point.first + 1, point.second))) fences.add('V')
            if (!region.contains(Pair(point.first - 1, point.second))) fences.add('^')
            if (!region.contains(Pair(point.first, point.second + 1))) fences.add('<')
            if (!region.contains(Pair(point.first, point.second - 1))) fences.add('>')
            mapWithFences.put(Pair(point.first, point.second), fences)
        }

        val top = region.minOf { it.first }
        val bottom = region.maxOf { it.first }
        val left = region.minOf { it.second }
        val right = region.maxOf { it.second }

        // top side
        var topSides = 0
        var prevLevel = -1
        for (i in top..bottom) {
            prevLevel = -1
            for (j in left..right) {
                if (mapWithFences.contains(Pair(i, j)) && mapWithFences[Pair(i, j)]!!.contains('^')) {
                    if (prevLevel != i) {
                        topSides++
                    }
                    prevLevel = i
                }
                else {
                    prevLevel = -1
                }
            }
        }

        // right side
        var rightSides = 0
        for (j in right downTo left) {
            prevLevel = -1
            for (i in top..bottom) {
                if (mapWithFences.contains(Pair(i, j)) && mapWithFences[Pair(i, j)]!!.contains('<')) {
                    if (prevLevel != j) {
                        rightSides++
                    }
                    prevLevel = j
                } else {
                    prevLevel = -1
                }
            }
        }

        // bottom side
        var bottomSide = 0
        for (i in bottom downTo top) {
            prevLevel = -1
            for (j in left..right) {
                if (mapWithFences.contains(Pair(i, j)) && mapWithFences[Pair(i, j)]!!.contains('V')) {
                    if (prevLevel != i) {
                        bottomSide++
                    }
                    prevLevel = i
                }
                else {
                    prevLevel = -1
                }
            }
        }

        var leftSides = 0
        for (j in left..right) {
            prevLevel = -1
            for (i in top..bottom) {
                if (mapWithFences.contains(Pair(i, j)) && mapWithFences[Pair(i, j)]!!.contains('>')) {
                    if (prevLevel != j) {
                        leftSides++
                    }
                    prevLevel = j
                }
                else {
                    prevLevel = -1
                }
            }
        }

        return topSides + rightSides + bottomSide + leftSides
    }

    fun part2(input: List<String>): Int {
        var result = 0
        val regions = parseRegions(input)

        for (region in regions) {
            val sides = getRegionSides(region)
            result += (region.size * sides)
        }

        return result
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
