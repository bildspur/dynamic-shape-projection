package ch.bildspur.dysp.vision

import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
 * Created by cansik on 04.02.17.
 */
class InfraredAnalyser {
    var isRunning = false

    val input = LinkedBlockingQueue<InfraredImage>()

    val output: Queue<InfraredImage> = LinkedList<InfraredImage>()

    fun start() {
        isRunning = true

        thread {
            while (isRunning) {
                val image = input.take()
                InfraredDetector.detect(image)
                output.offer(image)
            }
        }
    }

    fun stop() {
        isRunning = false
    }
}