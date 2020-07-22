package by.esas.forgetmenot_firebase_ext.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Base64
import java.io.ByteArrayOutputStream
import kotlin.math.abs

internal fun Bitmap.cutOut(bounds: RectF, margin: Int = 0): Bitmap {
    val cutX = if (bounds.left - margin > 0) bounds.left.toInt() - margin else 0
    val cutY = if (bounds.top - margin > 0) bounds.top.toInt() - margin else 0

    val cutWidth =
        if (bounds.right + margin < width) abs(bounds.right.toInt() - cutX + margin)
        else width - cutX
    val cutHeight =
        if (bounds.bottom + margin < height) abs(bounds.bottom.toInt() - cutY + margin)
        else height - cutY

    return Bitmap.createBitmap(this, cutX, cutY, cutWidth, cutHeight)
}