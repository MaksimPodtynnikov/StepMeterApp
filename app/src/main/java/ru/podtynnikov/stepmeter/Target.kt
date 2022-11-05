package ru.podtynnikov.stepmeter

import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Target(
    var id: Int,
    var title: String,
    var icon: ByteArray?,
    var steps: Int,
    var isReady: Boolean
) {

    val bitmap: Bitmap?
        get() = if (icon != null) BitmapFactory.decodeByteArray(icon, 0, icon!!.size) else null
}