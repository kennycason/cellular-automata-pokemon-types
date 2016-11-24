package com.kennycason.cellular.automata

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

/**
 * Created by kenny on 11/23/16.
 */

fun main(args: Array<String>) {
    CellularAutomataBalanced().run()
}

class CellularAutomataBalanced() {
    val random = Random()
    val screenWidth = 640
    val screenHeight = 480
    val cellDim = 2
    val width = screenWidth / cellDim
    val height = screenHeight / cellDim
    val saveImage = false
    val printConvergenceStats = true
    val mutualAttacks = false

    var canvas: BufferedImage = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
    var graphics = canvas.createGraphics()

    var fg = array2d(width, height, { Cell(element = Element.NONE) })
    var bg = array2d(width, height, { Cell(element = Element.NONE) })

    val palette = mapOf(
            Pair(Element.NONE, Color(0x000000).rgb),
            Pair(Element.WATER, Color(0x086FFF).rgb),
            Pair(Element.FIRE, Color(0xFF6608).rgb),
            Pair(Element.GRASS, Color(0x1f962b).rgb)
    )
    val population = mapOf(
            Pair(Element.NONE, AtomicInteger()),
            Pair(Element.WATER, AtomicInteger()),
            Pair(Element.FIRE, AtomicInteger()),
            Pair(Element.GRASS, AtomicInteger())
    )

    fun run() {
        randomize()

        val frame = JFrame()
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        frame.setSize(screenWidth, screenHeight)
        frame.setVisible(true)

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                step()
                countPopulation(i)
                draw()
                g.drawImage(canvas, 0, 0, screenWidth, screenHeight, this)

                if (saveImage) {
                    ImageIO.write(canvas, "png", File("/tmp/fire_water_grass_${cellDim}x${cellDim}_${i}.png"))
                }
                i++
            }
        }
        frame.add(panel)
        panel.revalidate()

        while (true) {
            panel.repaint()
        }
    }

    private fun step() {
        (0.. width - 1).forEach { x ->
            (0.. height - 1).forEach { y ->
                // vertical/horizontal
                if (x > 0) {
                    battle(fg[x - 1][y], fg[x][y])
                }
                if (x < width - 1) {
                     battle(fg[x + 1][y], fg[x][y])
                }
                if (y > 0) {
                    battle(fg[x][y - 1], fg[x][y])
                }
                if (y < height - 1) {
                    battle(fg[x][y + 1], fg[x][y])
                }
                // diagonals
                if (x > 0 && y > 0) {
                    battle(fg[x - 1][y - 1], fg[x][y])
                }
                if (x < width - 1 && y > 0) {
                    battle(fg[x + 1][y - 1], fg[x][y])
                }
                if (x > 0 && y < height - 1) {
                    battle(fg[x - 1][y + 1], fg[x][y])
                }
                if (x < width - 1 && y < height - 1) {
                    battle(fg[x + 1][y + 1], fg[x][y])
                }
            }
        }
    }

    private fun battle(defender: Cell, attacker: Cell) {
        if (defender.element == Element.NONE && attacker.element == Element.NONE) { return }

        if (defender.element == Element.FIRE && attacker.element == Element.WATER) { defender.hp-- }
        if (defender.element == Element.WATER && attacker.element == Element.GRASS) { defender.hp--  }
        if (defender.element == Element.GRASS && attacker.element == Element.FIRE) { defender.hp--  }

        // defender mutually attacking attacker
        if (mutualAttacks) {
            if (attacker.element == Element.FIRE && defender.element == Element.WATER) { attacker.hp-- }
            if (attacker.element == Element.WATER && defender.element == Element.GRASS) { attacker.hp-- }
            if (attacker.element == Element.GRASS && defender.element == Element.FIRE) { attacker.hp-- }
            if (attacker.hp <= 0 || attacker.element == Element.NONE) {
                attacker.hp = 10
                attacker.element = defender.element
            }
        }
        if (defender.hp <= 0 || defender.element == Element.NONE) {
            defender.hp = 10
            defender.element = attacker.element
        }
    }

    private fun countPopulation(i: Int) {
        Element.values().forEach { element ->
            population[element]!!.set(0)
        }
        (0.. width - 1).forEach { x ->
            (0..height - 1).forEach { y ->
                population[fg[x][y]!!.element]!!.incrementAndGet()
            }
        }

        if (printConvergenceStats) {
            val total = width * height
            println("$i, " +
                    "${population[Element.FIRE]!!.get().toFloat() / total}, " +
                    "${population[Element.WATER]!!.get().toFloat() / total}, " +
                    "${population[Element.GRASS]!!.get().toFloat() / total}")
        }
    }

    private fun draw() {
        (0.. width - 1).forEach { x ->
            (0.. height - 1).forEach { y ->
                if (cellDim == 1) {
                    canvas.setRGB(x * cellDim, y * cellDim, palette[fg[x][y].element]!!)
                } else {
                    drawRectangle(x * cellDim, y * cellDim, palette[fg[x][y].element]!!)
                }
            }
        }
    }

    private fun  drawRectangle(startX: Int, startY: Int, rgb: Int) {
        (0.. cellDim - 1).forEach { x ->
            (0.. cellDim - 1).forEach { y ->
                canvas.setRGB(startX + x, startY + y, rgb)
            }
        }
    }

    private fun randomize() {
        (0.. width - 1).forEach { x ->
            (0.. height - 1).forEach { y ->
                fg[x][y].element = generateElement()
            }
        }
    }

    private fun generateElement() = when (random.nextInt() % 4) {
                0 -> Element.WATER
                1 -> Element.FIRE
                2 -> Element.GRASS
                else -> Element.NONE
    }

}

data class Cell(var hp: Int = 10, var element: Element)

enum class Element {
    NONE,
    WATER,
    FIRE,
    GRASS
}