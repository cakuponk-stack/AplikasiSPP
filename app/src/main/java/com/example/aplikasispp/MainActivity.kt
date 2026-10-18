package com.example.aplikasispp

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView) // Pastikan ID di layout XML Anda adalah "webView"

        // Konfigurasi Pengaturan WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true      // Mengaktifkan JavaScript
        webSettings.domStorageEnabled = true      // Mengaktifkan penyimpanan DOM/LocalStorage HTML
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        // Agar link tetap terbuka di dalam aplikasi, bukan keluar ke Browser HP
        webView.webViewClient = WebViewClient()

        // Ganti URL di bawah ini dengan URL website/sistem SPP Anda
        webView.loadUrl("https://www.google.com")

        // Menangani fungsi tombol Back HP agar kembali ke halaman sebelumnya di WebView (jika ada)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}