package com.carlosdiestro.levelup.core.ui.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.Editable
import android.view.View
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
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

fun <T> ViewModel.launchAndCollect(
    flow: Flow<T>,
    body: (T) -> Unit
) {
    viewModelScope.launch {
        flow.collect(body)
    }
}

fun View.visible(visibility: Boolean = true) {
    if (visibility) this.visibility = View.VISIBLE
    else this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Editable?.toTrimmedString(): String = this.toString().trim()

fun BottomNavigationView.slide(isSlideUp: Boolean) {
    this.animate()
        .translationYBy(if (isSlideUp) this.height.toFloat() else 0F)
        .translationY(if (isSlideUp) 0F else this.height.toFloat())
        .setDuration(50)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (isSlideUp) visible() else gone()
            }
        })
}
