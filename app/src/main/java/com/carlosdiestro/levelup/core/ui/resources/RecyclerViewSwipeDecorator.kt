package com.carlosdiestro.levelup.core.ui.resources

import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple utility class to add a background and/or an icon while swiping a RecyclerView item left or right.
 */
class RecyclerViewSwipeDecorator private constructor() {
    private lateinit var canvas: Canvas
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewHolder: RecyclerView.ViewHolder
    private var dX: Float = 0.0f
    private var dY: Float = 0.0f
    private var actionState: Int = 0
    private var isCurrentlyActive: Boolean = false
    private var leftBackgroundColor = 0
    private var leftIconId = 0
    private var leftIconTint: Int? = null
    private var rightBackgroundColor = 0
    private var rightIconId = 0
    private var rightIconTint: Int? = null
    private var iconHorizontalMargin: Int = 0
    private var leftCornerRadius = 0f
    private var rightCornerRadius = 0f
    private var leftPadding: IntArray = intArrayOf(0, 0, 0)
    private var rightPadding: IntArray = intArrayOf(0, 0, 0)

    /**
     * Create a @RecyclerViewSwipeDecorator
     * @param canvas The canvas which RecyclerView is drawing its children
     * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
     * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
     * @param dX The amount of horizontal displacement caused by user's action
     * @param dY The amount of vertical displacement caused by user's action
     * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
     * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
     */
    constructor(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) : this() {
        this.canvas = canvas
        this.recyclerView = recyclerView
        this.viewHolder = viewHolder
        this.dX = dX
        this.dY = dY
        this.actionState = actionState
        this.isCurrentlyActive = isCurrentlyActive
        iconHorizontalMargin = applyDimensionsInt(TypedValue.COMPLEX_UNIT_DIP, 16f)
    }

    /**
     * Set the background color for either (left/right) swipe directions
     * @param backgroundColor The resource id of the background color to be set
     */
    fun setBackgroundColor(backgroundColor: Int) {
        leftBackgroundColor = backgroundColor
        rightBackgroundColor = backgroundColor
    }

    /**
     * Set the action icon for either (left/right) swipe directions
     * @param iconId The resource id of the icon to be set
     */
    fun setIcon(iconId: Int) {
        leftIconId = iconId
        rightIconId = iconId
    }

    /**
     * Set the tint color for either (left/right) action icons
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setIconTint(color: Int) {
        setLeftIconTint(color)
        setRightIconTint(color)
    }

    /**
     * Set the background corner radius for either (left/right) swipe directions
     * @param unit @TypedValue the unit to convert from
     * @param size the radius value
     */
    fun setCornerRadius(unit: Int, size: Float) {
        setLeftCornerRadius(unit, size)
        setRightCornerRadius(unit, size)
    }

    /**
     * Set the background padding for either (left/right) swipe directions
     * @param unit @TypedValue the unit to convert from
     * @param top the top padding value
     * @param side the side padding value
     * @param bottom the bottom padding value
     */
    fun setPadding(unit: Int, top: Float, side: Float, bottom: Float) {
        setLeftPadding(unit, top, side, bottom)
        setRightPadding(unit, top, side, bottom)
    }

    /**
     * Set the background color for left swipe direction
     * @param value The resource id of the background color to be set
     */
    fun setLeftBackgroundColor(value: Int) {
        this.leftBackgroundColor = value
    }

    /**
     * Set the action icon for left swipe direction
     * @param value The resource id of the icon to be set
     */
    fun setSwipeLeftActionIconId(value: Int) {
        this.leftIconId = value
    }

    /**
     * Set the tint color for action icon drawn while swiping left
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setLeftIconTint(color: Int) {
        leftIconTint = color
    }

    /**
     * Set the background color for right swipe direction
     * @param value The resource id of the background color to be set
     */
    fun setRightBackgroundColor(value: Int) {
        this.rightBackgroundColor = value
    }

    /**
     * Set the action icon for right swipe direction
     * @param value The resource id of the icon to be set
     */
    fun setRightIconId(value: Int) {
        this.rightIconId = value
    }

    /**
     * Set the tint color for action icon drawn while swiping right
     * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
     */
    fun setRightIconTint(color: Int) {
        rightIconTint = color
    }

