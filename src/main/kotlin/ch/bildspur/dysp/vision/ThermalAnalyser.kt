package ch.bildspur.dysp.vision

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
 * Created by cansik on 04.02.17.
 */
class ThermalAnalyser {
    var isRunning = false

    val input = LinkedBlockingQueue<ThermalImage>()

    val output: Queue<ThermalImage> = LinkedList<ThermalImage>()

    fun start() {
        isRunning = true

        thread {
            while (isRunning) {
                val image = input.take()
                ThermalDetector.detect(image)
                output.offer(image)
            }
        }
    }

    fun stop() {
        isRunning = false
    }
}