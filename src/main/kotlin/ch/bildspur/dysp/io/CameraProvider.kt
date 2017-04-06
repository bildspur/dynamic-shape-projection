package ch.bildspur.dysp.io

import processing.core.PApplet
import processing.core.PImage
import processing.video.Capture

/**
 * Created by cansik on 06.04.17.
 */
class CameraProvider(internal var sketch: PApplet) : InputProvider
{
    override var isSetup: Boolean = false

    lateinit var cam: Capture

    override fun setup() {
        val cameras = Capture.list()

        if (cameras.isEmpty()) {
            println("There are no cameras available for capture.")
        } else {
            println("Available cameras:");
            cameras.forEach { println(it) }

            cam = Capture(sketch, cameras[0])
            cam.start()

            isSetup = true
        }
    }

    override fun getImage(): PImage {
        return cam
    }

}