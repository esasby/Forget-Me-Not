package by.esas.forgetmenot.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

internal fun Bitmap.toBase64(): String {
    val byteArray = this.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

internal fun fromBase64(base64Str: String): Bitmap {
    val decodedBytes = Base64.decode(
        base64Str.substring(base64Str.indexOf(",") + 1),
        Base64.NO_WRAP
    )

    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

internal fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 70, stream)
    return stream.toByteArray()
}