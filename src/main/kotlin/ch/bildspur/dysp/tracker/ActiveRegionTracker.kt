package ch.bildspur.dysp.tracker

import ch.bildspur.dysp.vision.ConnectedComponent
import ch.fhnw.afpars.util.opencv.distance
import ch.fhnw.afpars.util.opencv.sparsePoints
import org.opencv.core.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegionTracker {
    val regions = mutableListOf<ActiveRegion>()

    var sparsing = 0.0
    var maxDelta = 100.0

    fun track(components: List<ConnectedComponent>) {
        // sparse and prepare points
        val points = components.map { it.centroid }
                .toMutableList()
                .sparsePoints(sparsing)
                .map { Point(it.map { it.x }.average(), it.map { it.y }.average()) }
                .map(::ActivePoint)

        // reset all regions
        regions.forEach { it.isDead = true }

        // create matrix
        matchNearest(points)

        // remove dead regions
        regions.removeAll { it.isDead }

        // update regions
        regions.forEach { it.lifeTime++ }

        // create new regions
        regions.addAll(points.filter { !it.used }.map { ActiveRegion(it.point) })
    }

    private fun matchNearest(points: List<ActivePoint>) {
        // create matrix (point to region)
        val distances = Array(points.size, { DoubleArray(regions.size) })

        // fill matrix O((m*n)^2)
        points.forEachIndexed { i, activePoint ->
            regions.forEachIndexed { j, activeRegion ->
                distances[i][j] = activeRegion.center.distance(activePoint.point)
            }
        }

        // match best region to point
        points.forEachIndexed { i, activePoint ->
            val minDelta = distances[i].min() ?: Double.MAX_VALUE

            if (minDelta <= maxDelta) {
                val regionIndex = distances[i].indexOf(minDelta)
                regions[regionIndex].center = activePoint.point
                regions[regionIndex].isDead = false

                activePoint.used = true
            }
        }
    }
}