package ch.bildspur.dysp.io

import org.openkinect.processing.Kinect
import processing.core.PApplet
import processing.core.PImage

/**
 * Created by cansik on 06.04.17.
 */
class KinectProvider(internal var sketch: PApplet) : InputProvider {
    lateinit var kinect: Kinect

    override var isSetup: Boolean = false

    override fun setup() {
        kinect = Kinect(sketch)
        kinect.initVideo()

        kinect.enableIR(true)

        isSetup = true
    }

    override fun getImage(): PImage {
        return kinect.videoImage
    }
}