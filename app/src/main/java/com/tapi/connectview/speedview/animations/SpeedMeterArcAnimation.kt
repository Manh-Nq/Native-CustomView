package com.tapi.connectview.speedview.animations

import android.animation.ValueAnimator
import com.tapi.connectview.speedview.view.SpeedometerView
import com.tapi.connectview.Constance

class SpeedMeterArcAnimation(val arcView: SpeedometerView) {
    private var animation: ValueAnimator? = null
    private var isCanceled = false

    fun cancel() {
        isCanceled = true
    }

    fun start(fromAngle: Float, toAngle: Float) {
        isCanceled = false
        animation?.cancel()
        animation = ValueAnimator.ofFloat(fromAngle, toAngle)
        animation?.apply {
            duration = 500
            addUpdateListener {
                if (!isCanceled) {
                    val currValue = it.animatedValue as Float
                    arcView.setSweepAngle(currValue)
                    arcView.invalidate()
                }
            }
            start()
        }
    }

    fun startFlash() {
        isCanceled = false
        arcView.setSweepAngle(Constance.MAX_ANGLE)
        arcView.invalidate()
    }

}