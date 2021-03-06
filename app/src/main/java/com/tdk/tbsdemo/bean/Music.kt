package com.tdk.tbsdemo.bean

import android.os.Parcel
import android.os.Parcelable
import com.tdk.tbsdemo.PinyinUtils


/**
 * @Author tangdekun
 * @Date 2019/6/1-11:23
 * @Email tangdekun0924@gmail.com
 */

data class Music(var name: String?,
                 /**歌曲名*/
                 var path: String?,
                 /**路径*/
                 var album: String?,
                 /**所属专辑*/
                 var artist: String?,
                 /**艺术家(作者)*/
                 var size: Long,
                 /**文件大小*/
                 var duration: Int
        /**时长*/

) : Parcelable {

    private var pinyin: String? = null
        get() {
            return PinyinUtils.getPinyin(name)
        }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(path)
        writeString(album)
        writeString(artist)
        writeLong(size)
        writeInt(duration)
        writeString(pinyin)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Music> = object : Parcelable.Creator<Music> {
            override fun createFromParcel(source: Parcel): Music = Music(source)
            override fun newArray(size: Int): Array<Music?> = arrayOfNulls(size)
        }
    }
}