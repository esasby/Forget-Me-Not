package by.esas.forgetmenot_firebase_ext.utils

import android.graphics.RectF
import by.esas.forgetmenot.model.ImageSize
import java.util.*
import kotlin.math.ln

internal object FaceBoundsUtils {

    fun boundsOutOfImagePercent(bounds: RectF, imageSize: ImageSize): Float {
        val outTop = 0 - bounds.top
        val outBottom = bounds.bottom - imageSize.height
        val outLeft = 0 - bounds.left
        val outRight = bounds.right - imageSize.width

        return Collections.max(setOf(outTop, outBottom, outLeft, outRight)) / bounds.width()
    }

    fun getFaceScreenPercent(faceBounds: RectF, screenSize: ImageSize): Double {
        val actualPercent = faceBounds.area() / (screenSize.width * screenSize.height) * 100
        return (100 / ln(101.0)) * ln(actualPercent + 1)
    }
}