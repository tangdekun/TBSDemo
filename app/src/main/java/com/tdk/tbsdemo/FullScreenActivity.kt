package com.tdk.tbsdemo

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.tdk.tbsdemo.utils.WebViewJavaScriptFunction
import com.tdk.tbsdemo.utils.X5WebView
import com.tencent.smtt.sdk.TbsMediaPlayer
import com.tencent.smtt.sdk.TbsVideo
import kotlinx.android.synthetic.main.filechooser_layout.*


class FullScreenActivity : Activity() {

    /**
     * 用于演示X5webview实现视频的全屏播放功能 其中注意 X5的默认全屏方式 与 android 系统的全屏方式
     */

    internal lateinit var webView: X5WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filechooser_layout)
//        webView = findViewById<View>(R.id.web_filechooser) as X5WebView
//        webView.loadUrl("file:///android_asset/webpage/fullscreenVideo.html")
//
//        window.setFormat(PixelFormat.TRANSLUCENT)
//
//        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS)
//        webView.addJavascriptInterface(object : WebViewJavaScriptFunction {
//
//            override fun onJsFunctionCalled(tag: String) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @JavascriptInterface
//            fun onX5ButtonClicked() {
//                this@FullScreenActivity.enableX5FullscreenFunc()
//            }
//
//            @JavascriptInterface
//            fun onCustomButtonClicked() {
//                this@FullScreenActivity.disableX5FullscreenFunc()
//            }
//
//            @JavascriptInterface
//            fun onLiteWndButtonClicked() {
//                this@FullScreenActivity.enableLiteWndFunc()
//            }
//
//            @JavascriptInterface
//            fun onPageVideoClicked() {
//                this@FullScreenActivity.enablePageVideoFunc()
//            }
//        }, "Android")
//        public static boolean canUseTbsPlayer(Context context)
//
////判断当前Tbs播放器是否已经可以使用。
//
//        public static void openVideo(Context context, String videoUrl)
//
////直接调用播放接口，传入视频流的url
//
//        public static void openVideo(Context context, String videoUrl, Bundle extraData)
//
////extraData对象是根据定制需要传入约定的信息，没有需要可以传如null
//
////extraData可以传入key: "screenMode", 值: 102, 来控制默认的播放UI
//
////类似: extraData.putInt("screenMode", 102); 来实现默认全屏+控制栏等UI
        openFiles.setOnClickListener {
            if (TbsVideo.canUseTbsPlayer(this@FullScreenActivity)){


                var extraData  = Bundle()
                extraData.putInt("screenMode", 102)
                TbsVideo.openVideo(this@FullScreenActivity,"http://www.hwapu22.com/upload/%E8%A7%86%E9%A2%91/2014%E5%BF%AB%E6%98%93%E5%85%B8%E3%80%8A%E6%8E%A8%E8%8D%90%E7%AF%87%E3%80%8B%E5%AE%8C%E6%95%B4%E7%89%88.mp4" ,extraData)
// 来实现默认全屏+控制栏等UI
            }
        }
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
