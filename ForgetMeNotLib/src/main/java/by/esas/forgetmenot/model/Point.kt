package by.esas.forgetmenot.model

import androidx.annotation.FloatRange

data class Point(@FloatRange(from = 0.0) val x: Float, @FloatRange(from = 0.0) val y: Float)