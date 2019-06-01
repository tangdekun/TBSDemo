package com.tdk.tbsdemo.utils

import Music
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.tdk.tbsdemo.APPAplication


class FileManager {

    companion object {

        private var mInstance: FileManager? = null
        private var mContext: Context = APPAplication.mContext
        lateinit var mContentResolver: ContentResolver
        private val mLock = Any()

        fun getInstance(): FileManager {
            if (mInstance == null) {
                synchronized(mLock) {
                    if (mInstance == null) {
                        mInstance = FileManager()
                        mContentResolver = mContext.getContentResolver()
                    }
                }
            }
            return mInstance!!
        }
    }

    /**
     * 获取本机音乐列表
     * @return
     */
    private fun getMusics(): List<Music> {
        val musics = mutableListOf<Music>()
        var c: Cursor? = null
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
            c.moveToFirst()
            while (c!!.moveToNext()) {
                val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))// 路径

                if (FileUtils.isFileExists(path)) {
                    continue
                }

                val name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)) // 歌曲名
                val album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) // 专辑
                val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) // 作者
                val size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))// 大小
                val duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))// 时长
                val time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))// 歌曲的id
                // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                val music = Music(name, path, album, artist, size, duration,pinyin = PinyinUtils.getPinyin(name))
                musics.add(music)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (c != null) {
                c!!.close()
            }
        }
        return musics
    }

}