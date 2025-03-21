package com.project.pomodoro.ui.ultraFocus

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.R
import com.project.pomodoro.databinding.FragmentShortfocusBinding
import com.project.pomodoro.databinding.FragmentUltraFocusBinding
import com.project.pomodoro.ui.shortFocus.ShortFocusViewModel

class UltraFocusFragment : Fragment() {
    private var _binding: FragmentUltraFocusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ultraFocusViewModel =
            ViewModelProvider(this).get(UltraFocusViewModel::class.java)

        _binding = FragmentUltraFocusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}