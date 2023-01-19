package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.udacity.util.sendNotification
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var buttonColor = 0
    private var text = ""
    private var textColor = 0
    private var loadingColor = 0
    private var circleColor = 0

    private var loadingProgress: Int = 0
    private var loadingArc = RectF()

    private val valueAnimator = ValueAnimator.ofInt(0, 1000)

    val notificationManager = ContextCompat.getSystemService(
        context, NotificationManager::class.java
    ) as NotificationManager

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                valueAnimator.duration = 30000
                valueAnimator.addUpdateListener { valueAnimator ->
                    loadingProgress = valueAnimator.animatedValue as Int
                    invalidate()
                }
                valueAnimator.doOnStart {
                    isEnabled = false
                    text = resources.getString(R.string.button_loading)
                    invalidate()
                }
                valueAnimator.doOnEnd {
                    buttonState = ButtonState.Completed
                    loadingProgress = 0
                    notificationManager.sendNotification(
                        context.getText(R.string.notification_description).toString(), context
                    )
                }
                valueAnimator.start()
            }
            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
                isEnabled = false
            }
            ButtonState.Completed -> {
                isEnabled = true
                text = resources.getString(R.string.button_name)
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
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            text = getString(R.styleable.LoadingButton_text).toString()
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, 0)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val textXPos = (width).div(2f)
        val textYPos =
            ((height).div(2).minus((this.paint.descent() + this.paint.ascent()) / 2))
        canvas.drawColor(buttonColor)

        this.paint.color = loadingColor
        val progressRect = loadingProgress / 1000f * widthSize
        canvas.drawRect(0f, 0f, progressRect, heightSize.toFloat(), this.paint)

        this.paint.color = textColor
        canvas.drawText(
            text, textXPos, textYPos, this.paint
        )

        this.paint.color = circleColor
        val sweepAngle = loadingProgress / 1000f * 360f
        canvas.drawArc(
            loadingArc, 0f, sweepAngle, true, this.paint
        )


    }

    fun buttonClicked() {
        buttonState = ButtonState.Clicked
    }


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)

        loadingArc = RectF(
            widthSize * 0.75f - 30f,
            heightSize / 2 - 30f,
            widthSize * 0.75f + 30f,
            heightSize / 2 + 30f
        )
    }

    fun completeLoading() {
        val currentAnimatedFraction = valueAnimator.animatedFraction
        valueAnimator.cancel()
        valueAnimator.setCurrentFraction(currentAnimatedFraction)
        valueAnimator.duration = 950
        valueAnimator.start()
    }


}