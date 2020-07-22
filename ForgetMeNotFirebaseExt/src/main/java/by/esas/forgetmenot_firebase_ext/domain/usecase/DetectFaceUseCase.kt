package by.esas.forgetmenot_firebase_ext.domain.usecase

import androidx.core.graphics.toRectF
import by.esas.forgetmenot.domain.usecase.UseCase
import by.esas.forgetmenot.model.ImageSize
import by.esas.forgetmenot.model.Landmarks
import by.esas.forgetmenot.model.Point
import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException
import by.esas.forgetmenot_firebase_ext.domain.FirebaseDetectorWrapper
import by.esas.forgetmenot_firebase_ext.model.ForgetMeNotFaceDetectorLimits
import by.esas.forgetmenot_firebase_ext.model.input.DetectFaceInput
import by.esas.forgetmenot_firebase_ext.model.result.DetectFaceResult
import by.esas.forgetmenot_firebase_ext.utils.FaceBoundsUtils
import by.esas.forgetmenot_firebase_ext.utils.area
import by.esas.forgetmenot_firebase_ext.utils.cutOut
import by.esas.forgetmenot_firebase_ext.utils.extend
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs

internal class DetectFaceUseCase(
    foregroundContext: CoroutineContext,
    private val forgetMeNotFaceDetectorLimits: ForgetMeNotFaceDetectorLimits?
) : UseCase<DetectFaceInput, DetectFaceResult>("IdRemoval", foregroundContext) {

    private val facesDetector: FirebaseDetectorWrapper

    init {
        val facesDetectorOptions = FirebaseVisionFaceDetectorOptions.Builder()
            .setMinFaceSize(0.1f)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .build()

        facesDetector =
            FirebaseDetectorWrapper(
                facesDetectorOptions
            )
    }

    override suspend fun executeOnBackground(inputParam: DetectFaceInput): DetectFaceResult {
        val detectedFaces = detectFace(inputParam.image)
        val biggestFace = getBiggestFace(detectedFaces)

        if (forgetMeNotFaceDetectorLimits != null) {
            val detectionError =
                getDetectionErrors(biggestFace, inputParam.size, forgetMeNotFaceDetectorLimits)
            if (detectionError != null) throw ForgetMeNotException(
                "Face detection errors", detectionError
            )
        }

        val faceBounds = biggestFace.boundingBox.toRectF().apply { extend(60f) }
        val faceImage = inputParam.image.bitmap.cutOut(faceBounds)
        val landmarks = getLandmarks(biggestFace)

        return DetectFaceResult(faceImage, landmarks)
    }

    private suspend fun detectFace(image: FirebaseVisionImage): List<FirebaseVisionFace> =
        suspendCoroutine {
            facesDetector.detectFacesSynchronous(image,
                onSuccessListener = { faces -> it.resume(faces) },
                onFailureListener = { e ->
                    it.resumeWithException(
                        ForgetMeNotException(
                            "Failed to detect faces",
                            ForgetMeNotError.FACE_DETECTION_FAILED, e
                        )
                    )
                })
        }

    private fun getBiggestFace(faces: List<FirebaseVisionFace>) =
        faces.maxBy { it.boundingBox.area() }
            ?: throw ForgetMeNotException("No faces detected", ForgetMeNotError.NO_FACES_DETECTED)

    private fun getLandmarks(face: FirebaseVisionFace): Landmarks {
        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)!!.position
        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)!!.position
        val mouthLeft = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT)!!.position
        val mouthRight = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT)!!.position
        val nose = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)!!.position

        return Landmarks(
            Point(leftEye.x, leftEye.y),
            Point(rightEye.x, rightEye.y),
            Point(mouthLeft.x, mouthLeft.y),
            Point(mouthRight.x, mouthRight.y),
            Point(nose.x, nose.y)
        )
    }

    private fun getDetectionErrors(
        face: FirebaseVisionFace,
        imageSize: ImageSize,
        limits: ForgetMeNotFaceDetectorLimits
    ): ForgetMeNotError? {
        val bounds = face.boundingBox.toRectF()
        val faceScreenPercent = FaceBoundsUtils.getFaceScreenPercent(bounds, imageSize)
        val faceOutOfScreenPercent = FaceBoundsUtils.boundsOutOfImagePercent(bounds, imageSize)

        return when {
            limits.faceTurnLimit != null && abs(face.headEulerAngleY) > limits.faceTurnLimit -> ForgetMeNotError.FACE_TOO_TURNED
            limits.faceTiltLimit != null && abs(face.headEulerAngleZ) > limits.faceTiltLimit -> ForgetMeNotError.FACE_TOO_TILTED
            limits.faceSizePercentMin != null && faceScreenPercent < limits.faceSizePercentMin -> ForgetMeNotError.FACE_TOO_SMALL
            limits.faceOutOfScreenPercentLimit != null && faceOutOfScreenPercent > limits.faceOutOfScreenPercentLimit -> ForgetMeNotError.FACE_OUT_OF_SCREEN
            else -> null
        }
    }
}