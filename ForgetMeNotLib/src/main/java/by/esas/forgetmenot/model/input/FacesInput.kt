package by.esas.forgetmenot.model.input

import android.graphics.Bitmap
import androidx.annotation.IntRange

internal data class FacesInput(
    val faceImages: List<Bitmap>,
    val group: String,
    val allowNewId: Boolean = false,
    @IntRange(from = 100, to = 5000)
    val normalizationSize: Int = 160
)