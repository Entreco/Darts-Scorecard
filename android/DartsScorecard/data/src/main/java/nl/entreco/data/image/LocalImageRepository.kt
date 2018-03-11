package nl.entreco.data.image

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import nl.entreco.domain.repository.ImageRepository
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

/**
 * Created by entreco on 11/03/2018.
 */
class LocalImageRepository(private val context: Context, private val contentResolver: ContentResolver) : ImageRepository {

    override fun copyImageToPrivateAppData(imageUri: String?, size: Float): String? {
        if (imageUri == null || size == 0F) return imageUri

        return try {
            val originalUri = Uri.parse(imageUri)
            val output = File(context.filesDir, originalUri.lastPathSegment)

            copyStream(originalUri, output)
            resize(output, size)

            val outputUri = Uri.fromFile(output)
            outputUri.toString()
        } catch (somethingWentWrong: Exception) {
            imageUri
        }

    }

    private fun copyStream(originalUri: Uri?, output: File) {
        contentResolver.openInputStream(originalUri).use { input ->
            FileOutputStream(output).use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun resize(output: File, size: Float): Bitmap {
        val bitmap = BitmapFactory.decodeFile(output.absolutePath)
        val width = bitmap.width
        val height = bitmap.height

        val shortSide = min(width, height) / 2
        val src = Rect(0, 0, width, height)
        val dst = Rect(src.centerX() - shortSide, src.centerY() - shortSide, src.centerX() + shortSide, src.centerY() + shortSide)
        val tmp = Bitmap.createBitmap(bitmap, dst.left, dst.top, dst.width(), dst.height(), null, true)
        val resized = Bitmap.createScaledBitmap(tmp, size.toInt(), size.toInt(), true)

        tmp.recycle()
        bitmap.recycle()
        return resized
    }
}