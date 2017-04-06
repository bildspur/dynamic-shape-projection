package ch.bildspur.dysp.io

import processing.core.PImage

/**
 * Created by cansik on 06.04.17.
 */
interface InputProvider {
    var isSetup : Boolean

    fun setup()

    fun getImage() : PImage
}