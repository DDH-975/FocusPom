package com.project.pomodoro

import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView

class SetPomodoroTimer(
    private var studyTime: Int,
    private var breakTime: Int,
    private var studyTextView: TextView,
    private var breakTextView: TextView
) {
    private var studyTimer: CountDownTimer? = null
    private var breakTimer: CountDownTimer? = null
    private var remainingStudyTime: Long = (studyTime * 60 * 1000).toLong()
    private var remainingBreakTime: Long = (breakTime * 60 * 1000).toLong()
    private var isStudyTime = true
    private var countStudyCycle: Int = 0;

    fun startTimer() {
        if (isStudyTime) {
            startStudyTimer()
        } else {
            startBreakTimer()
        }
    }

    private fun startStudyTimer() {
        Log.d("startStudyTimer","startStudyTimer")
        ++countStudyCycle

        studyTimer = object : CountDownTimer(remainingStudyTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingStudyTime = millisUntilFinished
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                studyTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                studyTextView.text = "${studyTime}:00"
                isStudyTime = false
                remainingBreakTime = (breakTime * 60 * 1000).toLong()
                startBreakTimer()
            }
        }.start()
    }

    private fun startBreakTimer() {
        Log.d("startBreakTimer()","startBreakTimer()")
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
                startStudyTimer()
            }
        }.start()
    }

    fun pauseTimer() {
        Log.d("pauseTimer()","pauseTimer()")
        studyTimer?.cancel()
        breakTimer?.cancel()
    }

    fun resumeTimer() {
        Log.d("resumeTimer()","resumeTimer()")
        Log.i("isStudyTime value : ","${isStudyTime}")
        startTimer()
    }

    fun resetTimer(){
        Log.d("resetTimer()","resetTimer()")
        Log.i("isStudyTime value : ","${isStudyTime}")
        studyTimer?.cancel()
        breakTimer?.cancel()
        remainingStudyTime = 0L
        remainingBreakTime = 0L
        isStudyTime = false
        breakTextView.text = "${breakTime}:00"
        studyTextView.text = "${studyTime}:00"
        Log.d("resetTimer()","resetTimer()")
        Log.i("isStudyTime value : ","${isStudyTime}")

    }

    fun changeStudyTimeAndBreakTimeForCustomMode(){
        studyTime = 0
        breakTime = 0
    }
}


