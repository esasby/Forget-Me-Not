package by.esas.forgetmenot.model.error

/**
 * Internal exceptions type.
 *
 * @param message Error description.
 * @param errorType Error type from enum.
 * @param cause Cause of the exception.
 */
class ForgetMeNotException(
    message: String, val errorType: ForgetMeNotError, cause: Throwable? = null
) : Exception(message, cause)