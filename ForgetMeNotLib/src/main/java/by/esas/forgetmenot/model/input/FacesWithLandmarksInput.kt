package by.esas.forgetmenot.model.input

import android.graphics.Bitmap
import androidx.annotation.IntRange
import by.esas.forgetmenot.model.Landmarks

internal data class FacesWithLandmarksInput(
    val faceImages: List<Bitmap>,
    val landmarks: List<Landmarks>,
    val group: String,
    val allowNewId: Boolean = false,
    @IntRange(from = 100, to = 5000)
    val normalizationSize: Int = 160
)
