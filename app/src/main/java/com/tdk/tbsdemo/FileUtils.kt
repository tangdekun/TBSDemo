package com.tdk.tbsdemo

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {

    /**文档类型 */
    private const val TYPE_DOC = 0
    /**apk类型 */
    private const val TYPE_APK = 1
    /**压缩包类型 */
    private const val TYPE_ZIP = 2


    /** 判断SD卡是否挂载  */
    val isSDCardAvailable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()


    /**
     * 判断文件是否存在
     * @param path 文件的路径
     * @return
     */
    fun isExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    fun getFileType(path: String): Int {
        var path = path
        path = path.toLowerCase()
        return if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx")) {
            TYPE_DOC
        } else if (path.endsWith(".apk")) {
            TYPE_APK
        } else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            TYPE_ZIP
        } else {
            -1
        }
    }


    /**通过文件名获取文件图标 */
    fun getFileIconByPath(path: String): Int {
        var path = path
        path = path.toLowerCase()
        var iconId = R.mipmap.ic_launcher
        if (path.endsWith(".txt")) {
            iconId = R.mipmap.ic_launcher_round
        } else if (path.endsWith(".doc") || path.endsWith(".docx")) {
            iconId = R.mipmap.ic_launcher_round
        } else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
            iconId = R.mipmap.ic_launcher_round
        } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
            iconId = R.mipmap.ic_launcher_round
        } else if (path.endsWith(".xml")) {
            iconId = R.mipmap.ic_launcher_round
        } else if (path.endsWith(".htm") || path.endsWith(".html")) {
            iconId = R.mipmap.ic_launcher_round
        }
        return iconId
    }

    /**是否是图片文件 */
    fun isPicFile(path: String): Boolean {
        var path = path
        path = path.toLowerCase()
        return path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")
    }

    /**
     * 从文件的全名得到文件的拓展名
     *
     * @param filename
     * @return
     */
    fun getExtFromFilename(filename: String): String {
        val dotPosition = filename.lastIndexOf('.')
        return if (dotPosition != -1) {
            filename.substring(dotPosition + 1, filename.length)
        } else ""
    }

    /**
     * 读取文件的修改时间
     *
     * @param f
     * @return
     */
    fun getModifiedTime(f: File): String {
        val cal = Calendar.getInstance()
        val time = f.lastModified()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        cal.timeInMillis = time
        // System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.time)
    }


}
