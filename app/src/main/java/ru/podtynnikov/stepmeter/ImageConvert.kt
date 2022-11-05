package ru.podtynnikov.stepmeter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object ImageConvert {
    @JvmStatic
    fun bitmapToBlob(bitmap: Bitmap?): ByteArray? {
        return if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            bitmap.recycle()
            squeeze(byteArray)
        } else null
    }

    private fun squeeze(imagemImg: ByteArray?): ByteArray? {
        var imagemImg1 = imagemImg
        return if (imagemImg1 != null) {
            while (imagemImg1!!.size > 500000) {
                    val bitmap = BitmapFactory.decodeByteArray(imagemImg1, 0, imagemImg1.size)
                    val resized = Bitmap.createScaledBitmap(
                        bitmap,
                        (bitmap.width * 0.8).toInt(),
                        (bitmap.height * 0.8).toInt(),
                        true
                    )
                    val stream = ByteArrayOutputStream()
                    resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    imagemImg1 = stream.toByteArray()
                }
            imagemImg1
        } else null
    }
}