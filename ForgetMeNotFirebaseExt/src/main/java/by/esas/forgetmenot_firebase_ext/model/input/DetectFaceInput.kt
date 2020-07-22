package by.esas.forgetmenot_firebase_ext.model.input

import by.esas.forgetmenot.model.ImageSize
import com.google.firebase.ml.vision.common.FirebaseVisionImage

internal data class DetectFaceInput(val image: FirebaseVisionImage,val size: ImageSize)