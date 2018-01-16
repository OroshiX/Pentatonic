package com.nimoroshix.pentatonic.action

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 16/01/2018.
 */
interface StringSerializable {
    fun toStringSerialization(): String
    fun fromStringSerialization(serialization: String)
}