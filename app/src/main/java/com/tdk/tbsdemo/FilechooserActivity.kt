package com.tdk.tbsdemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.tdk.tbsdemo.utils.UriUtil
import com.tencent.smtt.sdk.QbSdk
import kotlinx.android.synthetic.main.filechooser_layout.*
import java.util.*

class FilechooserActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filechooser_layout)
        openFiles.setOnClickListener {
            openFileChooseProcess()
        }
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
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                0 -> {
                    val result: Uri = data?.data!!
                    val path = UriUtil.getPath(this@FilechooserActivity, result)
                    path?.let { openFileReader(this@FilechooserActivity, it) }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {


        }
    }

    private fun openFileReader(context: Context, pathName: String) {

        val params = HashMap<String, String>()
        params["local"] = "false"
        params["style"] = "1"
        params["topBarBgColor"] = "#303F9F"
        params["menuData"] = "{ pkgName:\"\",className:\"\",thirdCtx:{},menuItems:[{}] }"
//        val jsonObject = JSONObject()
//        try {
//            jsonObject.put("pkgName", context.applicationContext.packageName)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//        params["menuData"] = jsonObject.toString()
        QbSdk.getMiniQBVersion(context)
        val ret = QbSdk.openFileReader(context, pathName, params) { p0 -> Toast.makeText(this@FilechooserActivity, "onReceiveValue$p0", Toast.LENGTH_SHORT).show() }

    }

    /**
     * 确保注销配置能够被释放
     */
    override fun onDestroy() {
        super.onDestroy()
    }

}
