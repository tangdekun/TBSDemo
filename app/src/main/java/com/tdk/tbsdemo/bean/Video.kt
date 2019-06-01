import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @Author tangdekun
 * @Date 2019/6/1-11:23
 * @Email tangdekun0924@gmail.com
 */
@SuppressLint("ParcelCreator")// 用于处理 Lint 的错误提示
@Parcelize
data class Video(var name: String?,
                 /**歌曲名*/
                 var path: String?,
                 /**路径*/
                 var album: String?,
                 /**所属专辑*/
                 var artist: String?,
                 /**艺术家(作者)*/
                 var size: Long,
                 /**文件大小*/
                 var duration: Int,
                 /**时长*/
                 var pinyin: String
        /**歌曲名的拼音，用于字母排序*/
) : Parcelable