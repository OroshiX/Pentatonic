package com.nimoroshix.pentatonic.action

import com.nimoroshix.pentatonic.model.Position

/**
 * Project Pentatonic
 *
 * Created by OroshiX on 15/01/2018.
 */
abstract class MultipleAction(var action: String, var positions: List<Position>) : Action