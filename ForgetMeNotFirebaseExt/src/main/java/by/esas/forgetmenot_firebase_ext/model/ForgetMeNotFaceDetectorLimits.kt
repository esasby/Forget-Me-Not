package by.esas.forgetmenot_firebase_ext.model

import androidx.annotation.FloatRange

/**
 * Limits for the detection inaccuracies.
 *
 * @param faceTurnLimit Max angle of facial turn.
 * @param faceTiltLimit Max angle of head tilt.
 * @param faceSizePercentMin Min percent of the face-to-image-size proportion.
 * @param faceOutOfScreenPercentLimit Max percent of the face being out of image borders.
 */
class ForgetMeNotFaceDetectorLimits private constructor(
    val faceTurnLimit: Float?,
    val faceTiltLimit: Float?,
    val faceSizePercentMin: Float?,
    val faceOutOfScreenPercentLimit: Float?
) {

    /**
     * Builder class for [ForgetMeNotFaceDetectorLimits].
     *
     * @property faceTurnLimit Max angle of facial turn.
     * @property faceTiltLimit Max angle of head tilt.
     * @property faceSizePercentMin Min percent of the face-to-image-size proportion.
     * @property faceOutOfScreenPercentLimit Max percent of the face being out of image borders.
     */
    class Builder {
        private var faceTurnLimit: Float? = null
        private var faceTiltLimit: Float? = null
        private var faceSizePercentMin: Float? = null
        private var faceOutOfScreenPercentLimit: Float? = null

        /**
         * Set face turn limit.
         *
         * @param faceTurnLimit Max angle of facial turn.
         */
        fun setFaceTurnLimit(
            @FloatRange(from = 0.0, to = 90.0) faceTurnLimit: Float?
        ) = apply { this.faceTurnLimit = faceTurnLimit }

        /**
         * Set head tilt limit.
         *
         * @param faceTiltLimit Max angle of head tilt.
         */
        fun setFaceTiltLimit(
            @FloatRange(from = 0.0, to = 90.0) faceTiltLimit: Float?
        ) = apply { this.faceTiltLimit = faceTiltLimit }

        /**
         * Set face to screen min proportion.
         *
         * @property faceSizePercentMin Min percent of the face-to-image-size proportion.
         */
        fun setFaceSizePercentMin(
            @FloatRange(from = 0.0, to = 100.0) faceSizePercentMin: Float?
        ) = apply { this.faceSizePercentMin = faceSizePercentMin }

        /**
         * Set face out of screen percent limit.
         *
         * @property faceOutOfScreenPercentLimit Max percent of the face being out of image borders.
         */
        fun setFaceOutOfScreenPercentLimit(
            @FloatRange(from = 0.0, to = 50.0) faceOutOfScreenPercentLimit: Float
        ) = apply { this.faceOutOfScreenPercentLimit = faceOutOfScreenPercentLimit }

        /**
         * Build [ForgetMeNotFaceDetectorLimits].
         *
         * @return [ForgetMeNotFaceDetectorLimits] with currently set limits.
         */
        fun build() = ForgetMeNotFaceDetectorLimits(
            faceTurnLimit, faceTiltLimit, faceSizePercentMin, faceOutOfScreenPercentLimit
        )
    }
}