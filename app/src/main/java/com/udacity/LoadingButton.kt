package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonColor = 0
    private var text = ""

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                ValueAnimator.ofInt(0, 1000).apply {
                    addUpdateListener {
                        invalidate()
                    }
                    duration = 3000
                    doOnStart {
                        isEnabled = false
                        text = resources.getString(R.string.button_loading)
                        buttonColor = resources.getColor(R.color.colorAccent)
                        invalidate()
                    }
                    doOnEnd {
                        buttonState = ButtonState.Completed
                    }
                    start()
                }
            }
            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
                isEnabled = false
            }
            ButtonState.Completed -> {
                isEnabled = true
                text = resources.getString(R.string.button_name)
                buttonColor = resources.getColor(R.color.colorPrimary)
                invalidate()
            }
        }
    }

    private val paint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER
    }


    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(
                R.styleable.LoadingButton_buttonColor, 0
            )
            text = getString(R.styleable.LoadingButton_text).toString()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val textXPos = (canvas?.width)?.div(2)
        val textYPos = ((canvas?.height)?.div(2)?.minus((paint.descent() + paint.ascent()) / 2))
        canvas?.drawColor(buttonColor)
        canvas?.drawText(
            text, textXPos!!.toFloat(), textYPos!!.toFloat(), paint
        )
    }

    override fun performClick(): Boolean {
        buttonState = ButtonState.Clicked
        return true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


}