package ch.bildspur.dysp.vision

import ch.bildspur.dysp.*
import org.opencv.core.CvType
import org.opencv.core.Mat


/**
 * Created by cansik on 04.02.17.
 */
object ThermalDetector {

    var threshold = 200.0
    var elementSize = 5
    var minAreaSize = 875

    init {

    }

    fun detect(ti: ThermalImage) {
        val image = Mat(ti.input.height, ti.input.width, CvType.CV_8UC4)

        ti.input.toMat(image)
        val gray = image.copy()
        gray.gray()
        image.toBGRA(image)

        // threshold
        // todo: implement intelligent threshold
        gray.threshold(threshold)

        // remove small parts
        gray.erode(elementSize)
        gray.dilate(elementSize)

        // detect areas (used-component analysis)
        val nativeComponents = gray.connectedComponentsWithStats()
        val components = nativeComponents.getConnectedComponents().filter { it.area >= minAreaSize && it.label != 0 }

        ti.components.addAll(components)

        // free memory
        gray.release()
        image.release()
        nativeComponents.release()
    }
}