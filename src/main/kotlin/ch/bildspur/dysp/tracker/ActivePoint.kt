package ch.bildspur.dysp.tracker

import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActivePoint(x : Double, y : Double, val area : Double) : Point(x, y) {
    var used = false

    constructor(point : Point, area : Double) : this(point.x, point.y, area)
}