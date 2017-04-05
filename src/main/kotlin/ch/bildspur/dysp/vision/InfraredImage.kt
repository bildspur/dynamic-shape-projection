package ch.bildspur.dysp.vision

import processing.core.PImage

/**
 * Created by cansik on 04.02.17.
 */
class InfraredImage {
    val input: PImage
    val components = mutableListOf<ConnectedComponent>()

    constructor(input: PImage) {
        this.input = input
    }
}