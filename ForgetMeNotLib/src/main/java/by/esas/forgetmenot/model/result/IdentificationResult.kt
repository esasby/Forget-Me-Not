package by.esas.forgetmenot.model.result

import androidx.annotation.FloatRange

/**
 * Identification results class.
 *
 * @param id API ID of the person.
 * @param probability Probability of person having the returned ID.
 * @param isNewPerson Is person new to the API server.
 */
data class IdentificationResult internal constructor(
    val id: String,
    @FloatRange(from = 0.0, to = 1.0)
    val probability: Float,
    val isNewPerson: Boolean = false
)
