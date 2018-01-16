package com.nimoroshix.pentatonic.action

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.model.Position
import com.nimoroshix.pentatonic.util.parcelableCreator
import com.nimoroshix.pentatonic.util.readBool
import com.nimoroshix.pentatonic.util.writeBool

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class PositionReplace(nLine: Int, nColumn: Int, var deduplicated: Boolean = false) : Position(nLine, nColumn), Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readBool())


    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(nLine)
        writeInt(nColumn)
        writeBool(deduplicated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::PositionReplace)
    }

    override fun toStringSerialization(): String {
        TODO("not implemented")
    }

    override fun fromStringSerialization(serialization: String) {
        TODO("not implemented")
    }
}