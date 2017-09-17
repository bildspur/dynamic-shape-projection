package ch.bildspur.dysp.vision.grid

import ch.bildspur.dysp.*
import org.opencv.core.CvType
import org.opencv.core.Mat

class GridDetector {

    var threshold = 100.0
    var elementSize = 0

    fun detect(source: GridImage) {
        val image = Mat(source.image.height, source.image.width, CvType.CV_8UC4)

        source.image.toMat(image)
        val gray = image.copy()
        gray.gray()
        image.toBGRA(image)

        // threshold
        gray.threshold(threshold)

        // remove small parts
        gray.erode(elementSize)
        gray.dilate(elementSize)

        gray.toPImage(source.debug)

        // free memory
        gray.release()
        image.release()
    }
}