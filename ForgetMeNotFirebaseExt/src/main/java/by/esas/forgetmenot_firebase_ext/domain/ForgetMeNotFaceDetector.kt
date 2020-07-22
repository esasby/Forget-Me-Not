package by.esas.forgetmenot_firebase_ext.domain

import by.esas.forgetmenot.domain.usecase.CompletionBlock
import by.esas.forgetmenot.model.ImageSize
import by.esas.forgetmenot_firebase_ext.domain.usecase.DetectFaceUseCase
import by.esas.forgetmenot_firebase_ext.model.ForgetMeNotFaceDetectorLimits
import by.esas.forgetmenot_firebase_ext.model.input.DetectFaceInput
import by.esas.forgetmenot_firebase_ext.model.result.DetectFaceResult
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlin.coroutines.CoroutineContext

class ForgetMeNotFaceDetector(private val options: ForgetMeNotFaceDetectorOptions) {

    data class ForgetMeNotFaceDetectorOptions(
        val foregroundContext: CoroutineContext,
        val forgetMeNotFaceDetectorLimits: ForgetMeNotFaceDetectorLimits? = null
    )

    fun detectFaces(
        firebaseImage: FirebaseVisionImage, imageSize: ImageSize,
        callbacks: CompletionBlock<DetectFaceResult>
    ) {
        val detectFaceInputs = DetectFaceInput(firebaseImage, imageSize)

        return DetectFaceUseCase(options.foregroundContext, options.forgetMeNotFaceDetectorLimits)
            .execute(detectFaceInputs, callbacks)
    }
}