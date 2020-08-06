package by.esas.forgetmenot_firebase_ext.model.result

import android.graphics.Bitmap
import by.esas.forgetmenot.model.Landmarks
import by.esas.forgetmenot.model.error.ForgetMeNotError

/**
 * Detection results class.
 *
 * @param faceImage Cut out image of the face.
 * @param faceLandmarks Landmarks of the face.
 * @param detectionError Errors that occurred with this face.
 */
data class DetectFaceResult internal constructor(
    val trackingId: Int,
    val faceImage: Bitmap,
    val faceLandmarks: Landmarks,
    val detectionError: ForgetMeNotError?
)