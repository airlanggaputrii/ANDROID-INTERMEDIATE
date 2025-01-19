package com.bangkit.intermediateandroid.submission1intermediate2.a.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener

class EdPassword : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Menambahkan Hint
        hint = "Password"

        // Menambahkan text aligmnet pada editText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START

    }

    private fun init() {
        addTextChangedListener(onTextChanged = { p0, _, _, _ ->
            val input = p0.toString().trim()
            if (input.length < 8) {
                error = "Password must be more than 8 characters"
            } else {
                error = null
            }
        })
    }

}
