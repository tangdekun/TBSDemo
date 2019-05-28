package com.tdk.tbsdemo

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.SimpleAdapter
import java.util.*

class MainActivity : Activity() {


    private var mContext: Context? = null
    private var gridAdapter: SimpleAdapter? = null
    private var gridView: GridView? = null
    private var items: ArrayList<HashMap<String, Any>>? = null

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity OnCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_advanced)
        var list = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE")
        this@MainActivity.requestPermissions(list, 105)
        mContext = this
        if (!main_initialized) {
            this.new_init()
        }
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // Activity OnResume
    override fun onResume() {
        this.new_init()

        // this.gridView.setAdapter(gridAdapter);
        super.onResume()
    }

    // ////////////////////////////////////////////////////////////////////////////////
    // initiate new UI content
    private fun new_init() {
        items = ArrayList()
        this.gridView = this.findViewById<View>(R.id.item_grid) as GridView

        if (gridView == null)
            throw IllegalArgumentException("the gridView is null")

        titles = resources.getStringArray(R.array.index_titles)
        val iconResourse = intArrayOf(R.drawable.tbsweb, R.drawable.fullscreen, R.drawable.filechooser)

        var item: HashMap<String, Any>? = null
        // HashMap<String, ImageView> block = null;
        for (i in titles!!.indices) {
            item = HashMap()
            item["title"] = titles!![i]
            item["icon"] = iconResourse[i]

            items!!.add(item)
        }
        this.gridAdapter = SimpleAdapter(this, items,
                R.layout.function_block, arrayOf("title", "icon"),
                intArrayOf(R.id.Item_text, R.id.Item_bt))
        if (null != this.gridView) {
            this.gridView!!.adapter = gridAdapter
            this.gridAdapter!!.notifyDataSetChanged()
            this.gridView!!.onItemClickListener = OnItemClickListener { gridView, view, position, id ->
                var intent: Intent? = null
                when (position) {
                    FILE_CHOOSER -> {
                        intent = Intent(this@MainActivity,
                                FilechooserActivity::class.java)
                        this@MainActivity.startActivity(intent)

                    }
                    FULL_SCREEN_VIDEO -> {
                        intent = Intent(this@MainActivity,
                                FullScreenActivity::class.java)
                        this@MainActivity.startActivity(intent)
                    }

                    TBS_WEB -> {
                        intent = Intent(this@MainActivity,
                                BrowserActivity::class.java)
                        this@MainActivity.startActivity(intent)

                    }
                }
            }

        }
        main_initialized = true

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // TODO Auto-generated method stub
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> this.tbsSuiteExit()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun tbsSuiteExit() {
        val dialog = AlertDialog.Builder(mContext)
        dialog.setTitle("X5功能演示")
        dialog.setPositiveButton("OK") { dialog, which ->
            Process.killProcess(Process.myPid())
        }
        dialog.setMessage("quit now?")
        dialog.create().show()
    }

    companion object {

        var firstOpening = true
        private var titles: Array<String>? = null

        val MSG_WEBVIEW_CONSTRUCTOR = 1
        val MSG_WEBVIEW_POLLING = 2
        private val TBS_WEB = 0
        private val FULL_SCREEN_VIDEO = 1
        private val FILE_CHOOSER = 2

        private var main_initialized = false
    }
}
