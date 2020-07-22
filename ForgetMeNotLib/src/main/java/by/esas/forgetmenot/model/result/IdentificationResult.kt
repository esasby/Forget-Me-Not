package by.esas.forgetmenot.model.result

import androidx.annotation.FloatRange

data class IdentificationResult internal constructor(
    val id: String,
    @FloatRange(from = 0.0, to = 1.0)
    val probability: Float,
    val isNewPerson: Boolean = false
)
