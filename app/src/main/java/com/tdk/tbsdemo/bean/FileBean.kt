package com.tdk.tbsdemo.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @创建人 chaychan
 * @创建时间 2016/7/23  18:11
 * @描述 文件,可以是文档、apk、压缩包
 */
@Parcelize
class FileBean(
        /** 文件的路径 */
        var path: String,
        /**文件图片资源的id，drawable或mipmap文件中已经存放doc、xml、xls等文件的图片 */
        var iconId: Int) : Parcelable {

    override fun toString(): String {
        return "FileBean{" +
                "path='" + path + '\''.toString() +
                ", iconId=" + iconId +
                '}'.toString()
    }
}
