package me.superpenguin.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CHEATED_KEY = "CHEATED"

class CheaterViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    var didCheat: Boolean
        get() = savedStateHandle[CHEATED_KEY] ?: false
        set(value) = savedStateHandle.set(CHEATED_KEY, value)


}