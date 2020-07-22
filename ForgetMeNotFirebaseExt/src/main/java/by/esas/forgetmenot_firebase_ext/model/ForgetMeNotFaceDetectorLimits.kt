package by.esas.forgetmenot_firebase_ext.model

import androidx.annotation.FloatRange

class ForgetMeNotFaceDetectorLimits private constructor(
    val faceTurnLimit: Float?,
    val faceTiltLimit: Float?,
    val faceSizePercentMin: Float?,
    val faceOutOfScreenPercentLimit: Float?
) {
    class Builder {
        private var faceTurnLimit: Float? = null
        private var faceTiltLimit: Float? = null
        private var faceSizePercentMin: Float? = null
        private var faceOutOfScreenPercentLimit: Float? = null

        fun setFaceTurnLimit(
            @FloatRange(from = 0.0, to = 90.0) faceTurnLimit: Float?
        ) = apply { this.faceTurnLimit = faceTurnLimit }

        fun setFaceTiltLimit(
            @FloatRange(from = 0.0, to = 90.0) faceTiltLimit: Float?
        ) = apply { this.faceTiltLimit = faceTiltLimit }

        fun setFaceSizePercentMin(
            @FloatRange(from = 0.0, to = 100.0) faceSizePercentMin: Float?
        ) = apply { this.faceSizePercentMin = faceSizePercentMin }

        fun setFaceOutOfScreenPercentLimit(
            @FloatRange(from = 0.0, to = 50.0) faceOutOfScreenPercentLimit: Float
        ) = apply { this.faceOutOfScreenPercentLimit = faceOutOfScreenPercentLimit }

        fun build() = ForgetMeNotFaceDetectorLimits(
            faceTurnLimit, faceTiltLimit, faceSizePercentMin, faceOutOfScreenPercentLimit
        )
    }
}