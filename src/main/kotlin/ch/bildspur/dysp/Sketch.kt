package ch.bildspur.dysp

import ch.bildspur.dysp.controller.SyphonController
import ch.bildspur.dysp.io.*
import ch.bildspur.dysp.shape.PolygonDetector
import ch.bildspur.dysp.shape.ShapeDetector
import ch.bildspur.dysp.tracker.ActiveRegionTracker
import ch.bildspur.dysp.vision.InfraredDetector
import ch.bildspur.dysp.vision.InfraredImage
import ch.bildspur.dysp.vision.grid.GridDetector
import ch.bildspur.dysp.vision.grid.GridImage
import ch.fhnw.afpars.util.opencv.sparsePoints
import controlP5.ControlP5
import org.opencv.core.Core
import org.opencv.core.Point
import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.core.PImage
import processing.opengl.PJOGL
import processing.video.Capture
import processing.video.Movie
import kotlin.properties.Delegates

/**
 * Created by cansik on 04.02.17.
 */
class Sketch : PApplet() {
    companion object {
        @JvmStatic val FRAME_RATE = 30f

        @JvmStatic val OUTPUT_WIDTH = 500
        @JvmStatic val OUTPUT_HEIGHT = 250

        @JvmStatic val WINDOW_WIDTH = 640
        @JvmStatic val WINDOW_HEIGHT = 500

        @JvmStatic val NAME = "Dynamic Shape Detection"

        @JvmStatic var instance = PApplet()

        @JvmStatic fun map(value: Double, start1: Double, stop1: Double, start2: Double, stop2: Double): Double {
            return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
        }
    }

    val syphon = SyphonController(this)

    var fpsOverTime = 0f

    var output: PGraphics by Delegates.notNull()

    var preview: PGraphics by Delegates.notNull()

    var cp5: ControlP5 by Delegates.notNull()

    var sparsing = 0.0

    val shapeDetector : ShapeDetector = PolygonDetector()

    var tracker = ActiveRegionTracker()

    val gridDetector = GridDetector()

    var previewCreated = false

    var isGridDetection = false

    lateinit var inputProvider : InputProvider

    init {

    }

