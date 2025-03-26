package com.project.pomodoro.ui.basicMode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.SetPomodoroTimer
import com.project.pomodoro.databinding.FragmentBasicmodeBinding


class BasicModeFragment : Fragment() {

    private var _binding: FragmentBasicmodeBinding? = null
    private lateinit var setTimer: SetPomodoroTimer
    private var isFirstClick: Boolean = true

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val basicModeViewModel =
            ViewModelProvider(this).get(BasicModeViewModel::class.java)
        _binding = FragmentBasicmodeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setTimer = SetPomodoroTimer(
            25, 5, binding.tvStudyText,
            binding.tvBreakText
        )

        binding.btnStart.setOnClickListener {
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
                setTimer.resetTimer()
                binding.btnStart.isEnabled = true
                binding.btnPause.isEnabled = false
                binding.btnContinue.isEnabled = false
                binding.btnStop.isEnabled = false

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