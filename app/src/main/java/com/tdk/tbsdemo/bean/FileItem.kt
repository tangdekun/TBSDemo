package com.tdk.tbsdemo.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @创建者 chaychan
 * @创建时间 2016/7/24
 * @描述 文件的Bean（文件管理/浏览模块中的一个item）
 */
@Parcelize
data class FileItem(var filePic: Int, var fileName: String, var filePath: String,
                    var fileModifiedTime: String) : Parcelable {

    override fun toString(): String {
        return ("FileItem [filePic=" + filePic + ", fileName=" + fileName
                + ", filePath=" + filePath + ", fileModifiedTime="
                + fileModifiedTime + "]")
    }

}
