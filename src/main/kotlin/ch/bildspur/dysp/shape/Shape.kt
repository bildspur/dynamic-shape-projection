package ch.bildspur.dysp.shape

import org.opencv.core.Point

/**
 * Created by cansik on 05.04.17.
 */
class Shape(vararg val points : Point) {
    
    /**
     * PNPoly Method:
     * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html#The%20C%20Code
     */
    fun contains(p: Point): Boolean {
        var inside = false
        var i = 0
        var j = points.size - 1
        while (i < points.size) {
            if (points[i].y > p.y != points[j].y > p.y
                    && p.x < (points[j].x - points[i].x)
                    * (p.y - points[i].y)
                    / (points[j].y - points[i].y) + points[i].x)
                inside = !inside
            j = i++
        }
        return inside
    }
}