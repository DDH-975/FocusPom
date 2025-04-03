package com.project.pomodoro.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.project.pomodoro.R
import com.project.pomodoro.databinding.FragmentHomeBinding
import com.project.pomodoro.roomDB.DataBase
import com.project.pomodoro.roomDB.StudySessionDao
import com.project.pomodoro.roomDB.StudySummaryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min


class HomeFragment : Fragment() {
    private lateinit var db: DataBase
    private lateinit var studySessionDao: StudySessionDao
    private lateinit var studySummaryDao: StudySummaryDao
    private var isFirstClick: Boolean = true
    private var totalTime: Int? = null


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = DataBase.getDatabase(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setTotalTextView()
        setPieChart()
        setBarChart()
        setLineChart()


        //통계 초기화
        binding.btnDeleteData.setOnClickListener {
            if (isFirstClick) {
                Toast.makeText(
                    context, "초기화하면 이전 상태로 복구할 수 없습니다. 진행하려면 한 번 더 클릭해주세요.",
                    Toast.LENGTH_LONG
                ).show()

                isFirstClick = false

            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    studySessionDao = db.studySessionDao()
                    studySummaryDao = db.studySummaryDao()

                    studySessionDao.deleteAllDataBySession()
                    studySummaryDao.deleteAllDataBySummary()

                    isFirstClick = true

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "초기화 되었습니다", Toast.LENGTH_SHORT).show()

                        //프래그먼트 리로드
                        val fragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.beginTransaction().apply {
                            replace(R.id.nav_host_fragment_content_main, HomeFragment())
                            addToBackStack(null)
                            commit()
                        }
                    }
                }

            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // LineChart 세팅
    private fun setLineChart() {
        lifecycleScope.launch(Dispatchers.IO) {
            studySessionDao = db.studySessionDao()
            val totalStudyTimeByDate = studySessionDao.getDataGroupByDate()

            Log.d("getDataGroupByDate()", "${totalStudyTimeByDate}")

            withContext(Dispatchers.Main) {
                if (totalStudyTimeByDate != null) {
                    var entries = mutableListOf<Entry>()
                    var date = mutableListOf<String>()
                    var index: Float

                    for (i in 0..totalStudyTimeByDate!!.size - 1) {
                        index = i.toFloat()
                        entries.add(
                            Entry(
                                index,
                                totalStudyTimeByDate.get(i).totalStudyTime.toFloat()
                            )
                        )
                        date.add(totalStudyTimeByDate.get(i).studyDate)
                    }

                    val dataSet = LineDataSet(entries, "공부 시간(분)").apply {
                        color = Color.BLUE
                        valueTextColor = Color.BLACK
                        lineWidth = 3f
                        circleRadius = 6f
                        valueTextSize = 12f
                        setDrawCircleHole(false)
                        setCircleColor(Color.RED)

                        // 그래디언트 효과 추가
                        setDrawFilled(true)
                        fillDrawable =
                            ContextCompat.getDrawable(requireContext(), R.drawable.chart_gradient)

                        // 강조 효과 (값이 클수록 진하게)
                        setDrawValues(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getPointLabel(entry: Entry?): String {
                                return "${entry?.y?.toInt()}분"
                            }
                        }
                    }

                    val lineData = LineData(dataSet)
                    binding.lineChart.data = lineData

                    // X축 설정 (날짜 표시)
                    binding.lineChart.xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        setLabelCount(min(7, date.size), true)
                        textColor = Color.BLACK
                        granularity = 1f
                        setAvoidFirstLastClipping(true)
                        valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return if (value >= 0 && value < date.size) date[value.toInt()] else ""
                            }
                        }
                    }

                    // Y축 설정
                    binding.lineChart.axisLeft.apply {
                        setDrawGridLines(true)
                        setDrawLabels(false)  // Y축 라벨 활성화
                        textColor = Color.BLACK
                    }
                    binding.lineChart.axisRight.isEnabled = false

                    // 차트 스타일 설정
                    binding.lineChart.apply {
                        description.isEnabled = false
                        legend.isEnabled = true
                        setTouchEnabled(true) // 터치 활성화
                        setPinchZoom(true) // 줌 가능
                        extraLeftOffset = 15f
                        extraRightOffset = 15f

                        setExtraOffsets(20f, 10f, 20f, 10f)

                        animateX(1000)
                        animateY(1000)
                    }

                    // 업데이트 반영
                    binding.lineChart.invalidate()
                } else {
                    binding.tvNoData1.isInvisible = false
                    binding.lineChart.isInvisible = true
                }
            }
        }
    }


    //PieChart 세팅
    private fun setPieChart() {
        lifecycleScope.launch(Dispatchers.IO) {
            studySummaryDao = db.studySummaryDao()
            val summaryData = studySummaryDao.getAllData()
            Log.d("asdfasdsafd", "${summaryData}")

            withContext(Dispatchers.Main) {
                if (summaryData != null) {

                    // data set
                    val entries = ArrayList<PieEntry>()
                    entries.add(PieEntry(summaryData.coustomMode.toFloat(), "커스텀"))
                    entries.add(PieEntry(summaryData.longFocus.toFloat(), "롱 포커스"))
                    entries.add(PieEntry(summaryData.basicMode.toFloat(), "기본"))
                    entries.add(PieEntry(summaryData.shortFocus.toFloat(), "짧은 집중"))
                    entries.add(PieEntry(summaryData.ultraFocus.toFloat(), "울트라"))


                    // add a lot of colors
                    val colorsItems = ArrayList<Int>()
                    for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
                    for (c in COLORFUL_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
                    for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
                    colorsItems.add(ColorTemplate.getHoloBlue())

                    //퍼센테이지 활성화
                    binding.pieChart.setUsePercentValues(true)

                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.apply {
                        colors = colorsItems
                        valueTextColor = Color.BLACK
                        valueTextSize = 18f
                        valueFormatter = PercentFormatter(binding.pieChart)
                    }

                    val pieData = PieData(pieDataSet)
                    binding.pieChart.apply {
                        data = pieData
                        description.isEnabled = false
                        isRotationEnabled = false
                        centerText = "모드별 비율"
                        setEntryLabelColor(Color.BLACK)
                        setCenterTextSize(20f)
                        animateY(1400, Easing.EaseInOutQuad)
                        animate()
                    }
                } else {
                    binding.tvNoData2.isInvisible = false
                    binding.pieChart.isInvisible = true
                }
            }
        }
    }


    //수직 바 차트 설정
    private fun setBarChart() {
        lifecycleScope.launch(Dispatchers.IO) {
            studySummaryDao = db.studySummaryDao()
            val summaryData = studySummaryDao.getAllData()

            withContext(Dispatchers.Main) {
                if (summaryData != null) {
                    val values = arrayListOf(
                        BarEntry(0f, summaryData.basicMode.toFloat()),
                        BarEntry(1f, summaryData.longFocus.toFloat()),
                        BarEntry(2f, summaryData.coustomMode.toFloat()),
                        BarEntry(3f, summaryData.shortFocus.toFloat()),
                        BarEntry(4f, summaryData.ultraFocus.toFloat())
                    )

                    val barLabels = listOf("기본", "롱 포커스", "커스텀", "짧은 집중", "울트라")

                    val barDataSet = BarDataSet(values, " ").apply {
                        setColors(ColorTemplate.JOYFUL_COLORS, 250)
                        setDrawValues(true)
                        valueTextColor = Color.BLACK
                        valueTextSize = 15f
                        valueFormatter = object : ValueFormatter() { //  바 끝에 '분' 표시
                            override fun getFormattedValue(value: Float): String {
                                return "${value.toInt()}분"
                            }
                        }
                    }

                    val barData = BarData(barDataSet).apply {
                        barWidth = 0.5f // 막대 너비 조정
                    }

                    binding.BarChart.apply {
                        data = barData
                        description.isEnabled = false // 설명 비활성화
                        setFitBars(true) // 막대를 차트에 맞춤
                        setDrawValueAboveBar(true) // 바 위쪽에 값 표시
                        legend.isEnabled = false // 범례 숨김
                        setTouchEnabled(false)
                        animateY(1400)


                        xAxis.apply {
                            valueFormatter = object : ValueFormatter() {
                                override fun getFormattedValue(value: Float): String {
                                    return barLabels.getOrNull(value.toInt()) ?: value.toString()
                                }
                            }
                            position = XAxis.XAxisPosition.BOTTOM //  X축을 차트 아래쪽 배치
                            textSize = 14f
                            textColor = Color.BLACK
                            granularity = 1f
                            setDrawGridLines(false)
                        }


                        extraBottomOffset = 10f
                        axisLeft.isEnabled = false
                        axisRight.isEnabled = false

                        invalidate()
                    }
                } else {
                    binding.tvNoData3.isInvisible = false
                    binding.BarChart.isInvisible = true
                }
            }
        }
    }


    //총 공부 시간 출력
    private fun setTotalTextView() {
        lifecycleScope.launch(Dispatchers.IO) {
            studySummaryDao = db.studySummaryDao()

            totalTime = studySummaryDao.getTotalTime()

            withContext(Dispatchers.Main) {

                withContext(Dispatchers.Main) {
                    totalTime?.let { time ->
                        val hours = time / 60
                        val minutes = time % 60
                        binding.tvTotalStudyTime.text = "총 공부 시간 : ${hours}시간 ${minutes}분"
                    } ?: run {
                        binding.tvTotalStudyTime.text = "데이터가 없습니다."
                    }
                }
            }
        }
    }

}

