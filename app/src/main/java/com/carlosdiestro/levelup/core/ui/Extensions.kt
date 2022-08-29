package com.carlosdiestro.levelup.core.ui

import android.text.Editable
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

fun <T, U> Flow<T>.diff(
    lifecycleOwner: LifecycleOwner,
    mapf: (T) -> U,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (U) -> Unit
) {
    lifecycleOwner.launchAndCollect(
        state = state,
        flow = map(mapf).distinctUntilChanged(),
        body = body
    )
}

fun View.visible(visibility: Boolean = true) {
    if (visibility) this.visibility = View.VISIBLE
    else this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Editable?.toTrimmedString(): String = this.toString().trim()
