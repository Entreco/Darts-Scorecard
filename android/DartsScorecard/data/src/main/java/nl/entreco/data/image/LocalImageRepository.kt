package nl.entreco.data.image

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import nl.entreco.domain.repository.ImageRepository
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

/**
 * Created by entreco on 11/03/2018.
 */
class LocalImageRepository(private val context: Context,
                           private val contentResolver: ContentResolver) : ImageRepository {

    override fun copyImageToPrivateAppData(imageUri: String?, size: Float): String? {
        if (imageUri == null || size == 0F) return imageUri

        return try {
            val originalUri = Uri.parse(imageUri)
            val output = File(context.filesDir, originalUri.lastPathSegment)
            val rotation = getPhotoOrientation(originalUri)

            copyInput(originalUri, output)
            copyOutput(output, resize(output, size, rotation))

            val outputUri = Uri.fromFile(output)
            outputUri.toString()
        } catch (somethingWentWrong: Exception) {
            imageUri
        }
    }

    private fun copyInput(originalUri: Uri, output: File) {
        contentResolver.openInputStream(originalUri).use { input ->
            FileOutputStream(output).use { output ->
                input?.copyTo(output)
            }
        }
    }

    private fun copyOutput(output: File, bmp: Bitmap) {
        output.outputStream().use {
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, it)
            bmp.recycle()
        }
    }

    private fun getPhotoOrientation(uri: Uri): Float {
        contentResolver.openInputStream(uri).use { input ->
            val exif = if (input != null) ExifInterface(input) else return 0F
            return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_270 -> 270F
                ExifInterface.ORIENTATION_ROTATE_180 -> 180F
                ExifInterface.ORIENTATION_ROTATE_90 -> 90F
                else -> 0F
            }
        }
    }

    private fun resize(output: File, size: Float, degrees: Float): Bitmap {
        val boundsOnly = BitmapFactory.Options()
        boundsOnly.inJustDecodeBounds = true
        BitmapFactory.decodeFile(output.absolutePath, boundsOnly)

        val options = BitmapFactory.Options()
        options.inSampleSize = determineSampleSize(boundsOnly, 1, size)
        val bitmap = BitmapFactory.decodeFile(output.absolutePath, options)
        val width = bitmap.width
        val height = bitmap.height

        val shortSide = min(width, height) / 2
        val src = Rect(0, 0, width, height)
        val dst = Rect(src.centerX() - shortSide, src.centerY() - shortSide,
                src.centerX() + shortSide, src.centerY() + shortSide)

        val matrix = Matrix()
        matrix.postRotate(degrees)
        val tmp = Bitmap.createBitmap(bitmap, dst.left, dst.top, dst.width(), dst.height(), matrix,
                true)


        val resized = Bitmap.createScaledBitmap(tmp, size.toInt(), size.toInt(), true)
        tmp.recycle()
        bitmap.recycle()

        return resized
    }

    private fun determineSampleSize(options: BitmapFactory.Options, startScale: Int,
                                    size: Float): Int {
        var scale = startScale
        while (options.outWidth / scale / 2 >= size && options.outHeight / scale / 2 >= size) {
            scale *= 2
        }
        return scale
    }
}