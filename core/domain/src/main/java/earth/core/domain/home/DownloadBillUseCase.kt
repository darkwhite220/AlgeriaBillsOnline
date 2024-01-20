package earth.core.domain.home

import android.os.Environment
import earth.core.database.BillDownload
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DownloadBillUseCase @Inject constructor() {
    
    operator fun invoke(billDownload: BillDownload): String {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = billDownload.toFileName()
        val path = File(directory, fileName)
        if (!directory.isDirectory) {
            directory.mkdir()
        }
        return try {
            FileOutputStream(path).use { fos ->
                fos.write(billDownload.byteArray)
                println("PDF File Saved")
            }
            fileName
        } catch (e: Exception) {
            println("DownloadBillUseCase error: $e")
            throw e
        }
    }
    
}
