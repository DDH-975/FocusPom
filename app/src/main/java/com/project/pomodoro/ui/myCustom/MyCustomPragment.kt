package com.project.pomodoro.ui.myCustom

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.SetPomodoroTimer
import com.project.pomodoro.databinding.FragmentMyCustomBinding

class MyCustomPragment : Fragment() {
    private var _binding: FragmentMyCustomBinding? = null
    private lateinit var setTimer: SetPomodoroTimer
    private var inputStudyTime: Int = 0
    private var inputBeakTime: Int = 0
    private var isFirstClick: Boolean = true
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myCoustomViewModel =
            ViewModelProvider(this).get(MyCustomPragmentViewModel::class.java)

        _binding = FragmentMyCustomBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator

        } else {
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }


        binding.btnInput.setOnClickListener {

            if (binding.etStudyTime.text.toString().trim().isNotEmpty() &&
                binding.etBeakTime.text.toString().trim().isNotEmpty()
            ) {
                inputStudyTime = binding.etStudyTime.text.toString().toInt()
                inputBeakTime = binding.etBeakTime.text.toString().toInt()

                binding.tvStudyText.text = "${inputStudyTime}:00"
                binding.tvBreakText.text = "${inputBeakTime}:00"

                binding.btnInput.text = "수정하기"
                binding.btnStart.isEnabled = true

                setTimer = SetPomodoroTimer(
                    inputStudyTime, inputBeakTime, binding.tvStudyText,
                    binding.tvBreakText, vibrator
                )

            } else {
                Toast.makeText(context, "시간을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnStart.setOnClickListener {
            listOf(binding.btnInput,binding.btnStart).forEach { it.isEnabled = false }
            listOf(binding.btnStop, binding.btnPause).forEach { it.isEnabled = true }

            setTimer.startTimer()
        }

        binding.btnPause.setOnClickListener {
            binding.btnPause.isEnabled = false
            binding.btnContinue.isEnabled = true

            setTimer.pauseTimer()
        }

        binding.btnContinue.setOnClickListener {
            binding.btnPause.isEnabled = true
            binding.btnContinue.isEnabled = false

            setTimer.resumeTimer()
        }

        binding.btnStop.setOnClickListener {
            if (isFirstClick) {
                Toast.makeText(context, "공부를 그만 하시려면 한번 더 눌러주세요", Toast.LENGTH_SHORT).show()
                isFirstClick = false

            } else {
                Toast.makeText(context, "수고하셨습니다. 내일도 뵈요!", Toast.LENGTH_SHORT).show()

                setTimer.resetTimer()
                setTimer.customModeTime()
                binding.btnInput.apply {
                    isEnabled = true
                    text = "입력 완료"
                }

                listOf(binding.btnStart, binding.btnPause,
                    binding.btnContinue, binding.btnStop).forEach { it.isEnabled = false }

                listOf(binding.etStudyTime, binding.etBeakTime).forEach { it.text = null }
                isFirstClick = true

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}