package com.mb.kibeti.forgot_password.utils

import android.os.CountDownTimer

object TimerUtils {

    fun createCountdownTimer(
        duration: Long,
        interval: Long,
        onTick: (Long) -> Unit,
        onFinish: () -> Unit
    ): CountDownTimer {
        return object : CountDownTimer(duration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }
    }
}
