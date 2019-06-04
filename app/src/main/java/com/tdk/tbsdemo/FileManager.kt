package com.tdk.tbsdemo

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.ApplicationInfo
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import com.tdk.tbsdemo.bean.*
import java.io.File
import java.util.*

/**
 * @创建人 chaychan
 * @创建时间 2016/7/23  14:34
 * @描述 文件管理者, 可以获取本机的各种文件
 */
class FileManager {

    /**
     * 获取本机音乐列表
     * @return
     */
    // 路径
    // 歌曲名
    // 专辑
    // 作者
    // 大小
    // 时长
    // 歌曲的id
    // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
    val musics: List<Music>
        get() {
            val musics = ArrayList<Music>()
            var c: Cursor? = null
            try {
                c = mContentResolver!!.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER)

                while (c!!.moveToNext()) {
                    val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                    if (!File(path).exists()) {
                        continue
                    }

                    val name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                    val duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))

                    val music = Music(name, path, album, artist, size, duration)
                    musics.add(music)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                c?.close()
            }
            return musics
        }

    /**
     * 获取本机视频列表
     * @return
     */
    // String[] mediaColumns = { "_id", "_data", "_display_name",
    // "_size", "date_modified", "duration", "resolution" };
    // 路径
    // 视频的id
    // 视频名称
    //分辨率
    // 大小
    // 时长
    //修改时间
    val videos: List<Video>
        get() {

            val videos = ArrayList<Video>()

            var c: Cursor? = null
            try {
                c = mContentResolver!!.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER)
                while (c!!.moveToNext()) {
                    val path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                    if (!File(path).exists()) {
                        continue
                    }

                    val id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    val name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                    val resolution = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION))
                    val size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
                    val date = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED))

                    val video = Video(id, path, name, resolution, size, date, duration)
                    videos.add(video)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                c?.close()
            }
            return videos
        }

    /**
     * 得到图片文件夹集合
     */
    // 扫描图片
    //用于保存已经添加过的文件夹目录
    // 路径
    //如果已经添加过
    //添加到保存目录的集合中
    val imageFolders: List<ImgFolderBean>
        get() {
            val folders = ArrayList<ImgFolderBean>()
            var c: Cursor? = null
            try {
                c = mContentResolver!!.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                        arrayOf("image/jpeg", "image/png"), MediaStore.Images.Media.DATE_MODIFIED)
                val mDirs = ArrayList<String>()
                while (c!!.moveToNext()) {
                    val path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA))
                    val parentFile = File(path).parentFile ?: continue

                    val dir = parentFile.absolutePath
                    if (mDirs.contains(dir))
                        continue

                    mDirs.add(dir)
                    val folderBean = ImgFolderBean()
                    folderBean.setDir(dir)
                    folderBean.fistImgPath = path
                    if (parentFile.list() == null)
                        continue
                    val count = parentFile.list { dir, filename ->
                        filename.endsWith(".jpeg") || filename.endsWith(".jpg") || filename.endsWith(".png")
                    }.size

                    folderBean.count = count
                    folders.add(folderBean)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                c?.close()
            }

            return folders
        }

    /**
     * 获取已安装apk的列表
     */
    //获取到包的管理者
    //获得所有的安装包
    //遍历每个安装包，获取对应的信息
    //得到icon
    //得到程序的名字
    //得到程序的包名
    //得到程序的资源文件夹
    //得到apk的大小
    //获取到安装应用程序的标记
    //表示系统app
    //表示用户app
    //表示在sd卡
    //表示内存
    val appInfos: List<AppInfo>
        get() {

            val appInfos = ArrayList<AppInfo>()
            val packageManager = mContext!!.packageManager
            val installedPackages = packageManager.getInstalledPackages(0)
            for (packageInfo in installedPackages) {

                val appInfo = AppInfo()

                appInfo.applicationInfo = packageInfo.applicationInfo
                appInfo.versionCode = packageInfo.versionCode
                val drawable = packageInfo.applicationInfo.loadIcon(packageManager)
                appInfo.icon = drawable
                val apkName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                appInfo.apkName = apkName
                val packageName = packageInfo.packageName
                appInfo.apkPackageName = packageName
                val sourceDir = packageInfo.applicationInfo.sourceDir
                val file = File(sourceDir)
                val size = file.length()
                appInfo.apkSize = size

                println("---------------------------")
                println("程序的名字:$apkName")
                println("程序的包名:$packageName")
                println("程序的大小:$size")
                val flags = packageInfo.applicationInfo.flags

                appInfo.isUserApp = flags and ApplicationInfo.FLAG_SYSTEM == 0

                appInfo.isRom = flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE == 0


                appInfos.add(appInfo)
            }
            return appInfos
        }

    // 获取视频缩略图
    fun getVideoThumbnail(id: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inDither = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, id.toLong(), MediaStore.Images.Thumbnails.MICRO_KIND, options)
        return bitmap
    }

    /**
     * 通过文件类型得到相应文件的集合
     */
    fun getFilesByType(fileType: Int): List<FileBean> {
        val files = ArrayList<FileBean>()
        // 扫描files文件库
        var c: Cursor? = null
        try {
            c = mContentResolver!!.query(MediaStore.Files.getContentUri("external"), arrayOf("_id", "_data", "_size"), null, null, null)
            val dataindex = c!!.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE)

            while (c.moveToNext()) {
                val path = c.getString(dataindex)

                if (FileUtils.getFileType(path) == fileType) {
                    if (!FileUtils.isExists(path)) {
                        continue
                    }
                    val size = c.getLong(sizeindex)
                    val fileBean = FileBean(path, FileUtils.getFileIconByPath(path))
                    files.add(fileBean)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            c?.close()
        }
        return files
    }

    /**
     * 通过图片文件夹的路径获取该目录下的图片
     */
    fun getImgListByDir(dir: String): List<String> {
        val imgPaths = ArrayList<String>()
        val directory = File(dir)
        if (directory == null || !directory.exists()) {
            return imgPaths
        }
        val files = directory.listFiles()
        for (file in files) {
            val path = file.absolutePath
            if (FileUtils.isPicFile(path)) {
                imgPaths.add(path)
            }
        }
        return imgPaths
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mInstance: FileManager? = null
        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null
        private var mContentResolver: ContentResolver? = null
        private val mLock = Any()

        fun getInstance(context: Context): FileManager {
            if (mInstance == null) {
                synchronized(mLock) {
                    if (mInstance == null) {
                        mInstance = FileManager()
                        mContext = context
                        mContentResolver = context.contentResolver
                    }
                }
            }
            return mInstance!!
        }
    }

}
