package ch.bildspur.dysp.tracker

import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(x : Double, y : Double, val area : Double) : Point(x, y) {
    var used = false

    var lifeTime = 0
    var isDead = true

    fun setCenter(point : Point)
    {
        this.x = point.x
        this.y = point.y
    }

    constructor(point : Point, area : Double) : this(point.x, point.y, area)
}