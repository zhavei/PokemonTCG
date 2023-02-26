package com.syafei.pokemontcg.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Environment
import com.syafei.pokemontcg.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private fun createTempFile(context: Context, pokemonName: String): File {
        // make sure that directory is created
        Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES + "/${context.getString(R.string.app_name)}/"
        ).mkdirs()

        // create directory
        val storageDir: File? = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES + "/${context.getString(R.string.app_name)}/"
        )

        // scan the file so it can be visible on gallery
        MediaScannerConnection.scanFile(
            context, arrayOf(storageDir.toString()),
            null, null
        )

        // return the file result
        return File.createTempFile("$timeStamp-$pokemonName-", ".jpg", storageDir)
    }

    fun saveToFile(
        imageUrl: String,
        pokemonName: String,
        context: Context,
        onLoadingListener: () -> Unit,
        onFinishedListener: (message: String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.IO) {
                    onLoadingListener()

                    val url = URL(imageUrl)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()

                    val input: InputStream = connection.inputStream
                    val myBitmap = BitmapFactory.decodeStream(input)
                    val stream = FileOutputStream(
                        createTempFile(context, pokemonName)
                    )

                    //add compress
                    var compressQuality = 50
                    var streamLength: Int
                    do {
                        val outStream = ByteArrayOutputStream()
                        myBitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outStream)
                        val byteArray = outStream.toByteArray()
                        streamLength = byteArray.size
                        compressQuality -= 5
                        stream.write(byteArray)
                        stream.close()
                    } while (streamLength > 500000)

                    onFinishedListener("image downloaded.")
                }
            } catch (e: Exception) {
                onFinishedListener("error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

}