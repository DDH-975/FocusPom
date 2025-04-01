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
import androidx.lifecycle.lifecycleScope
import com.project.pomodoro.SetPomodoroTimer
import com.project.pomodoro.databinding.FragmentMyCustomBinding
import com.project.pomodoro.roomDB.DataBase
import com.project.pomodoro.roomDB.StudySessionDao
import com.project.pomodoro.roomDB.StudySummaryDao
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyCustomPragment : Fragment() {
    private var _binding: FragmentMyCustomBinding? = null
    private lateinit var setTimer: SetPomodoroTimer
    private var inputStudyTime: Int = 0
    private var inputBeakTime: Int = 0
    private var isFirstClick: Boolean = true

    private lateinit var studySessionDao: StudySessionDao
    private lateinit var studySummaryDao: StudySummaryDao
    private lateinit var db: DataBase
    private val date = LocalDate.now()

    private var totalStudyTime: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DataBase.getDatabase(context)
    }

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

        //공부시간, 휴식 시간 입력완료 버튼 클릭 리스너
        binding.btnInput.setOnClickListener {

            //공부시간과 휴식시간을 제대로 입력을 했는지 확인
            //모두 true면 SetPomodoroTimer 인스턴스 생성
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


        //공부 시작 버튼 클릭 리스너
        binding.btnStart.setOnClickListener {
            listOf(binding.btnInput, binding.btnStart).forEach { it.isEnabled = false }
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
                setTimer.customModeTime()
                binding.btnInput.apply {
                    isEnabled = true
                    text = "입력 완료"
                }

                listOf(
                    binding.btnStart, binding.btnPause,
                    binding.btnContinue, binding.btnStop
                ).forEach { it.isEnabled = false }

                listOf(binding.etStudyTime, binding.etBeakTime).forEach { it.text = null }
                isFirstClick = true

                var session = com.project.pomodoro.roomDB.StudySession(
                    modeType = "내 맞춤 모드",
                    studyTime = totalStudyTime,
                    studyDate = date.toString()
                )

                lifecycleScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                    studySessionDao = db.studySessionDao()
                    studySummaryDao = db.studySummaryDao()

                    studySessionDao.insertSession(session)
                    studySummaryDao.addStudyTime(time = totalStudyTime, modeType = "내 맞춤 모드")

                    val testvalue = studySessionDao.getAllData()
                    val testvalue2 = studySummaryDao.getAllData()
                    android.util.Log.d("Database", "Stored session: $testvalue")
                    android.util.Log.d("Database", "Stored Summary: $testvalue2")

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