package nl.entreco.domain.profile.update

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import nl.entreco.domain.BaseUsecase
import nl.entreco.domain.common.executors.Background
import nl.entreco.domain.common.executors.Foreground
import nl.entreco.domain.repository.ProfileRepository
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by entreco on 28/02/2018.
 */
class UpdateProfileUsecase @Inject constructor(
        private val context: Context,
        private val profileRepository: ProfileRepository,
        bg: Background, fg: Foreground) : BaseUsecase(bg, fg) {

    private val contentResolver: ContentResolver by lazy { context.contentResolver }

    fun exec(req: UpdateProfileRequest, done: (UpdateProfileResponse) -> Unit, fail: (Throwable) -> Unit) {
        onBackground({

            // Copy image to local file && resize
            val localImage = copyImageToPrivateAppData(req.image, req.size)

            val profile = profileRepository.update(req.id, req.name, localImage, req.double)
            onUi { done(UpdateProfileResponse(profile)) }
        }, fail)
    }

    private fun copyImageToPrivateAppData(imageUri: String?, size: Float): String? {
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