    /**
     * Set the horizontal margin of the icon in the given unit (default is 16dp)
     * @param unit TypedValue
     * @param iconHorizontalMargin the margin in the given unit
     */
    fun setIconHorizontalMargin(unit: Int, iconHorizontalMargin: Int) {
        this.iconHorizontalMargin = applyDimensionsInt(unit, iconHorizontalMargin.toFloat())
    }

    /**
     * Set the background corner radius for left swipe direction
     * @param unit @TypedValue the unit to convert from
     * @param size the radius value
     */
    fun setLeftCornerRadius(unit: Int, size: Float) {
        leftCornerRadius = applyDimensionsFloat(unit, size)
    }

    /**
     * Set the background corner radius for right swipe direction
     * @param unit @TypedValue the unit to convert from
     * @param size the radius value
     */
    fun setRightCornerRadius(unit: Int, size: Float) {
        rightCornerRadius = applyDimensionsFloat(unit, size)
    }

    /**
     * Set the background padding for left swipe direction
     * @param unit @TypedValue the unit to convert from
     * @param top the top padding value
     * @param right the right padding value
     * @param bottom the bottom padding value
     */
    fun setLeftPadding(unit: Int, top: Float, right: Float, bottom: Float) {
        leftPadding = intArrayOf(
            applyDimensionsInt(unit, top),
            applyDimensionsInt(unit, right),
            applyDimensionsInt(unit, bottom)
        )
    }

    /**
     * Set the background padding for right swipe direction
     * @param unit @TypedValue the unit to convert from
     * @param top the top padding value
     * @param left the left padding value
     * @param bottom the bottom padding value
     */
    fun setRightPadding(unit: Int, top: Float, left: Float, bottom: Float) {
        rightPadding = intArrayOf(
            applyDimensionsInt(unit, top),
            applyDimensionsInt(unit, left),
            applyDimensionsInt(unit, bottom)
        )
    }

    private fun applyDimensionsInt(unit: Int, direction: Float): Int {
        return TypedValue.applyDimension(unit, direction, recyclerView.context.resources.displayMetrics).toInt()
    }

    private fun applyDimensionsFloat(unit: Int, direction: Float): Float {
        return TypedValue.applyDimension(unit, direction, recyclerView.context.resources.displayMetrics)
    }

    private fun getColorBoundRadii(isSwipingLeft: Boolean, needsRadii: Boolean): ColorBoundRadii {
        val color = if(isSwipingLeft) leftBackgroundColor else rightBackgroundColor
        val leftBound = if(isSwipingLeft) viewHolder.itemView.right + dX.toInt() else viewHolder.itemView.left + rightPadding[1]
        val topBound = if(isSwipingLeft) viewHolder.itemView.top + leftPadding[0] else viewHolder.itemView.top + rightPadding[0]
        val rightBound = if(isSwipingLeft) viewHolder.itemView.right - leftPadding[1] else viewHolder.itemView.left + dX.toInt()
        val bottomBound = if(isSwipingLeft) viewHolder.itemView.bottom - leftPadding[2] else viewHolder.itemView.bottom - rightPadding[2]
        val cornerRadii = if(needsRadii) {
            if(isSwipingLeft) {
                floatArrayOf(
                    0f,
                    0f,
                    leftCornerRadius,
                    leftCornerRadius,
                    leftCornerRadius,
                    leftCornerRadius,
                    0f,
                    0f
                )
            } else {
                floatArrayOf(
                    leftCornerRadius,
                    leftCornerRadius,
                    0f,
                    0f,
                    0f,
                    0f,
                    leftCornerRadius,
                    leftCornerRadius
                )
            }
        } else {
            null
        }

        return ColorBoundRadii(
            color,
            leftBound,
            topBound,
            rightBound,
            bottomBound,
            cornerRadii
        )
    }

    private fun applyColorDrawable(isSwipingLeft: Boolean) {
        val (color, left, top, right, bottom, _) = getColorBoundRadii(isSwipingLeft, false)
        ColorDrawable(color).apply {
            setBounds(left, top, right, bottom)
        }.also { it.draw(canvas) }
    }

