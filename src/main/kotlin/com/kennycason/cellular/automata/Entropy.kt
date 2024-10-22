package com.kennycason.cellular.automata

import java.awt.image.BufferedImage
import kotlin.math.ln
import kotlin.math.log2

object Entropy {
    fun calculateNormalizedEntropy(image: BufferedImage): Double {
        val entropy = calculateEntropy(image)
        val totalPixels = image.width * image.height
        val maxEntropy = log2(totalPixels.toDouble())

        // normalize entropy to a 0.0 - 1.0 scale
        return entropy / maxEntropy
    }

    fun calculateEntropy(image: BufferedImage): Double {
        val colorCountMap = mutableMapOf<Int, Int>()
        val totalPixels = image.width * image.height

        // Count occurrences of each color
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val rgb = image.getRGB(x, y)
                colorCountMap[rgb] = colorCountMap.getOrDefault(rgb, 0) + 1
            }
        }

        // calculate the entropy
        var entropy = 0.0
        for ((_, count) in colorCountMap) {
            val probability = count.toDouble() / totalPixels
            entropy -= probability * log2(probability)
        }
        return entropy
    }

    fun calculateColorHistogram(image: BufferedImage): Map<Int, Int> {
        val histogram = mutableMapOf<Int, Int>()
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val color = image.getRGB(x, y)
                histogram[color] = histogram.getOrDefault(color, 0) + 1
            }
        }
        return histogram
    }



}