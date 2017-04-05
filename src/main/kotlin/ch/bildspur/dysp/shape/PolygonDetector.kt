package ch.bildspur.dysp.shape

import ch.bildspur.dysp.tracker.ActiveRegion

/**
 * Created by cansik on 05.04.17.
 */
class PolygonDetector : ShapeDetector {

    override fun detectShapes(regions: List<ActiveRegion>) {
        this.detectShapes(regions, 4)
    }

    fun detectShapes(regions: List<ActiveRegion>, vertexNumber : Int) {
        // take n regions
        val nl = regions.sortedBy { it.area }
    }
}