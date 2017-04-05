package ch.bildspur.dysp.tracker

import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(var center: ActivePoint) {
    val area : Double = center.area
    var lifeTime = 0
    var isDead = true
}