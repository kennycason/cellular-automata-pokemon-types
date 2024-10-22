package com.kennycason.cellular.automata

import java.awt.image.BufferedImage
import kotlin.math.abs

class ImageDifference(val stepSize: Int) {
    fun compare(bi: BufferedImage, bi2: BufferedImage): Double {
        var error = 0.0
        (0..< bi.width step stepSize).forEach{ x ->
            (0..< bi.height step stepSize).forEach{ y ->
                val rgb = bi.getRGB(x, y)
                val rgb2 = bi2.getRGB(x, y)
                error += abs((rgb and 0xFF) - (rgb2 and 0xFF))
                error += abs(((rgb shr 8) and 0xFF) - ((rgb2 shr 8) and 0xFF))
                error += abs(((rgb shr 16) and 0xFF) - ((rgb2 shr 16) and 0xFF))
            }
        }
        return error
    }

    fun compareNormalized(bi: BufferedImage, bi2: BufferedImage): Double {
        var error = 0.0
        var totalPixels = 0

        (0 until bi.width step stepSize).forEach { x ->
            (0 until bi.height step stepSize).forEach { y ->
                val rgb = bi.getRGB(x, y)
                val rgb2 = bi2.getRGB(x, y)

                // Calculate absolute difference for each color channel (R, G, B)
                error += abs((rgb and 0xFF) - (rgb2 and 0xFF))   // Blue channel
                error += abs(((rgb shr 8) and 0xFF) - ((rgb2 shr 8) and 0xFF))  // Green channel
                error += abs(((rgb shr 16) and 0xFF) - ((rgb2 shr 16) and 0xFF))  // Red channel

                totalPixels++
            }
        }

        // Maximum possible error per pixel is 765 (255 per channel * 3 channels)
        val maxError = totalPixels * 765

        // Return normalized error (between 0 and 1)
        return error / maxError
    }
}