package ch.bildspur.dysp

import processing.core.PApplet

/**
 * Created by cansik on 04.02.17.
 */
class Main {
    val sketch = Sketch()

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val main = Main()
            PApplet.runSketch(arrayOf("Sketch "), main.sketch)
        }
    }
}