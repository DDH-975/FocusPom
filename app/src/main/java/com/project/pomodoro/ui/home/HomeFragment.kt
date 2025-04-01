package com.project.pomodoro.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.project.pomodoro.databinding.FragmentHomeBinding
import com.project.pomodoro.roomDB.DataBase
import com.project.pomodoro.roomDB.StudySessionDao
import com.project.pomodoro.roomDB.StudySummaryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var db: DataBase
    private lateinit var studySessionDao: StudySessionDao
    private lateinit var studySummaryDao: StudySummaryDao

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DataBase.getDatabase(context) // onAttach에서 초기화
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.btnDeleteData.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                studySessionDao = db.studySessionDao()
                studySummaryDao = db.studySummaryDao()

                studySessionDao.deleteAllDataBySession()
                studySummaryDao.deleteAllDataBySummary()

                var value1 = studySessionDao.getAllData()
                var value2 = studySummaryDao.getAllData()

                Log.i("checkedDeleteData", "studySession : ${value1}\n studySummary : ${value2}")

            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}