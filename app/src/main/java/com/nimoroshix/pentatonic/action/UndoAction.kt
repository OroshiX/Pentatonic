package com.nimoroshix.pentatonic.action

import android.os.Parcel
import android.os.Parcelable
import com.nimoroshix.pentatonic.model.Grid
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.deserializeActions
import com.nimoroshix.pentatonic.serializer.Serializer.Companion.serializeActions
import com.nimoroshix.pentatonic.util.parcelableCreator

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
class UndoAction() : Parcelable {
    private var current = 0
    var actions = mutableListOf<Action>()

    constructor(parcel: Parcel) : this() {
        current = parcel.readInt()
        val actionString = listOf<String>().apply { parcel.readStringList(this) }
        actions = deserializeActions(actionString)
    }

    fun addAction(action: Action) {
        when {
            current == actions.size -> {
                // Nominal case: no undos or redos done, just add the action
                actions.add(action)
                current++
            }
            canRedo() -> {
                // We had an or several undo(s) so add the action, and remove all actions after the added action
                actions[current] = action
                current++
                val removeIterator = actions.listIterator(current)
                while (removeIterator.hasNext()) {
                    removeIterator.next()
                    removeIterator.remove()
                }
            }
        }
    }

    fun canUndo(): Boolean {
        return current > 0 && actions.size > 0
    }

    fun canRedo(): Boolean {
        return current < actions.size
    }

    fun undo(grid: Grid): Boolean {
        if (!canUndo()) return false
        current--
        return actions[current].applyUndo(grid)
    }

    fun redo(grid: Grid): Boolean {
        if (!canRedo()) return false
        val res = actions[current].applyRedo(grid)
        current++
        return res
    }

    fun clear() {
        current = 0
        actions = mutableListOf()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(current)
        parcel.writeStringList(serializeActions(actions))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::UndoAction)
    }
}