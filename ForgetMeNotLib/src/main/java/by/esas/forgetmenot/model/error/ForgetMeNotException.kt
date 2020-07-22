package by.esas.forgetmenot.model.error

class ForgetMeNotException(
    message: String, val errorType: ForgetMeNotError, cause: Throwable? = null
) : Exception(message, cause)