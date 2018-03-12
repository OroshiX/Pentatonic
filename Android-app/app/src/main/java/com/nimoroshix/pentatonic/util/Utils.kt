package com.nimoroshix.pentatonic.util

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nimoroshix.pentatonic.model.Cell
import com.nimoroshix.pentatonic.model.DiffOne
import com.nimoroshix.pentatonic.model.Position
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 16/01/2018.
 */

/**
 *  See https://medium.com/@BladeCoder/reducing-parcelable-boilerplate-code-using-kotlin-741c3124a49a
 */
inline fun <reified T> parcelableCreator(
        crossinline create: (Parcel) -> T) =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel) = create(source)
            override fun newArray(size: Int) = arrayOfNulls<T>(size)
        }

inline fun <reified T> parcelableClassLoaderCreator(
        crossinline create: (Parcel, ClassLoader) -> T) =
        object : Parcelable.ClassLoaderCreator<T> {
            override fun createFromParcel(source: Parcel, loader: ClassLoader) = create(source,
                    loader)

            override fun createFromParcel(source: Parcel) = createFromParcel(source,
                    T::class.java.classLoader)

            override fun newArray(size: Int) = arrayOfNulls<T>(size)
        }

inline fun <reified T : Enum<T>> Parcel.readEnum() =
        readInt().let { if (it >= 0) enumValues<T>()[it] else null }

inline fun <reified T : Enum<T>> Parcel.writeEnum(value: T?) =
        writeInt(value?.ordinal ?: -1)

inline fun <T> Parcel.readNullable(reader: () -> T) =
        if (readInt() != 0) reader() else null

inline fun <T> Parcel.writeNullable(value: T?, writer: (T) -> Unit) {
    if (value != null) {
        writeInt(1)
        writer(value)
    } else {
        writeInt(0)
    }
}

/**
 * Takes all the cells that have only one value (as a number), and transform them into the list of
 * all the numeric values
 */
fun Set<Cell>.getOnlyValueList(): List<Int> =
        filter { it.values.size == 1 }.map { it.values[0] }
                .filter { it.isDigit() }.map { it.getNumericValue() }.toList()

/**
 * Takes all the cells that have only one value (as a number), and transform them into the set of
 * all the numeric values
 */
fun Set<Cell>.getOnlyValueSet() =
        filter { it.values.size == 1 }.map { it.values[0] }
                .filter { it.isDigit() }.map { it.getNumericValue() }.toSet()

/**
 * Transform the Char into its numeric value
 * @throws NumberFormatException if the Char does not represent an Int
 */
fun Char.getNumericValue(): Int {
    if (this !in '0'..'9') throw NumberFormatException()
    return this.toInt() - '0'.toInt()
}

fun Parcel.readChar() = readInt().toChar()

fun Parcel.writeChar(value: Char) = writeInt(value.toInt())

fun Parcel.readDate() =
        readNullable { Date(readLong()) }

fun Parcel.writeDate(value: Date?) =
        writeNullable(value) { writeLong(it.time) }

fun Parcel.readBool() = readInt() != 0

fun Parcel.writeBool(value: Boolean) = writeByte(if (value) 1 else 0)

fun Parcel.readBigInteger() =
        readNullable { BigInteger(createByteArray()) }

fun Parcel.writeBigInteger(value: BigInteger?) =
        writeNullable(value) { writeByteArray(it.toByteArray()) }

fun Parcel.readBigDecimal() =
        readNullable { BigDecimal(BigInteger(createByteArray()), readInt()) }

fun Parcel.writeBigDecimal(value: BigDecimal?) = writeNullable(value) {
    writeByteArray(it.unscaledValue().toByteArray())
    writeInt(it.scale())
}

fun <T : Parcelable> Parcel.readTypedObjectCompat(c: Parcelable.Creator<T>): T? =
        readNullable { c.createFromParcel(this) }

fun <T : Parcelable> Parcel.writeTypedObjectCompat(value: T?, parcelableFlags: Int) =
        writeNullable(value) { it.writeToParcel(this, parcelableFlags) }

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun List<DiffOne>.getOtherPosition(nLine: Int, nColumn: Int): Position? {
    forEach {
        when {
            it.position1.nLine == nLine && it.position1.nColumn == nColumn -> return it.position2
            it.position2.nLine == nLine && it.position2.nColumn == nColumn -> return it.position1
        }
    }
    return null
}