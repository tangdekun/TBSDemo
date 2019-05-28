package com.tdk.tbsdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.tdk.tbsdemo.utils.UriUtil
import com.tdk.tbsdemo.utils.X5WebView
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class FilechooserActivity : Activity() {

    /**
     * 用于展示在web端<input type=text></input>的标签被选择之后，文件选择器的制作和生成
     */

    private var webView: X5WebView? = null
    private var uploadFile: ValueCallback<Uri>? = null
    private var uploadFiles: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filechooser_layout)

        webView = findViewById<View>(R.id.web_filechooser) as X5WebView

        webView!!.webChromeClient = object : WebChromeClient() {
            // For Android 3.0+
            fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                Log.i("test", "openFileChooser 1")
                this@FilechooserActivity.uploadFile = uploadFile
                openFileChooseProcess()
            }

            // For Android < 3.0
            fun openFileChooser(uploadMsgs: ValueCallback<Uri>) {
                Log.i("test", "openFileChooser 2")
                this@FilechooserActivity.uploadFile = uploadFile
                openFileChooseProcess()
            }

            // For Android  > 4.1.1
            override fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String?, capture: String?) {
                Log.i("test", "openFileChooser 3")
                this@FilechooserActivity.uploadFile = uploadFile
                openFileChooseProcess()
            }

            // For Android  >= 5.0
            override fun onShowFileChooser(webView: com.tencent.smtt.sdk.WebView?,
                                           filePathCallback: ValueCallback<Array<Uri>>?,
                                           fileChooserParams: WebChromeClient.FileChooserParams?): Boolean {
                Log.i("test", "openFileChooser 4:" + filePathCallback!!.toString())
                this@FilechooserActivity.uploadFiles = filePathCallback
                openFileChooseProcess()
                return true
            }

        }

        webView!!.loadUrl("file:///android_asset/webpage/fileChooser.html")

    }

    private fun openFileChooseProcess() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        startActivityForResult(Intent.createChooser(i, "test"), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
//            openFileReader(this@FilechooserActivity, "/sdcard/edit.pptx")
            Toast.makeText(this@FilechooserActivity, "requestCode$requestCode", Toast.LENGTH_SHORT).show()
        }
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> {
                    if (null != uploadFile) {
                        val result = data?.data
                        uploadFile!!.onReceiveValue(result)
                        uploadFile = null
                    }
                    if (null != uploadFiles) {
                        val result: Uri = data?.data!!
                        val path = UriUtil.getPath(this@FilechooserActivity,result)
                        path?.let { openFileReader(this@FilechooserActivity, it) }
                        uploadFiles!!.onReceiveValue(arrayOf<Uri>(result))
                        uploadFiles = null
                    }
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

    fun openFileReader(context: Context, pathName: String) {

        val params = HashMap<String, String>()
        params["local"] = "true"
        val jsonObject = JSONObject()
        try {
            jsonObject.put("pkgName", context.applicationContext.packageName)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        params["menuData"] = jsonObject.toString()
        QbSdk.getMiniQBVersion(context)
        val ret = QbSdk.openFileReader(context, pathName, params) { p0 -> Toast.makeText(this@FilechooserActivity, "onReceiveValue$p0", Toast.LENGTH_SHORT).show() }

    }

    /**
     * 确保注销配置能够被释放
     */
    override fun onDestroy() {
        // TODO Auto-generated method stub
        if (this.webView != null) {
            webView!!.destroy()
        }
        super.onDestroy()
    }

}
