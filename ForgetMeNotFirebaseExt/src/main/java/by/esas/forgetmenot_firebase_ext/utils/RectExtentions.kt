package by.esas.forgetmenot_firebase_ext.utils

import android.graphics.Rect
import android.graphics.RectF

internal fun RectF.area(): Float = width() * height()

internal fun Rect.area(): Float = width().toFloat() * height().toFloat()

internal fun RectF.extend(size: Float) {
    this.bottom += size
    this.right += size
    this.top -= size
    this.left -= size
}

internal fun RectF.isNear(otherBounds: RectF, tolerance: Float) = when {
    left !in (otherBounds.left - tolerance)..(otherBounds.left + tolerance) -> false
    right !in (otherBounds.right - tolerance)..(otherBounds.right + tolerance) -> false
    top !in (otherBounds.top - tolerance)..(otherBounds.top + tolerance) -> false
    bottom !in (otherBounds.bottom - tolerance)..(otherBounds.bottom + tolerance) -> false
    else -> true
}

internal fun RectF.isInside(otherBounds: RectF, tolerance: Float) = when {
    left < otherBounds.left - tolerance -> false
    right > otherBounds.right + tolerance -> false
    top < otherBounds.top - tolerance -> false
    bottom > otherBounds.bottom + tolerance -> false
    else -> true
}