package com.carlosdiestro.levelup.core.ui.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.Editable
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.carlosdiestro.levelup.MainActivity
import com.carlosdiestro.levelup.core.ui.resources.StringResource
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.Flow
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

fun View.gone(isGone: Boolean = true) {
    if (isGone) this.visibility = View.GONE
    else this.visibility = View.VISIBLE
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

fun Fragment.showWarningDialog(messageId: Int) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(StringResource.Warning.resId)
        .setMessage(messageId)
        .setNegativeButton(StringResource.Close.resId) { d, _ -> d.dismiss() }
        .create()
        .show()
}

fun <T, VH : RecyclerView.ViewHolder> RecyclerView.setUp(recyclerAdapter: ListAdapter<T, VH>) {
    this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    this.adapter = recyclerAdapter
}

fun ViewPager2.setUp(
    viewPagerAdapter: FragmentStateAdapter,
    tabLayout: TabLayout,
    tabText: (Int) -> String
) {
    this.adapter = viewPagerAdapter
    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = tabText(position)
    }.attach()
}

fun Menu.menuItemAsMaterialButton(@IdRes id: Int, buttonText: String, onClick: () -> Unit) {
    (this.findItem(id).actionView as MaterialButton).apply {
        text = buttonText
        setOnClickListener { onClick() }
    }
}

fun Fragment.setUpMenuProvider(provider: MenuProvider) {
    requireActivity().addMenuProvider(provider, viewLifecycleOwner, Lifecycle.State.RESUMED)
}

fun Fragment.setActionBarTitle(text: String) {
    (requireActivity() as MainActivity).supportActionBar?.title = text.uppercase()
}

fun Fragment.showPopUpMenu(
    anchorView: View,
    @MenuRes menuId: Int,
    gravity: Int = Gravity.END,
    onMenuItemClicked: (MenuItem) -> Boolean
) {
    PopupMenu(requireContext(), anchorView).apply {
        menuInflater.inflate(menuId, this.menu)
        this.gravity = gravity
        setOnMenuItemClickListener { onMenuItemClicked(it) }
    }.also {
        it.show()
    }
}