    private fun applyGradientDrawable(isSwipingLeft: Boolean) {
        val (color, left, top, right, bottom, radii) = getColorBoundRadii(isSwipingLeft, true)
        GradientDrawable().apply {
            setColor(color)
            setBounds(left, top, right, bottom)
            this.cornerRadii = radii
        }.also { it.draw(canvas) }
    }

    /**
     * Decorate the RecyclerView item with the chosen backgrounds and icons
     */
    fun decorate() {
        try {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return
            if (dX > 0) {
                // Swiping Right
                canvas.clipRect(
                    viewHolder.itemView.left,
                    viewHolder.itemView.top,
                    viewHolder.itemView.left + dX.toInt(),
                    viewHolder.itemView.bottom
                )
                if (rightBackgroundColor != 0) {
                    if (rightCornerRadius != 0f) {
                        applyGradientDrawable(false)
                    } else {
                        applyColorDrawable(false)
                    }
                }
                val iconSize: Int
                if (rightIconId != 0 && dX > iconHorizontalMargin) {
                    val icon =
                        ContextCompat.getDrawable(recyclerView.context, rightIconId)
                    if (icon != null) {
                        iconSize = icon.intrinsicHeight
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)
                        icon.setBounds(
                            viewHolder.itemView.left + iconHorizontalMargin + rightPadding[1],
                            top,
                            viewHolder.itemView.left + iconHorizontalMargin + rightPadding[1] + icon.intrinsicWidth,
                            top + icon.intrinsicHeight
                        )
                        if (rightIconTint != null) icon.setColorFilter(
                            rightIconTint!!, PorterDuff.Mode.SRC_IN
                        )
                        icon.draw(canvas)
                    }
                }
            } else if (dX < 0) {
                // Swiping Left
                canvas.clipRect(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top,
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom
                )
                if (leftBackgroundColor != 0) {
                    if (leftCornerRadius != 0f) {
                        applyGradientDrawable(true)
                    } else {
                        applyColorDrawable(true)
                    }
                }
                val iconSize: Int
                if (leftIconId != 0 && dX < -iconHorizontalMargin) {
                    val icon =
                        ContextCompat.getDrawable(recyclerView.context, leftIconId)
                    if (icon != null) {
                        iconSize = icon.intrinsicHeight
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)

                        icon.setBounds(
                            viewHolder.itemView.right - iconHorizontalMargin - leftPadding[1] - halfIcon * 2,
                            top,
                            viewHolder.itemView.right - iconHorizontalMargin - leftPadding[1],
                            top + icon.intrinsicHeight
                        )
                        if (leftIconTint != null) icon.setColorFilter(
                            leftIconTint!!, PorterDuff.Mode.SRC_IN
                        )
                        icon.draw(canvas)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message!!)
        }
    }

