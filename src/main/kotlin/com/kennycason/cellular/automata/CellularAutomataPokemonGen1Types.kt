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
    CellularAutomataPokemonGen1Types().run()
}

class CellularAutomataPokemonGen1Types() {
    val random = Random()
    val screenWidth = 640
    val screenHeight = 480
    val cellDim = 2
    val width = screenWidth / cellDim
    val height = screenHeight / cellDim
    val saveImage = true
    val saveIthImage = 1
    val printConvergenceStats = true

    var canvas: BufferedImage = BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB)
    var graphics = canvas.createGraphics()

    var fg = array2d(width, height, { Pokemon() })
    var bg = array2d(width, height, { Pokemon() })

    val palette = mapOf(
            Pair(Type.NORMAL, Color(0xFFFFFF).rgb),
            Pair(Type.FIGHT, Color(0x9b2c00).rgb),
            Pair(Type.FLYING, Color(0xf8def9).rgb),
            Pair(Type.POISON, Color(0x820358).rgb),
            Pair(Type.GROUND, Color(0x7f6738).rgb),
            Pair(Type.ROCK, Color(0x6b6b6b).rgb),
            Pair(Type.BUG, Color(0x93d870).rgb),
            Pair(Type.GHOST, Color(0x46055b).rgb),
            Pair(Type.FIRE, Color(0xFF6608).rgb),
            Pair(Type.WATER, Color(0x086FFF).rgb),
            Pair(Type.GRASS, Color(0x1f962b).rgb),
            Pair(Type.ELECTRIC, Color(0xfaff72).rgb),
            Pair(Type.PSYCHIC, Color(0xc1579e).rgb),
            Pair(Type.ICE, Color(0xbdfcfb).rgb),
            Pair(Type.DRAGON, Color(0x33aa9c).rgb),
            Pair(Type.NONE, Color(0x000000).rgb)
    )
    val population: Map<Type, AtomicInteger> = Type.values()
            .map { type -> Pair(type, AtomicInteger())}
            .toMap()

//    val immuneFrom: Map<Type, Set<Type>> = emptyMap()
    val immuneFrom = mapOf(
            Pair(Type.NORMAL, setOf(Type.GHOST)),
            Pair(Type.FLYING, setOf(Type.GROUND)),
            Pair(Type.GROUND, setOf(Type.ELECTRIC)),
            Pair(Type.GHOST, setOf(Type.NORMAL, Type.FIGHT)),
            Pair(Type.PSYCHIC, setOf(Type.GHOST))
    )

    val doubleDamageTo = mapOf(
            Pair(Type.FIGHT, setOf(Type.NORMAL, Type.ROCK, Type.ICE)),
            Pair(Type.FLYING, setOf(Type.FIGHT, Type.BUG, Type.GRASS)),
            Pair(Type.POISON, setOf(Type.BUG, Type.GRASS)),
            Pair(Type.GROUND, setOf(Type.POISON, Type.ROCK, Type.FIRE, Type.ELECTRIC)),
            Pair(Type.ROCK, setOf(Type.FLYING, Type.BUG, Type.FIRE, Type.ICE)),
            Pair(Type.BUG, setOf(Type.POISON, Type.GRASS, Type.PSYCHIC)),
            Pair(Type.GHOST, setOf(Type.GHOST)),
            Pair(Type.FIRE, setOf(Type.BUG, Type.GRASS, Type.ICE)),
            Pair(Type.WATER, setOf(Type.GROUND, Type.ROCK, Type.FIRE)),
            Pair(Type.GRASS, setOf(Type.GROUND, Type.ROCK, Type.WATER)),
            Pair(Type.ELECTRIC, setOf(Type.FLYING, Type.WATER)),
            Pair(Type.PSYCHIC, setOf(Type.FIGHT, Type.POISON)),
            Pair(Type.ICE, setOf(Type.FLYING, Type.GROUND, Type.GRASS, Type.DRAGON)),
            Pair(Type.DRAGON, setOf(Type.DRAGON))
    )

    val halfDamageTo = mapOf(
            Pair(Type.NORMAL, setOf(Type.ROCK)),
            Pair(Type.FIGHT, setOf(Type.FLYING, Type.POISON, Type.BUG, Type.PSYCHIC)),
            Pair(Type.FLYING, setOf(Type.ROCK, Type.ELECTRIC)),
            Pair(Type.POISON, setOf(Type.POISON, Type.GROUND, Type.ROCK, Type.GHOST)),
            Pair(Type.GROUND, setOf(Type.BUG, Type.GRASS)),
            Pair(Type.ROCK, setOf(Type.FIGHT, Type.GROUND)),
            Pair(Type.BUG, setOf(Type.FIGHT, Type.FLYING, Type.GHOST, Type.FIRE)),
            Pair(Type.FIRE, setOf(Type.ROCK, Type.FIRE, Type.WATER, Type.DRAGON)),
            Pair(Type.WATER, setOf(Type.WATER, Type.GRASS, Type.DRAGON)),
            Pair(Type.GRASS, setOf(Type.FLYING, Type.POISON, Type.BUG, Type.FIRE, Type.GRASS, Type.DRAGON)),
            Pair(Type.ELECTRIC, setOf(Type.GRASS, Type.ELECTRIC, Type.DRAGON)),
            Pair(Type.PSYCHIC, setOf(Type.PSYCHIC)),
            Pair(Type.ICE, setOf(Type.WATER, Type.ICE))
    )

    fun run() {
        randomize()

        val frame = JFrame()
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        frame.setSize(screenWidth, screenHeight)
        frame.setVisible(true)

        if (printConvergenceStats) {
            print("Step, ")
            Type.values().forEach { type ->
                print("$type, ")
            }
            println()
        }
        var i = 0
        val panel = object: JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                step()
                countPopulation(i)
                draw()
                g.drawImage(canvas, 0, 0, screenWidth, screenHeight, this)

                if (saveImage && i % saveIthImage == 0) {
                    ImageIO.write(canvas, "png", File("/tmp/pokemon_${cellDim}x${cellDim}_${i}.png"))
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

    private fun battle(defender: Pokemon, attacker: Pokemon) {
        if (defender.type == Type.NONE && attacker.type == Type.NONE) { return }

        val damage = getDamage(defender, attacker)

        defender.hp -= damage
        if (defender.hp <= 0 || defender.type == Type.NONE) {
            defender.hp = 20
            defender.type = attacker.type
        }
    }

    private fun getDamage(defender: Pokemon, attacker: Pokemon): Int {
         if (immuneFrom.containsKey(defender.type)
                 && immuneFrom[defender.type]!!.contains(attacker.type)) {
             return 0
         }
        if (doubleDamageTo.containsKey(attacker.type)
                && doubleDamageTo[attacker.type]!!.contains(defender.type)) {
            return 4 // double base damage
        }
        if (halfDamageTo.containsKey(attacker.type)
                && halfDamageTo[attacker.type]!!.contains(defender.type)) {
            return 2 // base damage
        }
        return 1 // 1/2 base damage
    }

    private fun countPopulation(i: Int) {
        Type.values().forEach { type ->
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
            Type.values().forEach { type ->
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

    private fun generateType(): Type {
        return Type.values()[random.nextInt(Type.values().size)]
    }

    data class Pokemon(var hp: Int = 20,
                       var type: Type = Type.NONE)

    enum class Type {
        NORMAL,
        FIGHT,
        FLYING,
        POISON,
        GROUND,
        ROCK,
        BUG,
        GHOST,
        FIRE,
        WATER,
        GRASS,
        ELECTRIC,
        PSYCHIC,
        ICE,
        DRAGON,
        NONE
    }

}