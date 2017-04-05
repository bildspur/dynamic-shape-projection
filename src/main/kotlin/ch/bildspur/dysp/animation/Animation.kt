package ch.bildspur.dysp.animation

import processing.core.PGraphics

/**
 * Created by cansik on 13.02.17.
 */
class Animation(var length: Int = 0, val render: (animation: Animation, g: PGraphics) -> Unit, var running: Boolean = true) {
    val finished: Boolean
        get() = lifeTime >= length

    var lifeTime = 0

    fun start() {
        running = true
    }

    fun stop() {
        running = false
    }

    fun reset() {
        lifeTime = 0
    }
}