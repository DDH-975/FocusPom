package com.project.pomodoro.ui.myCustom

import android.os.Bundle
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


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myCoustomViewModel =
            ViewModelProvider(this).get(MyCustomPragmentViewModel::class.java)

        _binding = FragmentMyCustomBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.btnInput.setOnClickListener {
            if (binding.etStudyTime.text == null) {
                Toast.makeText(context, "공부시간을 입력해주세요!", Toast.LENGTH_SHORT).show()

            } else if (binding.etBeakTime.text == null) {
                Toast.makeText(context, "휴식시간을 입력해주세요!", Toast.LENGTH_SHORT).show()

            } else if (binding.etStudyTime.text == null && binding.etBeakTime.text == null) {
                Toast.makeText(context, "공부시간과 휴식시간을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                inputStudyTime = binding.etStudyTime.text.toString().toInt()
                inputBeakTime = binding.etBeakTime.text.toString().toInt()

                binding.tvStudyText.text = "${inputStudyTime}:00"
                binding.tvBreakText.text = "${inputBeakTime}:00"

                binding.btnInput.text = "수정하기"
                binding.btnStart.isEnabled = true

                setTimer = SetPomodoroTimer(
                    inputStudyTime, inputBeakTime, binding.tvStudyText,
                    binding.tvBreakText
                )
            }
        }




        binding.btnStart.setOnClickListener {
            binding.btnInput.isEnabled = false
            binding.btnStart.isEnabled = false
            binding.btnStop.isEnabled = true
            binding.btnPause.isEnabled = true

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
                setTimer.changeStudyTimeAndBreakTimeForCustomMode()
                setTimer.resetTimer()

                binding.btnInput.apply {
                    isEnabled = true
                    text = "입력 완료"
                }

                binding.btnStart.isEnabled = false
                binding.btnPause.isEnabled = false
                binding.btnContinue.isEnabled = false
                binding.btnStop.isEnabled = false
                binding.etStudyTime.text = null
                binding.etBeakTime.text = null

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