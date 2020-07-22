package by.esas.forgetmenot.domain.usecase

import androidx.core.graphics.scale
import by.esas.forgetmenot.RecognizeQuery
import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException
import by.esas.forgetmenot.model.input.FacesInput
import by.esas.forgetmenot.model.result.IdentificationResult
import by.esas.forgetmenot.network.ForgetMeNotClient
import by.esas.forgetmenot.type.IdentifyStatusEnum
import by.esas.forgetmenot.type.ImageBase64Input
import by.esas.forgetmenot.utils.toBase64
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import kotlin.coroutines.CoroutineContext

internal class IdentifyUseCase(foregroundContext: CoroutineContext) :
    UseCase<FacesInput, List<IdentificationResult?>>("Identificator", foregroundContext) {

    override suspend fun executeOnBackground(inputParam: FacesInput): List<IdentificationResult?> {
        val images = mutableListOf<ImageBase64Input>()
        val normalizationSize = inputParam.normalizationSize

        inputParam.faceImages.forEach { face ->
            val scaledImage = face.scale(normalizationSize, normalizationSize)
            val base64Image = scaledImage.toBase64()
            images.add(ImageBase64Input.builder().image(base64Image).build())
        }

        val imagesInput = Input.optional(images)
        val groupInput = Input.optional(inputParam.group)
        val allowNewIdInput = Input.optional(inputParam.allowNewId)

        val query = RecognizeQuery(imagesInput, groupInput, allowNewIdInput)
        val response = ForgetMeNotClient.startQuery(query)

        return getIdentificationResult(response)
    }

    private fun getIdentificationResult(response: Response<RecognizeQuery.Data>): List<IdentificationResult?> {
        val data = response.data()?.identify()
        if (response.hasErrors() || data.isNullOrEmpty())
            throw ForgetMeNotException(
                "Response has no data" + response.errors(),
                ForgetMeNotError.EMPTY_RESPONSE
            )

        return data.map { result -> getIdentificationResult(result) }
    }

    private fun getIdentificationResult(response: RecognizeQuery.Identify) =
        when (response.status()) {
            IdentifyStatusEnum.NEWUSER -> IdentificationResult(
                response.id() as String, 0.0f, true
            )
            IdentifyStatusEnum.OK -> IdentificationResult(
                response.candidates()!![0].id(),
                response.candidates()!![0].probability().toFloat(),
                false
            )
            else -> null
        }
}