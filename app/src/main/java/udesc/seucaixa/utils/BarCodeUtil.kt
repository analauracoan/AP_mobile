package udesc.seucaixa.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class BarcodeUtil {
    companion object {

        fun generateBarcode(input: String): Bitmap? {
            return try {
                val barcodeEncoder = BarcodeEncoder()
                val bitMatrix = barcodeEncoder.encode(input, BarcodeFormat.CODE_128, 600, 300)
                barcodeEncoder.createBitmap(bitMatrix)
            } catch (e: WriterException) {
                e.printStackTrace()
                null
            }
        }

    }
}