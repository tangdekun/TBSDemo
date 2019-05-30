package com.tdk.tbsdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Process
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.tdk.tbsdemo.utils.X5WebView
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.*
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm
import com.tencent.smtt.utils.TbsLog
import java.net.MalformedURLException
import java.net.URL

class BrowserActivity : Activity() {
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private var mWebView: X5WebView? = null
    private var mViewParent: ViewGroup? = null
    private var mBack: ImageButton? = null
    private var mForward: ImageButton? = null
    private var mExit: ImageButton? = null
    private var mHome: ImageButton? = null
    private var mMore: ImageButton? = null
    private var mGo: Button? = null
    private var mUrl: EditText? = null
    private val mNeedTestPage = false

    private val disable = 120
    private val enable = 255

    private var mPageLoadingProgressBar: ProgressBar? = null

    private var uploadFile: ValueCallback<Uri>? = null

    private var mIntentUrl: URL? = null

    internal var m_selected = booleanArrayOf(true, true, true, true, false, false, true)
    private val mUrlStartNum = 0
    private var mCurrentUrl = mUrlStartNum
    private val mTestHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_OPEN_TEST_URL -> {
                    if (!mNeedTestPage) {
                        return
                    }

                    val testUrl = ("file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html")
                    if (mWebView != null) {
                        mWebView!!.loadUrl(testUrl)
                    }

                    mCurrentUrl++
                }
                MSG_INIT_UI -> init()
            }
            super.handleMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.TRANSLUCENT)

        val intent = intent
        if (intent != null) {
            try {
                mIntentUrl = URL(intent.data!!.toString())
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {

            } catch (e: Exception) {
            }

        }
        //
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                window.setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
            }
        } catch (e: Exception) {
        }

        setContentView(R.layout.activity_main)
        mViewParent = findViewById<View>(R.id.webView1) as ViewGroup

        initBtnListenser()

        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10)

    }

    private fun changGoForwardButton(view: WebView) {
        if (view.canGoBack())
            mBack!!.imageAlpha=enable
        else
         mBack!!.imageAlpha=enable
        if (view.canGoForward())
            mForward!!.imageAlpha=enable
        else
            mForward!!.imageAlpha=enable
        if (view.url != null && view.url.equals(mHomeUrl, ignoreCase = true)) {
            mHome!!.imageAlpha=enable
            mHome!!.isEnabled = false
        } else {
            mHome!!.imageAlpha=enable
            mHome!!.isEnabled = true
        }
    }

    private fun initProgressBar() {
        mPageLoadingProgressBar = findViewById<View>(R.id.progressBar1) as ProgressBar// new
        // ProgressBar(getApplicationContext(),
        // null,
        // android.R.attr.progressBarStyleHorizontal);
        mPageLoadingProgressBar!!.max = 100
        mPageLoadingProgressBar!!.progressDrawable = this.resources
                .getDrawable(R.drawable.color_progressbar)
    }

    private fun init() {

        mWebView = X5WebView(this)

        mViewParent!!.addView(mWebView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT))

        initProgressBar()

        mWebView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000)// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view!!)
                /* mWebView.showLog("test Log"); */
            }
        }

        mWebView!!.webChromeClient = object : WebChromeClient() {

            internal var myVideoView: View? = null
            internal lateinit var myNormalView: View
            internal var callback: CustomViewCallback? = null

            override fun onJsConfirm(arg0: WebView?, arg1: String?, arg2: String?,
                                     arg3: JsResult?): Boolean {
                return super.onJsConfirm(arg0, arg1, arg2, arg3)
            }

            // /////////////////////////////////////////////////////////
            //
            /**
             * 全屏播放配置
             */
            override fun onShowCustomView(view: View?,
                                          customViewCallback: CustomViewCallback?) {
                val normalView = findViewById<View>(R.id.web_filechooser) as FrameLayout
                val viewGroup = normalView.parent as ViewGroup
                viewGroup.removeView(normalView)
                viewGroup.addView(view)
                myVideoView = view
                myNormalView = normalView
                callback = customViewCallback
            }

            override fun onHideCustomView() {
                if (callback != null) {
                    callback!!.onCustomViewHidden()
                    callback = null
                }
                if (myVideoView != null) {
                    val viewGroup = myVideoView!!.parent as ViewGroup
                    viewGroup.removeView(myVideoView)
                    viewGroup.addView(myNormalView)
                }
            }

            override fun onJsAlert(arg0: WebView?, arg1: String?, arg2: String?,
                                   arg3: JsResult?): Boolean {
                /**
                 * 这里写入你自定义的window alert
                 */
                return super.onJsAlert(null, arg1, arg2, arg3)
            }
        }

        mWebView!!.setDownloadListener { arg0, arg1, arg2, arg3, arg4 ->
            TbsLog.d(TAG, "url: $arg0")
            AlertDialog.Builder(this@BrowserActivity)
                    .setTitle("allow to download？")
                    .setPositiveButton("yes"
                    ) { dialog, which ->
                        Toast.makeText(
                                this@BrowserActivity,
                                "fake message: i'll download...",
                                Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("no"
                    ) { dialog, which ->
                        // TODO Auto-generated method stub
                        Toast.makeText(
                                this@BrowserActivity,
                                "fake message: refuse download...",
                                Toast.LENGTH_SHORT).show()
                    }
                    .setOnCancelListener {
                        // TODO Auto-generated method stub
                        Toast.makeText(
                                this@BrowserActivity,
                                "fake message: refuse download...",
                                Toast.LENGTH_SHORT).show()
                    }.show()
        }

        val webSetting = mWebView!!.getSettings()
        webSetting.setAllowFileAccess(true)
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS)
        webSetting.setSupportZoom(true)
        webSetting.setBuiltInZoomControls(true)
        webSetting.setUseWideViewPort(true)
        webSetting.setSupportMultipleWindows(false)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true)
        webSetting.setJavaScriptEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        webSetting.setAppCachePath(this.getDir("appcache", 0).path)
        webSetting.setDatabasePath(this.getDir("databases", 0).path)
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .path)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND)
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        val time = System.currentTimeMillis()
        if (mIntentUrl == null) {
            mWebView!!.loadUrl(mHomeUrl)
        } else {
            mWebView!!.loadUrl(mIntentUrl!!.toString())
        }
        TbsLog.d("time-cost", "cost time: " + (System.currentTimeMillis() - time))
        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().sync()
    }

    @SuppressLint("SetTextI18n")
    private fun initBtnListenser() {
        mBack = findViewById<View>(R.id.btnBack1) as ImageButton
        mForward = findViewById<View>(R.id.btnForward1) as ImageButton
        mExit = findViewById<View>(R.id.btnExit1) as ImageButton
        mHome = findViewById<View>(R.id.btnHome1) as ImageButton
        mGo = findViewById<View>(R.id.btnGo1) as Button
        mUrl = findViewById<View>(R.id.editUrl1) as EditText
        mMore = findViewById<View>(R.id.btnMore) as ImageButton
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
         mBack!!.imageAlpha=enable
            mForward!!.imageAlpha=enable
            mHome!!.imageAlpha=enable
        }
        mHome!!.isEnabled = false

        mBack!!.setOnClickListener {
            if (mWebView != null && mWebView!!.canGoBack())
                mWebView!!.goBack()
        }

        mForward!!.setOnClickListener {
            if (mWebView != null && mWebView!!.canGoForward())
                mWebView!!.goForward()
        }

        mGo!!.setOnClickListener {
            val url = mUrl!!.text.toString()
            mWebView!!.loadUrl(url)
            mWebView!!.requestFocus()
        }

        mMore!!.setOnClickListener {
            Toast.makeText(this@BrowserActivity, "not completed",
                    Toast.LENGTH_LONG).show()
        }

        mUrl!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mGo!!.visibility = View.VISIBLE
                if (null == mWebView!!.getUrl())
                    return@OnFocusChangeListener
                if (mWebView!!.url.equals(mHomeUrl, true)) {
                    mUrl!!.setText("")
                    mGo!!.text = "首页"
                    mGo!!.setTextColor(0X6F0F0F0F)
                } else {
                    mUrl!!.setText(mWebView!!.getUrl())
                    mGo!!.text = "进入"
                    mGo!!.setTextColor(0X6F0000CD)
                }
            } else {
                mGo!!.visibility = View.GONE
                val title = mWebView!!.getTitle()
                if (title != null && title!!.length > MAX_LENGTH)
                    mUrl!!.setText("${title!!.subSequence(0, MAX_LENGTH)}...")
                else
                    mUrl!!.setText(title)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }

        mUrl!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub

                var url: String? = null
                if (mUrl!!.text != null) {
                    url = mUrl!!.text.toString()
                }

                if (url == null || mUrl!!.text.toString().equals("", ignoreCase = true)) {
                    mGo!!.text = "请输入网址"
                    mGo!!.setTextColor(0X6F0F0F0F)
                } else {
                    mGo!!.text = "进入"
                    mGo!!.setTextColor(0X6F0000CD)
                }
            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int,
                                           arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub

            }

            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int,
                                       arg3: Int) {
                // TODO Auto-generated method stub

            }
        })

        mHome!!.setOnClickListener {
            if (mWebView != null)
                mWebView!!.loadUrl(mHomeUrl)
        }

        mExit!!.setOnClickListener { Process.killProcess(Process.myPid()) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView!!.canGoBack()) {
                mWebView!!.goBack()
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(mWebView!!)
                return true
            } else
                return super.onKeyDown(keyCode, event)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
                + ",resultCode:" + resultCode)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> if (null != uploadFile) {
                    val result = if (data == null || resultCode != Activity.RESULT_OK)
                        null
                    else
                        data.data
                    uploadFile!!.onReceiveValue(result)
                    uploadFile = null
                }
                else -> {
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (null != uploadFile) {
                uploadFile!!.onReceiveValue(null)
                uploadFile = null
            }

        }

    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null || mWebView == null || intent.data == null)
            return
        mWebView!!.loadUrl(intent.data!!.toString())
    }

    override fun onDestroy() {
        mTestHandler?.removeCallbacksAndMessages(null)
        if (mWebView != null)
            mWebView!!.destroy()
        super.onDestroy()
    }

    companion object {

        private val mHomeUrl = "http://app.html5.qq.com/navi/index"
        private val TAG = "SdkDemo"
        private val MAX_LENGTH = 14

        val MSG_OPEN_TEST_URL = 0
        val MSG_INIT_UI = 1
    }

}
