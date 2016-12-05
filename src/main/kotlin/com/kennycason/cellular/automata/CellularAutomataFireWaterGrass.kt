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
 * Fire beats Grass
 * Grass beats Water
 * Water beats Fire
 *
 * aka Rock, Scissors, Paper
 */

fun main(args: Array<String>) {
    CellularAutomataFireWaterGrass().run()
}

class CellularAutomataFireWaterGrass() {
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

    var fg = array2d(width, height, { Pokemon(hp = 10, type = Type.NONE) })

    val palette = mapOf(
            Pair(Type.NONE, Color(0x000000).rgb),
            Pair(Type.WATER, Color(0x086FFF).rgb),
            Pair(Type.FIRE, Color(0xFF6608).rgb),
            Pair(Type.GRASS, Color(0x1f862b).rgb)
    )
    val population = mapOf(
            Pair(Type.NONE, AtomicInteger()),
            Pair(Type.WATER, AtomicInteger()),
            Pair(Type.FIRE, AtomicInteger()),
            Pair(Type.GRASS, AtomicInteger())
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
                battle(get(x - 1, y), fg[x][y])
                battle(get(x + 1, y), fg[x][y])
                battle(get(x, y - 1), fg[x][y])
                battle(get(x, y + 1), fg[x][y])

                // diagonals
                battle(get(x - 1, y - 1), fg[x][y])
                battle(get(x + 1, y - 1), fg[x][y])
                battle(get(x - 1, y + 1), fg[x][y])
                battle(get(x + 1, y + 1), fg[x][y])
            }
        }
    }

    private fun get(x: Int, y: Int) = fg[(x + width) % width][(y + height) % height]


    private fun battle(defender: Pokemon, attacker: Pokemon) {
        if (defender.type == Type.NONE && attacker.type == Type.NONE) { return }

        if (defender.type == Type.FIRE && attacker.type == Type.WATER) { defender.hp-- }
        if (defender.type == Type.WATER && attacker.type == Type.GRASS) { defender.hp--  }
        if (defender.type == Type.GRASS && attacker.type == Type.FIRE) { defender.hp--  }

        // defender mutually attacking attacker
        if (mutualAttacks) {
            if (attacker.type == Type.FIRE && defender.type == Type.WATER) { attacker.hp-- }
            if (attacker.type == Type.WATER && defender.type == Type.GRASS) { attacker.hp-- }
            if (attacker.type == Type.GRASS && defender.type == Type.FIRE) { attacker.hp-- }
            if (attacker.hp <= 0 || attacker.type == Type.NONE) {
                attacker.hp = 10
                attacker.type = defender.type
            }
        }
        if (defender.hp <= 0 || defender.type == Type.NONE) {
            defender.hp = 10
            defender.type = attacker.type
        }
    }

    private fun countPopulation(i: Int) {
        population[Type.FIRE]!!.set(0)
        population[Type.WATER]!!.set(0)
        population[Type.GRASS]!!.set(0)

        (0.. width - 1).forEach { x ->
            (0..height - 1).forEach { y ->
                population[fg[x][y]!!.type]!!.incrementAndGet()
            }
        }

        if (printConvergenceStats) {
            val total = width * height
            println("$i, " +
                    "${population[Type.FIRE]!!.get().toFloat() / total}, " +
                    "${population[Type.WATER]!!.get().toFloat() / total}, " +
                    "${population[Type.GRASS]!!.get().toFloat() / total}")
        }
    }

    private fun draw() {
        (0.. width - 1).forEach { x ->
            (0.. height - 1).forEach { y ->
                if (cellDim == 1) {
                    canvas.setRGB(x * cellDim, y * cellDim, palette[fg[x][y].type]!!)
                } else {
                    drawRectangle(x * cellDim, y * cellDim, palette[fg[x][y].type]!!)
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
                fg[x][y].type = generateType()
            }
        }
    }

    private fun generateType() = Type.values()[random.nextInt(Type.values().size)]

    data class Pokemon(var hp: Int = 20,
                       var type: Type = Type.NONE)

    enum class Type {
        FIRE,
        WATER,
        GRASS,
        NONE
    }
}
