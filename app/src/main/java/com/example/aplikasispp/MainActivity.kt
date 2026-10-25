package com.example.aplikasispp

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()

        // JEMBATAN UNTUK SIMPAN PDF
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun savePdf(base64: String, fileName: String) {
                try {
                    val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Untuk Android 10 ke atas
                        val contentValues = ContentValues().apply {
                            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        }
                        val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                        if (uri != null) {
                            val outputStream = contentResolver.openOutputStream(uri)
                            outputStream?.write(decodedBytes)
                            outputStream?.close()
                            runOnUiThread { Toast.makeText(this@MainActivity, "PDF disimpan di folder Download", Toast.LENGTH_LONG).show() }
                        }
                    } else {
                        // Untuk Android 9 ke bawah
                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val file = File(downloadsDir, fileName)
                        val fos = FileOutputStream(file)
                        fos.write(decodedBytes)
                        fos.close()
                        runOnUiThread { Toast.makeText(this@MainActivity, "PDF disimpan di folder Download", Toast.LENGTH_LONG).show() }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(this@MainActivity, "Gagal simpan PDF: ${e.message}", Toast.LENGTH_LONG).show() }
                }
            }
        }, "AndroidDownloader")

        webView.loadUrl("file:///android_asset/index.html")
        setContentView(webView)
    }
}
