package io.monkeypatch.talks.mobile.waterpouring.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import io.monkeypatch.talks.mobile.waterpouring.GlassAnimation
import io.monkeypatch.talks.mobile.waterpouring.GlassPosition
import io.monkeypatch.talks.mobile.waterpouring.R
import io.monkeypatch.talks.mobile.waterpouring.model.filled
import io.monkeypatch.talks.mobile.waterpouring.model.sized
import io.monkeypatch.talks.waterpouring.model.Glass
import kotlinx.android.synthetic.main.view_level_glass.view.*


interface OnGlassEvent {
    fun onClick(glassPosition: GlassPosition)
    fun onDoubleClick(glassPosition: GlassPosition)
}

class GlassLayout : FrameLayout, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private val TAG = GlassLayout::class.simpleName

    private lateinit var gesture: GestureDetectorCompat

    var onGlassEvent: OnGlassEvent? = null
    lateinit var glassPosition: GlassPosition

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.view_level_glass, this)
        gesture = GestureDetectorCompat(context, this)
        gesture.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean =
            gesture.onTouchEvent(event)

    fun glassChanged(newGlass: Glass) {
        updateState(newGlass)
        updateScale(newGlass)
    }

    fun setLevels(glasses: GlassAnimation, stepNotifier: () -> Unit) {
        var (currents, levels) = glasses.unzip()
        Log.i(TAG, "Animation start with $levels and $currents")
        glassView.setLevels(levels = *levels.toFloatArray(),
                listener = object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator?) {
                        Log.i(TAG, "set text to ${currents[0]}")
                        levelView.text = "${currents[0]}"
                        currents = currents.drop(1)
                        stepNotifier()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        if (!currents.isEmpty()) {
                            Log.i(TAG, "set text to ${currents.last()}")
                            levelView.text = "${currents.last()}"
                            stepNotifier()
                        }
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    fun updateState(glass: Glass) {
        levelView.text = "${glass.current}/${glass.capacity}"
        glassView.setLevels(glass.filled())
    }

    fun updateScale(glass: Glass) {
        scale(glass.sized())
    }

    private fun scale(scale: Float) {
        val scaled = scale.coerceIn(0F..1F)
        val currentScale = glassView.scaleX
        val valueAnimator = ValueAnimator.ofFloat(currentScale, scaled)
        valueAnimator.addUpdateListener {
            val current = it.animatedValue as Float

            glassView.apply {
                scaleX = current
                scaleY = current
                // Pivot = bottom center
                pivotX = width.toFloat() / 2 // 50%
                pivotY = height.toFloat()    // 100%
            }
        }
        valueAnimator.start()
    }


    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        onGlassEvent?.onClick(glassPosition)
        return true
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        onGlassEvent?.onDoubleClick(glassPosition)
        return true
    }

    override fun onShowPress(e: MotionEvent?) = Unit
    override fun onDown(e: MotionEvent?): Boolean = true
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) = false
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onLongPress(e: MotionEvent?) = Unit
    override fun onDoubleTapEvent(e: MotionEvent?) = false


}

