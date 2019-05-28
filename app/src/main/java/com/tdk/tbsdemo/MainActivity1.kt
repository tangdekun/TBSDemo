package com.tdk.tbsdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.ValueCallback
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity1 : AppCompatActivity(), ValueCallback<String> {


    override fun onReceiveValue(p0: String?) {
        Log.d("MainActivity", p0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = arrayOfNulls<String>(1)
        list[0] = "android.permission.WRITE_EXTERNAL_STORAGE"
        this@MainActivity1.requestPermissions(list, 105)

        QbSdk.initX5Environment(this, object : PreInitCallback {
            override fun onCoreInitFinished() {
                Toast.makeText(this@MainActivity1, "onCoreInitFinished", Toast.LENGTH_SHORT).show()
            }

            override fun onViewInitFinished(p0: Boolean) {
                Log.d("MainActivity", "onViewInitFinished$p0")
                Toast.makeText(this@MainActivity1, "X5内核初始化完成$p0", Toast.LENGTH_SHORT).show()
            }

        })
//        openFile.setOnClickListener {
//            openFileReader(this@MainActivity1, "/sdcard/edit.pptx")
//        }

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
        val ret = QbSdk.openFileReader(context, pathName, params, this)

    }

}
