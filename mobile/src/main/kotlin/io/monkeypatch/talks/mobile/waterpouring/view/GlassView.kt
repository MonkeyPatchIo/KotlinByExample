package io.monkeypatch.talks.mobile.waterpouring.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import io.monkeypatch.talks.mobile.waterpouring.R

/**
 * Display Glass View. No need to change that class.
 */
class GlassView : AppCompatImageView {

    private val TAG = GlassView::class.simpleName

    private val drinkLevelDrawable: Drawable
    private var drawableBounds = Rect()
    private var drawableHeight = 0
    private var currentAnimation: ValueAnimator? = null
    private val defaultAnimationDuration: Int
    private var _level: Float = 0f
        set(value) {
            val newValue = value.coerceIn(0f, 1f)
            if (newValue != field) {
                field = newValue
            }
        }
    private var level: Float = _level
        get() = _level
        set(value) {
            if (field != value) {
                val oldValue = field
                _level = value
                animateDrink(oldValue, _level)
            }
        }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val drinkDrawable = ContextCompat.getDrawable(context, R.drawable.layer_drink) as LayerDrawable
        setImageDrawable(drinkDrawable)
        drinkLevelDrawable = drinkDrawable.getDrawable(0)
        defaultAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        addOnLayoutChangeListener({ _, _, _, _, _, _, _, _, _ ->
            drawableBounds = drinkDrawable.bounds
            drawableHeight = drawableBounds.height()
            setLevelView(level)
        })

    }

    private fun animateDrink(vararg animations: Pair<Float, Float>,
                             stepListener: AnimatorListenerAdapter? = null,
                             duration: Int = defaultAnimationDuration) {
        if (!animations.isEmpty()) {
            val first = animations.first()
            animateDrink(
                    first.first,
                    first.second,
                    object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator?) {
                            stepListener?.onAnimationStart(animation)
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                            animation?.removeAllListeners()
                            stepListener?.onAnimationCancel(animation)
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            val nextAnim: Array<Pair<Float, Float>> = animations.drop(1).toTypedArray()
                            stepListener?.onAnimationEnd(animation)
                            animateDrink(animations = *nextAnim,
                                    stepListener = stepListener,
                                    duration = duration)
                        }
                    }, duration
            )
        }
    }

    private fun animateDrink(fromPercent: Float,
                             toPercent: Float,
                             animatorListener: Animator.AnimatorListener? = null,
                             duration: Int = defaultAnimationDuration) {
        if (!isShown) {
            return
        }

        val animation = ValueAnimator.ofFloat(fromPercent, toPercent)
        with(animation) {
            addUpdateListener { anim ->
                _level = anim.animatedValue as Float
                setLevelView(level)
            }
            addListener(animatorListener)
            setTarget(drinkLevelDrawable)
            this.duration = duration.toLong()
            this.interpolator = AccelerateDecelerateInterpolator()
        }
        currentAnimation?.cancel()
        currentAnimation = animation
        currentAnimation?.start()
    }

    private fun setLevelView(level: Float) {
        val newSize = (drawableHeight * (1f - level)).toInt()
        val bounds = drinkLevelDrawable.copyBounds()
        bounds.top = newSize
        drinkLevelDrawable.bounds = bounds
        drinkLevelDrawable.invalidateSelf()
    }

    fun setLevels(vararg levels: Float, listener: AnimatorListenerAdapter? = null) {
        if (isShown) {
            val duration = if (levels.size == 1) defaultAnimationDuration else 2000
            val pairs: List<Pair<Float, Float>> = levels.mapIndexed { index, value ->
                val idx = if (index == 0) level else levels[index - 1]
                Pair(idx, value)
            }
            animateDrink(animations = *pairs.toTypedArray(),
                    stepListener = listener,
                    duration = duration)

        } else {
            level = levels.lastOrNull() ?: 0f
        }
    }
}