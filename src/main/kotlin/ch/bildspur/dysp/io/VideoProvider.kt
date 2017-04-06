package ch.bildspur.dysp.io

import processing.core.PApplet
import processing.core.PImage
import processing.video.Movie

/**
 * Created by cansik on 06.04.17.
 */
class VideoProvider(internal var sketch: PApplet, internal var fileName : String) : InputProvider {
    lateinit var movie: Movie

    override var isSetup: Boolean = false

    override fun setup() {
        movie = Movie(sketch, fileName)
        movie.loop()

        isSetup = true
    }

    override fun getImage(): PImage {
        return movie
    }
}