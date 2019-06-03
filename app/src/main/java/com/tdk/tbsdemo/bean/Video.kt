package com.tdk.tbsdemo.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @创建人 chaychan
 * @创建时间 2016/7/23  17:20
 */
@Parcelize
data class Video
constructor(var id: Int,
            var path: String,
            var name: String,
            var resolution: String,
        //分辨率
            var size: Long,
            var date: Long,
            var duration: Long) : Parcelable
