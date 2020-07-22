package by.esas.forgetmenot.model

class ImageSize(var width: Int, var height: Int) {

    fun flip(){
        val temp = height
        height = width
        width = temp
    }
}