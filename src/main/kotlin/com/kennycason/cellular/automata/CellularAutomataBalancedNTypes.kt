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
 * Created by kenny on 11/24/16.
 *
 * Programmed to be generic enough such that we can easily and dynamically add Types.
 * The world wraps around
 */
fun main(args: Array<String>) {
    CellularAutomataBalancedNTypes().run()
}

class CellularAutomataBalancedNTypes() {
    val random = Random()
    val screenWidth = 640
    val screenHeight = 480
    val cellDim = 2
    val width = screenWidth / cellDim
    val height = screenHeight / cellDim
    val saveImage = false
    val saveIthImage = 10
    val printConvergenceStats = true

    var canvas: BufferedImage = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
    var graphics = canvas.createGraphics()

    var fg = array2d(width, height, { Cell(hp = 10, type = SimpleType.A) })
    var bg = array2d(width, height, { Cell(hp = 10, type = SimpleType.A) })

    val palette: MutableMap<SimpleType, Int> = mutableMapOf()

    val population: Map<SimpleType, AtomicInteger> = SimpleType.values()
            .map { type -> Pair(type, AtomicInteger())}
            .toMap()

    val weakTo: MutableMap<SimpleType, SimpleType> = mutableMapOf()

    fun run() {
        // build weakToMap
        SimpleType.values().forEachIndexed { i, type ->
            if (i == SimpleType.values().size - 1) { // handle overflow
                weakTo[type] = SimpleType.values()[0]
            } else {
                weakTo[type] = SimpleType.values()[i + 1]
            }
        }

        // build colors
        val step = 0xFFFFFF / SimpleType.values().size
        SimpleType.values().forEachIndexed { i, type ->
            palette[type] = Color(i * step).rgb
        }

        // print column headers
        if (printConvergenceStats) {
            print("Step, ")
            SimpleType.values().forEach { type ->
                print("$type, ")
            }
            println()
        }

        randomize()

        val frame = JFrame()
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        frame.setSize(screenWidth, screenHeight)
        frame.setVisible(true)

        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                countPopulation(i)
                step()
                draw()
                g.drawImage(canvas, 0, 0, screenWidth, screenHeight, this)

                if (saveImage && i % saveIthImage == 0) {
                    ImageIO.write(canvas, "png", File("/tmp/balanced_n_types_${cellDim}x${cellDim}_${i}.png"))
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
                } else {
                    battle(fg[width - 1][y], fg[x][y])
                }

                if (x < width - 1) {
                     battle(fg[x + 1][y], fg[x][y])
                } else {
                    battle(fg[0][y], fg[x][y])
                }

                if (y > 0) {
                    battle(fg[x][y - 1], fg[x][y])
                } else {
                    battle(fg[x][height - 1], fg[x][y])
                }

                if (y < height - 1) {
                    battle(fg[x][y + 1], fg[x][y])
                } else {
                    battle(fg[x][0], fg[x][y])
                }

                // diagonals
                if (x > 0 && y > 0) {
                    battle(fg[x - 1][y - 1], fg[x][y])
                } else {
                    battle(fg[width - 1][height - 1], fg[x][y])
                }

                if (x < width - 1 && y > 0) {
                    battle(fg[x + 1][y - 1], fg[x][y])
                } else {
                    battle(fg[0][height - 1], fg[x][y])
                }

                if (x > 0 && y < height - 1) {
                    battle(fg[x - 1][y + 1], fg[x][y])
                } else {
                    battle(fg[width - 1][0], fg[x][y])
                }

                if (x < width - 1 && y < height - 1) {
                    battle(fg[x + 1][y + 1], fg[x][y])
                } else {
                    battle(fg[0][0], fg[x][y])
                }
            }
        }
    }

    private fun battle(defender: Cell, attacker: Cell) {
//        if (attacker.type == SimpleType.NONE) { return }
//        if (defender.type == SimpleType.NONE) {
//            defender.hp = 10
//            defender.type = attacker.type
//            return
//        }

        if (weakTo[defender.type] == attacker.type) { defender.hp--  }

        if (defender.hp <= 0) {
            defender.hp = 10
            defender.type = attacker.type
        }
    }

    private fun countPopulation(i: Int) {
        SimpleType.values().forEach { type ->
            population[type]!!.set(0)
        }
        (0.. width - 1).forEach { x ->
            (0..height - 1).forEach { y ->
                population[fg[x][y]!!.type]!!.incrementAndGet()
            }
        }

        if (printConvergenceStats) {
            val total = width * height

            val stringBuilder = StringBuilder()
            stringBuilder.append(i)
            stringBuilder.append((", "))
            SimpleType.values().forEach { type ->
                stringBuilder.append(population[type]!!.get().toFloat() / total)
                stringBuilder.append((", "))
            }
            println(stringBuilder.toString())
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

    private fun generateType(): SimpleType {
//        if (random.nextInt(100) > 1) {
//            return SimpleType.NONE
//        }
        return SimpleType.values()[random.nextInt(SimpleType.values().size)]
    }

    // data structures

    data class Cell(var hp: Int = 10, var type: SimpleType)

    enum class SimpleType {
 //       NONE,
        A,
        B,
//        C,
//        D,
//        E,
//        F,
//        G,
//        H,
//        I,
//        J,
//        K,
//        L,
//        M,
//        N,
//        O,
//        P,
//        Q,
//        R,
//        S,
//        T,
//        U,
//        V,
//        W,
//        X,
//        Y,
//        Z
    }
}

