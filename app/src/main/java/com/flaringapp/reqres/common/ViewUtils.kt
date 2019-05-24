package com.flaringapp.reqres.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue

class ViewUtils {
    companion object {
        fun dp(context: Context, size: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics)
        }

        fun getPlaceholderDrawable(): Drawable {
            return ColorDrawable(Color.TRANSPARENT)
        }
    }
}