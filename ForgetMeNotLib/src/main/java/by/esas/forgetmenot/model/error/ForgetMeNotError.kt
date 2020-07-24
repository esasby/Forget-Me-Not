package by.esas.forgetmenot.model.error

enum class ForgetMeNotError {
    EMPTY_RESPONSE,
    NOT_INITIALIZED,

    FAILED_QUERY,
    NO_ACCESS_TOKEN,

    FACE_DETECTION_FAILED,
    NO_FACES_DETECTED,
    FACE_TOO_TURNED,
    FACE_TOO_TILTED,
    FACE_TOO_SMALL,
    FACE_OUT_OF_SCREEN,
}