import java.awt.Color
import java.io.File
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

fun main() {

    data class Robot(val x: Int, val y: Int, val dx: Int, val dy: Int)

    fun parseInput(input: List<String>): List<Robot> {
        val result = mutableListOf<Robot>()
        val regex = """-?\d+""".toRegex()
        for (line in input) {
            val numbers = regex.findAll(line).map { it.value.toInt() }.toList()
            result.add(Robot(numbers[0], numbers[1], numbers[2], numbers[3]))
        }
        return result
    }

    fun moveRobot(robot: Robot, cycles: Int, mapX: Int, mapY: Int): Pair<Int, Int> {
        var currentX = robot.x
        var currentY = robot.y
        (0 until cycles).forEach { i ->
            currentX += robot.dx
            currentY += robot.dy

            if (currentY < 0) {
                currentY = mapY + currentY
            }
            if (currentY >= mapY) {
                currentY = currentY - mapY
            }
            if (currentX < 0) {
                currentX = mapX + currentX
            }
            if (currentX >= mapX) {
                currentX = currentX - mapX
            }
        }
        return Pair(currentX, currentY)
    }

    fun part1(input: List<String>, mapX: Int, mapY: Int): Int {
        val robots = parseInput(input)
        val cycles = 100
        val robotsPositions = mutableMapOf<Pair<Int, Int>, Int>()
        for (robot in robots) {
            val (currentX, currentY) = moveRobot(robot, cycles, mapX, mapY)
            if (robotsPositions.containsKey(Pair(currentX, currentY))) {
                robotsPositions[Pair(currentX, currentY)] = robotsPositions[Pair(currentX, currentY)]!! + 1
            } else {
                robotsPositions[Pair(currentX, currentY)] = 1
            }
        }

        val middleX = (mapX - 1) / 2
        val middleY = (mapY - 1) / 2

        var topLeft = 0
        for (i in 0 .. middleX - 1) {
            for (j in 0..middleY - 1) {
                if (robotsPositions.containsKey(Pair(i, j))) {
                    topLeft += robotsPositions[Pair(i, j)]!!
                }
            }
        }

        var topRight = 0
        for (i in middleX +1.. mapX) {
            for (j in 0..middleY-1) {
                if (robotsPositions.containsKey(Pair(i, j))) {
                    topRight += robotsPositions[Pair(i, j)]!!
                }
            }
        }

        // bottomLeft
        var bottomLeft = 0
        for (i in 0 .. middleX - 1) {
            for (j in middleY + 1..mapY) {
                if (robotsPositions.containsKey(Pair(i, j))) {
                    bottomLeft += robotsPositions[Pair(i, j)]!!
                }
            }
        }

        // bottomRight
        var bottomRight = 0
        for (i in middleX +1.. mapX) {
            for (j in middleY + 1..mapY) {
                if (robotsPositions.containsKey(Pair(i, j))) {
                    bottomRight += robotsPositions[Pair(i, j)]!!
                }
            }
        }

        return topLeft * topRight * bottomLeft * bottomRight
    }

    fun printPicture(
        map: MutableMap<Pair<Int, Int>, Int>,
        mapX: Int,
        mapY: Int,
        cycle: Int
    ) {
        val bufferedImage = BufferedImage(mapX, mapY, BufferedImage.TYPE_INT_RGB)
        val graphics = bufferedImage.createGraphics()
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, mapX, mapY)

        for (x in 0 until mapX) {
            for (y in 0 until mapY) {
                if (map.containsKey(Pair(x, y))) {
                    graphics.color = Color.BLACK
                    graphics.fillRect(x, y, 1, 1)
                }
            }
        }

        graphics.dispose()

        val outputFile = File("temp//${cycle}.png")
        ImageIO.write(bufferedImage, "png", outputFile)
    }

    fun part2(input: List<String>, mapX: Int, mapY: Int) {
        val robots = parseInput(input)

        val startPattern = 305
        val patternStep = 101

        for (i in 0..100) {
            val robotsPositions = mutableMapOf<Pair<Int, Int>, Int>()
            val cycle =  startPattern + i * patternStep
            for (robot in robots) {
                val (currentX, currentY) = moveRobot(robot, cycle, mapX, mapY)
                if (robotsPositions.containsKey(Pair(currentX, currentY))) {
                    robotsPositions[Pair(currentX, currentY)] = robotsPositions[Pair(currentX, currentY)]!! + 1
                } else {
                    robotsPositions[Pair(currentX, currentY)] = 1
                }
            }

            printPicture(robotsPositions, mapX, mapY, cycle)
        }
    }

    val testInput = readInput("Day14_test")
    check(part1(testInput, 11, 7) == 12)
    //check(part2(testInput) == 480L)

    val input = readInput("Day14")
    part1(input, 101, 103).println()
    part2(input, 101, 103).println()
}
