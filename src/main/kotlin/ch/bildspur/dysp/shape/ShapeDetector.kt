package ch.bildspur.dysp.shape

import ch.bildspur.dysp.tracker.ActiveRegion

/**
 * Created by cansik on 05.04.17.
 */
interface ShapeDetector {

    fun detectShapes(regions : List<ActiveRegion>)
}