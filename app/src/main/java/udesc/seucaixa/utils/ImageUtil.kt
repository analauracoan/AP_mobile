package udesc.seucaixa.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageUtil {
    companion object {

        fun getBitmapFromView(view: View): Bitmap {
            val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = view.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            view.draw(canvas)
            return returnedBitmap
        }

        @Throws(IOException::class)
        fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs()
            val file = File(cachePath, "image.png")
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()

            return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        }

        fun shareImage(context: Context, imageUri: Uri) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
            }

            // Allow the user to choose any app that can handle the intent
            val chooser = Intent.createChooser(intent, "Share Image")
            context.startActivity(chooser)
        }
    }
}