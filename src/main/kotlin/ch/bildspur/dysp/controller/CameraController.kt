package ch.bildspur.dysp.controller

import processing.core.PApplet
import processing.video.Capture

/**
 * Created by cansik on 07.02.17.
 */
class CameraController(internal var sketch: PApplet) {
    var cam: Capture? = null

    fun setupCamera() {
        val cameras = Capture.list()

        if (cameras.isEmpty()) {
            println("There are no cameras available for capture.")
        } else {
            println("Available cameras:");
            cameras.forEach { println(it) }

            cam = Capture(sketch, cameras[0])
            cam!!.start()
        }
    }

    fun read() {
        if (cam!!.available())
            cam!!.read()
    }


    val image: Capture
        get() = cam!!
}