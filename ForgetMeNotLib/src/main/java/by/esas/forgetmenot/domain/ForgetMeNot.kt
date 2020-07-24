package by.esas.forgetmenot.domain

import by.esas.forgetmenot.model.error.ForgetMeNotError
import by.esas.forgetmenot.model.error.ForgetMeNotException

object ForgetMeNot {
    internal var faceApiUrl: String? = null
        private set

    internal var faceApiSecret: String? = null
        private set

    internal var faceApiTokenUrl: String? = null
        private set

    fun initialize(faceApiUrl: String, faceApiSecret: String, faceApiTokenUrl: String) {
        this.faceApiUrl = faceApiUrl
        this.faceApiSecret = faceApiSecret
        this.faceApiTokenUrl = faceApiTokenUrl
    }

    internal fun checkInitialization() {
        if (faceApiUrl == null)
            throw ForgetMeNotException(
                "Face Api Url is null. Did you forget to call ForgetMeNot.initialize()?",
                ForgetMeNotError.NOT_INITIALIZED
            )

        if (faceApiSecret == null)
            throw ForgetMeNotException(
                "Face Api Secret is null. Did you forget to call ForgetMeNot.initialize()?",
                ForgetMeNotError.NOT_INITIALIZED
            )

        if (faceApiTokenUrl == null)
            throw ForgetMeNotException(
                "Face Api Token Url is null. Did you forget to call ForgetMeNot.initialize()?",
                ForgetMeNotError.NOT_INITIALIZED
            )
    }
}