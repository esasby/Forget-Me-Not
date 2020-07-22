package by.esas.forgetmenot.domain

import android.graphics.Bitmap
import androidx.annotation.IntRange
import by.esas.forgetmenot.domain.usecase.CompletionBlock
import by.esas.forgetmenot.domain.usecase.IdentifyUseCase
import by.esas.forgetmenot.domain.usecase.IdentifyWithLandmarksUseCase
import by.esas.forgetmenot.model.Landmarks
import by.esas.forgetmenot.model.input.FacesInput
import by.esas.forgetmenot.model.input.FacesWithLandmarksInput
import by.esas.forgetmenot.model.result.IdentificationResult
import kotlin.coroutines.CoroutineContext

class ForgetMeNotIdentifier(private val options: ForgetMeNotIdentifierOptions) {

    data class ForgetMeNotIdentifierOptions(
        val foregroundContext: CoroutineContext,
        val group: String,
        @IntRange(from = 100, to = 5000)
        val normalizationSize: Int = 160
    )

    fun identify(
        faceImages: List<Bitmap>, allowNewId: Boolean = false,
        callbacks: CompletionBlock<List<IdentificationResult?>>
    ) {
        val identifierInputs =
            FacesInput(faceImages, options.group, allowNewId, options.normalizationSize)

        return IdentifyUseCase(options.foregroundContext)
            .execute(identifierInputs, callbacks)
    }

    fun identifyWithLandmarks(
        faceImages: List<Bitmap>, landmarks: List<Landmarks>, allowNewId: Boolean = false,
        callbacks: CompletionBlock<List<IdentificationResult?>>
    ) {
        val identifierInputs =
            FacesWithLandmarksInput(
                faceImages,
                landmarks,
                options.group,
                allowNewId,
                options.normalizationSize
            )

        return IdentifyWithLandmarksUseCase(options.foregroundContext)
            .execute(identifierInputs, callbacks)
    }
}

