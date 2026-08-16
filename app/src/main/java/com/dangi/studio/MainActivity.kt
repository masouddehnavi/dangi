package com.dangi.studio

import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.webkit.WebViewAssetLoader

class MainActivity : FragmentActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = false

        webView.addJavascriptInterface(BiometricBridge(), "AndroidBiometric")

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(
                "/assets/",
                WebViewAssetLoader.AssetsPathHandler(this)
            )
            .build()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                val url = request?.url ?: return null
                return assetLoader.shouldInterceptRequest(url)
            }
        }

        if (savedInstanceState == null) {
            webView.loadUrl(
                "https://appassets.androidplatform.net/assets/index.html"
            )
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    /**
     * پل ارتباطی بین جاوااسکریپت (WebView) و قابلیت اثر انگشت اندروید.
     * از داخل index.html با window.AndroidBiometric صدا زده می‌شود.
     */
    inner class BiometricBridge {

        @JavascriptInterface
        fun isAvailable(): Boolean {
            val manager = BiometricManager.from(this@MainActivity)
            val allowed = BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.BIOMETRIC_WEAK
            return manager.canAuthenticate(allowed) == BiometricManager.BIOMETRIC_SUCCESS
        }

        @JavascriptInterface
        fun authenticate() {
            runOnUiThread {
                val executor = ContextCompat.getMainExecutor(this@MainActivity)
                val callback = object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        webView.evaluateJavascript(
                            "window.onBiometricResult && window.onBiometricResult(true)",
                            null
                        )
                    }

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        webView.evaluateJavascript(
                            "window.onBiometricResult && window.onBiometricResult(false)",
                            null
                        )
                    }

                    override fun onAuthenticationFailed() {
                        // اجازه بده کاربر دوباره امتحان کند؛ اینجا کاری انجام نمی‌دهیم
                    }
                }

                val prompt = BiometricPrompt(this@MainActivity, executor, callback)
                val info = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("ورود به دَنگی")
                    .setSubtitle("اثر انگشت خود را روی حسگر قرار بده")
                    .setNegativeButtonText("انصراف")
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.BIOMETRIC_WEAK
                    )
                    .build()

                prompt.authenticate(info)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
