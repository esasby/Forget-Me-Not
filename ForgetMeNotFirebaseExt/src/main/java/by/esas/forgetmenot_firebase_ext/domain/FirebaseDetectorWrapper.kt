package by.esas.forgetmenot_firebase_ext.domain

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

internal class FirebaseDetectorWrapper(options: FirebaseVisionFaceDetectorOptions) {

    private val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

    fun detectFacesSynchronous(
        firebaseImage: FirebaseVisionImage,
        onSuccessListener: (faces: List<FirebaseVisionFace>) -> Unit,
        onFailureListener: (e: Exception) -> Unit = ::failedToProcessImage
    ) {
        try {
            Tasks.await(detectFacesAsyncTask(firebaseImage, onSuccessListener, onFailureListener))
        } catch (e: Exception) {
            onFailureListener(e)
        }
    }

    fun detectFacesAsyncTask(
        firebaseImage: FirebaseVisionImage,
        onSuccessListener: (List<FirebaseVisionFace>) -> Unit,
        onFailureListener: (e: Exception) -> Unit = ::failedToProcessImage
    ) = detector.detectInImage(firebaseImage)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)

    private fun failedToProcessImage(e: Exception) {
        Log.e(FirebaseVisionFaceDetector::class.java.simpleName, "Failed to process an image", e)
    }
}