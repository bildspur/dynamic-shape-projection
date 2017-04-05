package ch.bildspur.dysp.vision

import processing.core.PImage

/**
 * Created by cansik on 04.02.17.
 */
class ThermalImage {
    val input: PImage
    val components = mutableListOf<ConnectedComponent>()

    constructor(input: PImage) {
        this.input = input
    }
}