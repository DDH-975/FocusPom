package com.project.pomodoro.ui.basicMode

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


        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator

        } else {
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }



        setTimer = SetPomodoroTimer(
            25, 5, binding.tvStudyText,
            binding.tvBreakText, vibrator
        )

        binding.btnStart.setOnClickListener {
            binding.btnStart.isEnabled = false
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

                listOf(binding.btnPause, binding.btnContinue, binding.btnStop).forEach { it.isEnabled = false }
                binding.btnStart.isEnabled = true

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