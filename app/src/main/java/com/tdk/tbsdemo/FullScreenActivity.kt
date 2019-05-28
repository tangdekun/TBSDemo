package com.tdk.tbsdemo

import android.app.Activity
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.tdk.tbsdemo.utils.WebViewJavaScriptFunction
import com.tdk.tbsdemo.utils.X5WebView


class FullScreenActivity : Activity() {

    /**
     * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */

    internal lateinit var webView: X5WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filechooser_layout)
        webView = findViewById<View>(R.id.web_filechooser) as X5WebView
        webView.loadUrl("file:///android_asset/webpage/fullscreenVideo.html")

        window.setFormat(PixelFormat.TRANSLUCENT)

        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS)
        webView.addJavascriptInterface(object : WebViewJavaScriptFunction {

            override fun onJsFunctionCalled(tag: String) {
                // TODO Auto-generated method stub

            }

            @JavascriptInterface
            fun onX5ButtonClicked() {
                this@FullScreenActivity.enableX5FullscreenFunc()
            }

            @JavascriptInterface
            fun onCustomButtonClicked() {
                this@FullScreenActivity.disableX5FullscreenFunc()
            }

            @JavascriptInterface
            fun onLiteWndButtonClicked() {
                this@FullScreenActivity.enableLiteWndFunc()
            }

            @JavascriptInterface
            fun onPageVideoClicked() {
                this@FullScreenActivity.enablePageVideoFunc()
            }
        }, "Android")

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig)
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // /////////////////////////////////////////
    // 向webview发出信息
    private fun enableX5FullscreenFunc() {

        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data)
        }
    }

    private fun disableX5FullscreenFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", true)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data)
        }
    }

    private fun enableLiteWndFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data)
        }
    }

    private fun enablePageVideoFunc() {
        if (webView.getX5WebViewExtension() != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams",
                    data)
        }
    }

}
