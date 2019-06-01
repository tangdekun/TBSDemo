import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * @Author tangdekun
 * @Date 2019/6/1-11:23
 * @Email tangdekun0924@gmail.com
 */

data class ImageFolderBean(var name: String?,
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeString(album)
        parcel.writeString(artist)
        parcel.writeLong(size)
        parcel.writeInt(duration)
        parcel.writeString(pinyin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<ImageFolderBean> {
        override fun createFromParcel(parcel: Parcel): ImageFolderBean {
            return ImageFolderBean(parcel)
        }

        override fun newArray(size: Int): Array<ImageFolderBean?> {
            return arrayOfNulls(size)
        }
    }

}