    override fun settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P2D)
        PJOGL.profile = 1
        pixelDensity = 2
    }

    override fun setup() {
        Sketch.instance = this

        smooth()
        frameRate(FRAME_RATE)

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        surface.setTitle(NAME)
        syphon.setupSyphon(NAME)

        cp5 = ControlP5(this)
        setupUI()

        // create image provider based on argument
        if(args.contains("-kinect"))
            inputProvider = KinectProvider(this)

        if(args.contains("-video"))
            inputProvider = VideoProvider(this, "basic_2rect_4circles.mov")

        if(args.contains("-grid-video"))
            inputProvider = VideoProvider(this, "dynamicShape.mov")

        if(args.contains("-grid-image"))
            inputProvider = ImageProvider(this, "lines.png")

        if(args.contains("-camera"))
            inputProvider = CameraProvider(this)

        if(args.contains("-ps3"))
            inputProvider = PS3EyeCamProvider(this)

        if(args.contains("-grid"))
            isGridDetection = true

        // setup output
        output = createGraphics(OUTPUT_WIDTH, OUTPUT_HEIGHT, PApplet.P2D)
    }

    override fun draw() {
        background(55f)

        // skip first two frames
        if (frameCount < 2) {
            text("loading image...", width / 2 - 50f, height / 2f - 50f)
            return
        }

        // setup image lazy
        if (!inputProvider.isSetup) {
            inputProvider.setup()
        }

        // skip dead frames
        if (inputProvider.getImage().width == 0) {
            text("waiting for frame...", width / 2 - 75f, height / 2f - 50f)
            return
        }

        val sourceImage = inputProvider.getImage()

        // first time create preview image
        if(!previewCreated)
        {
            preview = createGraphics(sourceImage.width, sourceImage.height, PApplet.P2D)
            output = createGraphics(sourceImage.width, sourceImage.height, PApplet.P2D)
            previewCreated = true
        }

        if(isGridDetection)
            detectMovingGrid(sourceImage)
        else
            detectMovingEdges(sourceImage)

        // draw output
        //Animator.update(output)

        // send output
        syphon.sendImageToSyphon(output)

        // paint preview
        g.imageRect(preview, 10f, 10f, 300f, 250f)
        g.imageRect(output, 320f, 10f, 300f, 250f)

        // draw text
        fill(255f)
        text("Input", 10f, 270f)
        text("Output", width - 20f - 250f, 270f)

        cp5.draw()
        drawFPS()
    }

    fun detectMovingGrid(sourceImage : PImage)
    {
        val grid = GridImage(sourceImage)
        gridDetector.detect(grid)

        preview.draw {
            it.image(sourceImage.copy(), 0f, 0f)
        }

        output.draw {
            it.background(0f)
            it.image(grid.debug, 0f, 0f)
        }
    }

    fun detectMovingEdges(sourceImage : PImage)
    {
        // analyse image
        val ti = InfraredImage(sourceImage)
        InfraredDetector.detect(ti)

        // track regions in image
        tracker.track(ti.components)

        // detect shapes based on the regions
        val shapes = shapeDetector.detectShapes(tracker.regions)

        // draw debug image
        preview.draw {
            it.image(sourceImage.copy(), 0f, 0f)

            // draw cross for component
            it.strokeWeight(1f)
            it.stroke(0f, 0f, 255f)
            it.fill(0f, 0f, 255f)

            for (points in ti.components.map { it.centroid }.toMutableList().sparsePoints(sparsing)) {
                val center = Point(points.map { it.x }.average(), points.map { it.y }.average())

                for (p in points) {
                    it.strokeWeight(1f)
                    it.stroke(0f, 0f, 255f)

                    it.cross(p.x.toFloat(), p.y.toFloat(), 5f)

                    it.strokeWeight(1f)
                    it.stroke(0f)

                    // draw line to center
                    it.line(p.x.toFloat(), p.y.toFloat(), center.x.toFloat(), center.y.toFloat())
                }

                // draw center
                it.strokeWeight(1f)
                it.stroke(255f, 0f, 0f)

                it.cross(center.x.toFloat(), center.y.toFloat(), 5f)
            }

            // draw regions
            val rectSize = 10.0f
            tracker.regions.forEachIndexed { i, activeRegion ->
                it.strokeWeight(1f)
                it.stroke(0f, 255f, 0f)
                it.noFill()
                it.rect(activeRegion.x.toFloat() - (rectSize / 2f),
                        activeRegion.y.toFloat() - (rectSize / 2f),
                        rectSize,
                        rectSize)

                it.fill(255f)
                it.textSize(20f)
                it.text("${activeRegion.lifeTime}", activeRegion.x.toFloat(), activeRegion.y.toFloat())
            }
        }

        // create output
        output.draw { g ->
            g.background(0f)
            g.fill(0f, 255f, 0f, 100f)
            g.stroke(0f, 255f, 0f)
            g.strokeWeight(3f)

            shapes.forEach { shape ->
                g.shape {
                    shape.points.forEach {
                        g.vertex(it.x.toFloat(), it.y.toFloat())
                    }
                }
            }
        }
    }

    fun drawFPS() {
        // draw fps
        fpsOverTime += frameRate
        val averageFPS = fpsOverTime / frameCount.toFloat()

        textAlign(PApplet.LEFT, PApplet.BOTTOM)
        fill(255)
        text("FPS: ${frameRate.format(2)}\nFOT: ${averageFPS.format(2)}", 10f, height - 5f)
    }

    fun setupUI() {
        val h = 400f
        val w = 20f

        cp5.addSlider("threshold")
                .setPosition(w, h)
                .setSize(120, 20)
                .setValue(InfraredDetector.threshold.toFloat())
                .setRange(0f, 255f)
                .onChange { e ->
                    InfraredDetector.threshold = e.controller.value.toDouble()
                }

        cp5.addSlider("element")
                .setPosition(w + 220, h)
                .setSize(120, 20)
                .setValue(InfraredDetector.elementSize.toFloat())
                .setRange(0f, 20f)
                .onChange { e ->
                    InfraredDetector.elementSize = e.controller.value.toInt()
                }

        cp5.addSlider("min area")
                .setPosition(w + 440, h)
                .setSize(120, 20)
                .setValue(InfraredDetector.minAreaSize.toFloat())
                .setRange(0f, 5000f)
                .onChange { e ->
                    InfraredDetector.minAreaSize = e.controller.value.toInt()
                }

        cp5.addSlider("sparsing")
                .setPosition(w, h + 25)
                .setSize(120, 20)
                .setValue(sparsing.toFloat())
                .setRange(0f, width.toFloat())
                .onChange { e ->
                    sparsing = e.controller.value.toDouble()
                    tracker.sparsing = sparsing
                }
    }

    fun captureEvent(c: Capture) {
        c.read()
    }

    fun movieEvent(m: Movie) {
        if (m.available())
            m.read()
    }
}