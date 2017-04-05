package ch.bildspur.dysp.tracker

import ch.bildspur.dysp.vision.ConnectedComponent
import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActivePoint(x : Double, y : Double, val component : ConnectedComponent) : Point(x, y) {
    var used = false

    constructor(point : Point, component : ConnectedComponent) : this(point.x, point.y, component)
}