package ch.bildspur.dysp.tracker

import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(var center: Point) {
    var lifeTime = 0
    var area = 0
    var isDead = true
}