package by.esas.forgetmenot_firebase_ext.domain

import by.esas.forgetmenot.domain.usecase.CompletionBlock
import by.esas.forgetmenot.model.ImageSize
import by.esas.forgetmenot_firebase_ext.domain.usecase.DetectFaceUseCase
import by.esas.forgetmenot_firebase_ext.model.ForgetMeNotFaceDetectorLimits
import by.esas.forgetmenot_firebase_ext.model.input.DetectFaceInput
import by.esas.forgetmenot_firebase_ext.model.result.DetectFaceResult
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlin.coroutines.CoroutineContext

/**
 * Class used for detecting faces on pictures via Firebase ML Kit.
 *
 * @param options Detector options.
 */
class ForgetMeNotFaceDetector(private val options: ForgetMeNotFaceDetectorOptions) {

    /**
     * Options for the detector.
     *
     * @param foregroundContext Main context of the identifier.
     * @param forgetMeNotFaceDetectorLimits Detection inaccuracies limits.
     */
    data class ForgetMeNotFaceDetectorOptions(
        val foregroundContext: CoroutineContext,
        val forgetMeNotFaceDetectorLimits: ForgetMeNotFaceDetectorLimits? = null
    )

    /**
     * Detect faces on image.
     *
     * @param firebaseImage Facial image.
     * @param imageSize Facial image size.
     * @param callbacks Callbacks for completion of detection.
     */
    fun detectFaces(
        firebaseImage: FirebaseVisionImage, imageSize: ImageSize,
        callbacks: CompletionBlock<DetectFaceResult>
    ) {
        val detectFaceInputs = DetectFaceInput(firebaseImage, imageSize)

        return DetectFaceUseCase(options.foregroundContext, options.forgetMeNotFaceDetectorLimits)
            .execute(detectFaceInputs, callbacks)
    }
}