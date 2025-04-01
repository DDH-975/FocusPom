package com.project.pomodoro.ui.ultraFocus

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.project.pomodoro.SetPomodoroTimer
import com.project.pomodoro.databinding.FragmentUltraFocusBinding
import com.project.pomodoro.roomDB.DataBase
import com.project.pomodoro.roomDB.StudySession
import com.project.pomodoro.roomDB.StudySessionDao
import com.project.pomodoro.roomDB.StudySummaryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class UltraFocusFragment : Fragment() {
    private var _binding: FragmentUltraFocusBinding? = null
    private lateinit var setTimer: SetPomodoroTimer
    private var isFirstClick: Boolean = true

    private lateinit var studySessionDao: StudySessionDao
    private lateinit var studySummaryDao: StudySummaryDao
    private lateinit var db: DataBase
    private val date = LocalDate.now()

    private var totalStudyTime: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DataBase.getDatabase(context) // onAttach에서 초기화
    }

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ultraFocusViewModel =
            ViewModelProvider(this).get(UltraFocusViewModel::class.java)

        _binding = FragmentUltraFocusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator

        } else {
            requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        setTimer = SetPomodoroTimer(
            90, 20, binding.tvStudyText,
            binding.tvBreakText, vibrator
        )


        //공부 시작 버튼 클릭 리스너
        binding.btnStart.setOnClickListener {
            binding.btnStart.isEnabled = false
            listOf(binding.btnStop, binding.btnPause).forEach { it.isEnabled = true }

            setTimer.startTimer()
        }


        //일시정지 버튼 클릭 리스너
        binding.btnPause.setOnClickListener {
            binding.btnPause.isEnabled = false
            binding.btnContinue.isEnabled = true

            setTimer.pauseTimer()
        }


        //계속 버튼 클릭 리스너
        binding.btnContinue.setOnClickListener {
            binding.btnPause.isEnabled = true
            binding.btnContinue.isEnabled = false

            setTimer.resumeTimer()
        }


        //공부 그만하기 버튼 클릭 리스너
        binding.btnStop.setOnClickListener {

            //공부 그만하려면 버튼 두번 누르도록 유도
            if (isFirstClick) {
                Toast.makeText(context, "공부를 그만 하시려면 한번 더 눌러주세요", Toast.LENGTH_SHORT).show()
                isFirstClick = false

            } else {
                Toast.makeText(context, "수고하셨습니다. 내일도 뵈요!", Toast.LENGTH_SHORT).show()
                totalStudyTime = setTimer.resetTimer()

                listOf(
                    binding.btnPause,
                    binding.btnContinue,
                    binding.btnStop
                ).forEach { it.isEnabled = false }
                binding.btnStart.isEnabled = true

                isFirstClick = true


                var session = StudySession(
                    modeType = "울트라 포커스 모드",
                    studyTime = totalStudyTime,
                    studyDate = date.toString()
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    studySessionDao = db.studySessionDao()
                    studySummaryDao = db.studySummaryDao()

                    studySessionDao.insertSession(session)
                    studySummaryDao.addStudyTime(time = totalStudyTime, modeType = "울트라 포커스 모드")

                    val testvalue = studySessionDao.getAllData()
                    val testvalue2 = studySummaryDao.getAllData()
                    Log.d("Database", "Stored session: $testvalue")
                    Log.d("Database", "Stored Summary: $testvalue2")

                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}