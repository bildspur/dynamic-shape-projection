package ch.bildspur.dysp.shape

import ch.bildspur.dysp.batch
import ch.bildspur.dysp.sortTopLeft
import ch.bildspur.dysp.tracker.ActiveRegion

/**
 * Created by cansik on 05.04.17.
 */
class PolygonDetector : ShapeDetector {

    override fun detectShapes(activeRegions: List<ActiveRegion>) {
        this.detectShapes(activeRegions, 4)
    }

    fun detectShapes(activeRegions: List<ActiveRegion>, vertexNumber : Int) {
        // take n regions based on vertex count
        val batch = activeRegions
                .sortedByDescending { it.area }
                .take(activeRegions.size / vertexNumber * vertexNumber)
                .sortTopLeft()
                .asSequence().batch(vertexNumber)
                .toMutableList()
    }
}