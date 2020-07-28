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


/**
 * Class used for identifying people (e.g. get their API unique IDs).
 *
 * @param options Identifier options.
 */
class ForgetMeNotIdentifier(private val options: ForgetMeNotIdentifierOptions) {

    /**
     * Options for the identifier.
     *
     * @param foregroundContext Main context of the identifier.
     * @param group Server group for identification.
     * @param normalizationSize Size, to which image is compressed before processing.
     */
    data class ForgetMeNotIdentifierOptions(
        val foregroundContext: CoroutineContext,
        val group: String,
        @IntRange(from = 100, to = 5000)
        val normalizationSize: Int = 160
    )

    /**
     * Identifying people on image (e.g. get their API unique IDs).
     *
     * @param faceImages List of facial bitmaps.
     * @param allowNewId Allow creation of new IDs on the server.
     * @param callbacks Callbacks for completion of identification.
     */
    fun identify(
        faceImages: List<Bitmap>, allowNewId: Boolean = false,
        callbacks: CompletionBlock<List<IdentificationResult?>>
    ) {
        val identifierInputs =
            FacesInput(faceImages, options.group, allowNewId, options.normalizationSize)

        return IdentifyUseCase(options.foregroundContext)
            .execute(identifierInputs, callbacks)
    }

    /**
     * Identifying people on image (e.g. get their API unique IDs), using landmark positions.
     *
     * @param faceImages List of facial bitmaps.
     * @param landmarks List of facial landmarks.
     * @param allowNewId Allow creation of new IDs on the server.
     * @param callbacks Callbacks for completion of identification.
     */
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

