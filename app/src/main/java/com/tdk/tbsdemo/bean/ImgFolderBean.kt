package com.tdk.tbsdemo.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @创建人 chaychan
 * @创建时间 2016/7/24  10:05
 * @描述 图片文件夹的bean类
 */
@Parcelize
data class ImgFolderBean(
        /**第一张图片的路径 */
        var fistImgPath: String?,
        /**文件夹名 */
        var name: String?,
        /**文件夹中图片的数量 */
        var count: Int = 0
) : Parcelable {
    /**当前文件夹的路径 */
    private var dir: String? = null

    fun getDir(): String? {
        return dir
    }


    constructor() : this(null, null) {

    }

    fun setDir(dir: String) {
        this.dir = dir
        val lastIndex = dir.lastIndexOf("/")
        this.name = dir.substring(lastIndex + 1)
    }


}
