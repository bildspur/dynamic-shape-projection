package ch.bildspur.dysp.io

import processing.core.PApplet
import processing.core.PImage

class ImageProvider(internal var sketch: PApplet, internal var fileName : String) : InputProvider {
    lateinit var img: PImage

    override var isSetup: Boolean = false

    override fun setup() {
        img = sketch.loadImage(fileName)

        isSetup = true
    }

    override fun getImage(): PImage {
        return img
    }
}