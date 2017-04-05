package ch.bildspur.dysp.shape

import ch.bildspur.dysp.batch
import ch.bildspur.dysp.convexHull
import ch.bildspur.dysp.sortTopLeft
import ch.bildspur.dysp.tracker.ActiveRegion
import org.opencv.core.MatOfPoint

/**
 * Created by cansik on 05.04.17.
 */
class PolygonDetector : ShapeDetector {

    override fun detectShapes(activeRegions: List<ActiveRegion>): List<Shape> {
        return this.detectShapes(activeRegions, 4)
    }

    fun detectShapes(activeRegions: List<ActiveRegion>, vertexNumber : Int): List<Shape> {
        // take n regions based on vertex count
        val batch = activeRegions
                .sortedByDescending { it.area }
                .take(activeRegions.size / vertexNumber * vertexNumber)
                .sortTopLeft()
                .asSequence().batch(vertexNumber)
                .toMutableList()

        // create hull and return shapes
        return batch.map { group ->
            val hull = MatOfPoint(*group.map { it }.toTypedArray()).convexHull(clockwise = true)
            Shape(*hull.toArray().map { group[it] }.toTypedArray())
        }
    }
}