    /**
     * A Builder for the RecyclerViewSwipeDecorator class
     */
    class Builder(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        private val mDecorator: RecyclerViewSwipeDecorator

        /**
         * Create a builder for a RecyclerViewsSwipeDecorator
         * @param canvas The canvas which RecyclerView is drawing its children
         * @param recyclerView The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder The ViewHolder which is being interacted by the User or it was interacted and simply animating to its original position
         * @param dX The amount of horizontal displacement caused by user's action
         * @param dY The amount of vertical displacement caused by user's action
         * @param actionState The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
         * @param isCurrentlyActive True if this view is currently being controlled by the user or false it is simply animating back to its original state
         */
        init {
            mDecorator = RecyclerViewSwipeDecorator(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }

        /**
         * Add a background color to both swiping directions
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addBackgroundColor(color: Int): Builder {
            mDecorator.setBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon to both swiping directions
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addActionIcon(drawableId: Int): Builder {
            mDecorator.setIcon(drawableId)
            return this
        }

        /**
         * Set the tint color for either (left/right) action icons
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setActionIconTint(color: Int): Builder {
            mDecorator.setIconTint(color)
            return this
        }

        /**
         * Add a corner radius to swipe background for both swipe directions
         * @param unit @TypedValue the unit to convert from
         * @param size the corner radius in the given unit
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addCornerRadius(unit: Int, size: Int): Builder {
            mDecorator.setCornerRadius(unit, size.toFloat())
            return this
        }

        /**
         * Add padding to the swipe background for both swipe directions
         * @param unit @TypedValue the unit to convert from
         * @param top the top padding value
         * @param side the side padding value
         * @param bottom the bottom padding value
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addPadding(unit: Int, top: Float, side: Float, bottom: Float): Builder {
            mDecorator.setPadding(unit, top, side, bottom)
            return this
        }

        /**
         * Add a background color while swiping right
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightBackgroundColor(color: Int): Builder {
            mDecorator.setRightBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping right
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightActionIcon(drawableId: Int): Builder {
            mDecorator.setRightIconId(drawableId)
            return this
        }

        /**
         * Set the tint color for action icon shown while swiping right
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeRightActionIconTint(color: Int): Builder {
            mDecorator.setRightIconTint(color)
            return this
        }

        /**
         * Adds a background color while swiping left
         * @param color A single color value in the form 0xAARRGGBB
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftBackgroundColor(color: Int): Builder {
            mDecorator.setLeftBackgroundColor(color)
            return this
        }

        /**
         * Add an action icon while swiping left
         * @param drawableId The resource id of the icon to be set
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeLeftActionIconId(drawableId)
            return this
        }

        /**
         * Set the tint color for action icon shown while swiping left
         * @param color a color in ARGB format (e.g. 0xFF0000FF for blue)
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setSwipeLeftActionIconTint(color: Int): Builder {
            mDecorator.setLeftIconTint(color)
            return this
        }

        /**
         * Set the horizontal margin of the icon in the given unit (default is 16dp)
         * @param unit @TypedValue the unit to convert from
         * @param iconHorizontalMargin the margin in the given unit
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun setIconHorizontalMargin(unit: Int, iconHorizontalMargin: Int): Builder {
            mDecorator.setIconHorizontalMargin(unit, iconHorizontalMargin)
            return this
        }

        /**
         * Set the background corner radius for left swipe direction
         * @param unit @TypedValue the unit to convert from
         * @param size the radius value
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftCornerRadius(unit: Int, size: Float): Builder {
            mDecorator.setLeftCornerRadius(unit, size)
            return this
        }

        /**
         * Set the background corner radius for right swipe direction
         * @param unit @TypedValue the unit to convert from
         * @param size the radius value
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightCornerRadius(unit: Int, size: Float): Builder {
            mDecorator.setRightCornerRadius(unit, size)
            return this
        }

        /**
         * Add padding to the swipe background for left swipe direction
         * @param unit @TypedValue the unit to convert from
         * @param top the top padding value
         * @param right the right padding value
         * @param bottom the bottom padding value
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeLeftPadding(unit: Int, top: Float, right: Float, bottom: Float): Builder {
            mDecorator.setLeftPadding(unit, top, right, bottom)
            return this
        }

        /**
         * Add padding to the swipe background for right swipe direction
         * @param unit @TypedValue the unit to convert from
         * @param top the top padding value
         * @param left the left padding value
         * @param bottom the bottom padding value
         *
         * @return This instance of @RecyclerViewSwipeDecorator.Builder
         */
        fun addSwipeRightPadding(unit: Int, top: Float, left: Float, bottom: Float): Builder {
            mDecorator.setRightPadding(unit, top, left, bottom)
            return this
        }

        /**
         * Create a RecyclerViewSwipeDecorator
         * @return The created @RecyclerViewSwipeDecorator
         */
        fun create(): RecyclerViewSwipeDecorator {
            return mDecorator
        }
    }
}

private data class ColorBoundRadii(
    val color: Int,
    val leftBound: Int,
    val topBound: Int,
    val rightBound: Int,
    val bottomBound: Int,
    val cornerRadii: FloatArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ColorBoundRadii

        if (color != other.color) return false
        if (leftBound != other.leftBound) return false
        if (topBound != other.topBound) return false
        if (rightBound != other.rightBound) return false
        if (bottomBound != other.bottomBound) return false
        if (cornerRadii != null) {
            if (other.cornerRadii == null) return false
            if (!cornerRadii.contentEquals(other.cornerRadii)) return false
        } else if (other.cornerRadii != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color
        result = 31 * result + leftBound
        result = 31 * result + topBound
        result = 31 * result + rightBound
        result = 31 * result + bottomBound
        result = 31 * result + (cornerRadii?.contentHashCode() ?: 0)
        return result
    }
}