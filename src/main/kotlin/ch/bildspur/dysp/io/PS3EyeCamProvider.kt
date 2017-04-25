package ch.bildspur.dysp.io

import com.thomasdiewald.ps3eye.PS3EyeCamera
import processing.core.PApplet
import processing.core.PImage

/**
 * Created by cansik on 25.04.17.
 */
class PS3EyeCamProvider(internal var sketch: PApplet) : InputProvider {
    lateinit var cam: PS3EyeCamera

    override var isSetup: Boolean = false

    override fun setup() {
        cam = PS3EyeCamera.getDevice(sketch)
        cam.start()

        isSetup = true
    }

    override fun getImage(): PImage {
        return cam.frame
    }
}