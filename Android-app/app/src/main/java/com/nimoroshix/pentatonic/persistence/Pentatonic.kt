package com.nimoroshix.pentatonic.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Project Pentatonic
 * Created by OroshiX on 13/01/2018.
 */
@Entity(tableName = "pentatonic")
class Pentatonic(var lines: Int, var columns: Int) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "has_sister")
    var hasSister: Boolean = false

    @ColumnInfo(name = "has_diff_one")
    var hasDiffOne: Boolean = false

    var version: Int = 0
    var areas: String = ""
    var enonce: String = ""
    var progress: String? = null
    var difficulty: Int = 0
    var author: String = ""
    var filename: String = ""
}