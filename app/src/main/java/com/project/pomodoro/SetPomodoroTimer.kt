package com.project.pomodoro

import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.TextView

class SetPomodoroTimer(
    private var studyTime: Int,
    private var breakTime: Int,
    private var studyTextView: TextView,
    private var breakTextView: TextView,
    private var vibrator: Vibrator
) {
    private var studyTimer: CountDownTimer? = null
    private var breakTimer: CountDownTimer? = null
    private var minutes: Long = 0L
    private var seconds: Long = 0L
    private var remainingStudyTime: Long = (studyTime * 60 * 1000).toLong()
    private var remainingBreakTime: Long = (breakTime * 60 * 1000).toLong()
    private var isStudyTime = true
    private var cycle: Int = 0


    private var totalStudyTime: Int = 0


    fun startTimer() {
        if (isStudyTime) {
            startStudyTimer()
        } else {
            startBreakTimer()
        }
    }

    private fun startStudyTimer() {

        studyTimer = object : CountDownTimer(remainingStudyTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingStudyTime = millisUntilFinished
                minutes = millisUntilFinished / 60000
                seconds = (millisUntilFinished % 60000) / 1000
                studyTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                ++cycle
                studyTextView.text = "${studyTime}:00"
                isStudyTime = false
                remainingBreakTime = (breakTime * 60 * 1000).toLong()
                executionViberate()
                startBreakTimer()
            }
        }.start()
    }

    private fun startBreakTimer() {
        breakTimer = object : CountDownTimer(remainingBreakTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingBreakTime = millisUntilFinished
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                breakTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                breakTextView.text = "${breakTime}:00"
                isStudyTime = true
                remainingStudyTime = (studyTime * 60 * 1000).toLong()
                executionViberate()
                startStudyTimer()
            }
        }.start()
    }

    fun pauseTimer() {
        studyTimer?.cancel()
        breakTimer?.cancel()
    }

    fun resumeTimer() {
        startTimer()
    }

    fun resetTimer() {
        totalStudyTime = cycle * studyTime + (studyTime - minutes.toInt())
        Log.i(
            "checkValues", "cycle : ${cycle}, \n " +
                    "studyTime : ${studyTime}, minutes : ${minutes}, totalStudyTime : ${totalStudyTime}"
        )
        studyTimer?.cancel()
        breakTimer?.cancel()
        remainingStudyTime = 0L
        remainingBreakTime = 0L
        isStudyTime = false
        breakTextView.text = "${breakTime}:00"
        studyTextView.text = "${studyTime}:00"
        cycle = 0

    }

    fun customModeTime() {
        studyTime = 0
        breakTime = 0
    }


    fun executionViberate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(500)
        }
    }
}


