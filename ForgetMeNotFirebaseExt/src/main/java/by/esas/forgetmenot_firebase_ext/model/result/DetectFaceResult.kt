package by.esas.forgetmenot_firebase_ext.model.result

import android.graphics.Bitmap
import by.esas.forgetmenot.model.Landmarks

data class DetectFaceResult internal constructor(
    val faceImage: Bitmap,
    val faceLandmarks: Landmarks
)
