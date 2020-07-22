package by.esas.forgetmenot.domain.usecase

import androidx.core.graphics.scale
import by.esas.forgetmenot.RecognizeWithLandmarksQuery
import by.esas.forgetmenot.model.Landmarks
import by.esas.forgetmenot.model.Point
import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException
import by.esas.forgetmenot.model.input.FacesWithLandmarksInput
import by.esas.forgetmenot.model.result.IdentificationResult
import by.esas.forgetmenot.network.ForgetMeNotClient
import by.esas.forgetmenot.type.IdentifyStatusEnum
import by.esas.forgetmenot.type.ImageBase64Input
import by.esas.forgetmenot.type.LandmarksInput
import by.esas.forgetmenot.type.PointInput
import by.esas.forgetmenot.utils.toBase64
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Response
import kotlin.coroutines.CoroutineContext

internal class IdentifyWithLandmarksUseCase(foregroundContext: CoroutineContext) :
    UseCase<FacesWithLandmarksInput, List<IdentificationResult?>>(
        "IdentificatorWithLandmarks", foregroundContext
    ) {

    override suspend fun executeOnBackground(inputParam: FacesWithLandmarksInput): List<IdentificationResult?> {
        val images = mutableListOf<ImageBase64Input>()
        val landmarks = mutableListOf<LandmarksInput>()
        val normalizationSize = inputParam.normalizationSize

        inputParam.faceImages.forEachIndexed { index, face ->
            val scaledImage = face.scale(normalizationSize, normalizationSize)
            val xScaleFactor = normalizationSize.toFloat() / face.width
            val yScaleFactor = normalizationSize.toFloat() / face.height

            val base64Image = scaledImage.toBase64()
            images.add(ImageBase64Input.builder().image(base64Image).build())

            val faceLandmarks =
                getLandmarks(inputParam.landmarks[index], xScaleFactor, yScaleFactor)
            landmarks.add(faceLandmarks)
        }

        val imagesInput = Input.optional(images)
        val landmarksInput = Input.optional(landmarks)
        val groupInput = Input.optional(inputParam.group)
        val allowNewIdInput = Input.optional(inputParam.allowNewId)

        val query =
            RecognizeWithLandmarksQuery(imagesInput, landmarksInput, groupInput, allowNewIdInput)
        val response = ForgetMeNotClient.startQuery(query)

        return getIdentificationResults(response)
    }

    private fun getLandmarks(
        landmarks: Landmarks, xScaleFactor: Float, yScaleFactor: Float
    ): LandmarksInput {
        val nosePos = getScaledLandmarkPosition(landmarks.nose, xScaleFactor, yScaleFactor)

        val leftEyePos = getScaledLandmarkPosition(landmarks.leftEye, xScaleFactor, yScaleFactor)
        val rightEyePos = getScaledLandmarkPosition(landmarks.rightEye, xScaleFactor, yScaleFactor)

        val leftMouthPos =
            getScaledLandmarkPosition(landmarks.leftMouth, xScaleFactor, yScaleFactor)
        val rightMouthPos =
            getScaledLandmarkPosition(landmarks.rightMouth, xScaleFactor, yScaleFactor)

        return LandmarksInput.builder()
            .leftEye(PointInput.builder().x(leftEyePos[0]).y(leftEyePos[1]).build())
            .rightEye(PointInput.builder().x(rightEyePos[0]).y(rightEyePos[1]).build())
            .nose(PointInput.builder().x(nosePos[0]).y(nosePos[1]).build())
            .leftMouth(PointInput.builder().x(leftMouthPos[0]).y(leftMouthPos[1]).build())
            .rightMouth(PointInput.builder().x(rightMouthPos[0]).y(rightMouthPos[1]).build())
            .build()
    }

    private fun getScaledLandmarkPosition(
        landmark: Point, xScaleFactor: Float, yScaleFactor: Float
    ): List<Int> {
        val xPosition = landmark.x * xScaleFactor
        val yPosition = landmark.y * yScaleFactor

        return listOf(xPosition.toInt(), yPosition.toInt())
    }

    private fun getIdentificationResults(response: Response<RecognizeWithLandmarksQuery.Data>): List<IdentificationResult?> {
        val data = response.data()?.identify()
        if (response.hasErrors() || data.isNullOrEmpty())
            throw ForgetMeNotException(
                "Response has no data" + response.errors(),
                ForgetMeNotError.EMPTY_RESPONSE
            )

        return data.map { result -> getIdentificationResult(result) }
    }

    private fun getIdentificationResult(response: RecognizeWithLandmarksQuery.Identify) =
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