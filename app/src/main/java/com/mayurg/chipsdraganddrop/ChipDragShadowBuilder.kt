package com.mayurg.chipsdraganddrop

import android.graphics.Canvas
import android.graphics.Point
import android.view.View
import androidx.core.content.res.ResourcesCompat

class ChipDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

    //set shadow to be the drawable
    private val shadow = ResourcesCompat.getDrawable(
        view.context.resources,
        R.drawable.shadow_bg,
        view.context.theme
    )

    // Defines a callback that sends the drag shadow dimensions and touch point back to the
    // system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        // Sets the width of the shadow to full width of the original View
        val width: Int = view.width

        // Sets the height of the shadow to full height of the original View
        val height: Int = view.height

        // The drag shadow is a Drawable. This sets its dimensions to be the same as the
        // Canvas that the system will provide. As a result, the drag shadow will fill the
        // Canvas.
        shadow?.setBounds(0, 0, width, height)

        // Sets the size parameter's width and height values. These get back to the system
        // through the size parameter.
        size.set(width, height)

        // Sets the touch point's position to be in the middle of the drag shadow
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system constructs
    // from the dimensions passed in onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {
        // Draws the Drawable in the Canvas passed in from the system.
        shadow?.draw(canvas)
    }
}