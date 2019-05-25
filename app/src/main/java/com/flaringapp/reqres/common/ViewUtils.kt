package com.flaringapp.reqres.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.flaringapp.reqres.R


class ViewUtils {
    companion object {
        fun dp(context: Context, size: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics)
        }

        fun getPlaceholderDrawable(): Drawable {
            return ColorDrawable(Color.TRANSPARENT)
        }

        fun sendEmail(context: Context, email: String, message: String = "") {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_EMAIL, email)
            intent.putExtra(Intent.EXTRA_TEXT, message)

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)))
        }
    }
}