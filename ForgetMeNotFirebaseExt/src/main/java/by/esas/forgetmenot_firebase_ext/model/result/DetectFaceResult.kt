package by.esas.forgetmenot_firebase_ext.model.result

import android.graphics.Bitmap
import by.esas.forgetmenot.model.Landmarks

/**
 * Detection results class.
 *
 * @param faceImage Cut out image of the face.
 * @param faceLandmarks Landmarks of the face.
 */
data class DetectFaceResult internal constructor(
    val faceImage: Bitmap,
    val faceLandmarks: Landmarks
)