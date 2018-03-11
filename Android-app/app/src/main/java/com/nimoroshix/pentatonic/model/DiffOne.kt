package com.nimoroshix.pentatonic.model

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.util.parcelableCreator

/**
 * Project Android-app
 * Created by OroshiX on 10/03/2018.
 */
class DiffOne(var position1: Position, var position2: Position) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Position::class.java.classLoader),
            parcel.readParcelable(Position::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(position1, flags)
        parcel.writeParcelable(position2, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiffOne

        if (position1 != other.position1) return false
        if (position2 != other.position2) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position1.hashCode()
        result = 31 * result + position2.hashCode()
        return result
    }

    override fun toString(): String {
        return "DiffOne($position1, $position2)"
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::DiffOne)
    }
}