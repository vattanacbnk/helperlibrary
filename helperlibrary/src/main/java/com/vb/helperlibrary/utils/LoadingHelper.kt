/*
 * @Created by piseysen on 2/22/19 11:03 PM
 */

package com.vb.helperlibrary.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.vb.helperlibrary.R
import java.util.*

class LoadingHelper private constructor(context: Context, isCancelable: Boolean = false) {
    private val dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.progress)
        dialog.setCancelable(isCancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun withAlpha(alpha: Float): LoadingHelper {
        dialog.window?.attributes?.dimAmount = alpha
        return this
    }

    fun setUserCancelable(cancelable: Boolean): LoadingHelper {
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        return this
    }

    fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    fun dismiss() {
        try {
            dialog.dismiss()
        } catch (ignored: Exception) {

        }
    }

    companion object {
        private val LHs = IdentityHashMap<Context, LoadingHelper>()
        fun with(ctx: Context, isCancelable: Boolean = false): LoadingHelper {
            var lh = LHs[ctx]
            if (lh == null) {
                lh = LoadingHelper(ctx, isCancelable)
                LHs[ctx] = lh
            }
            return lh
        }

        fun dismiss() {
            for (d in LHs.values) {
                d.dismiss()
            }
        }
    }
}