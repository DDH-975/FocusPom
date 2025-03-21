package com.project.pomodoro.ui.longFocus

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.R
import com.project.pomodoro.databinding.FragmentBasicmodeBinding
import com.project.pomodoro.databinding.FragmentLongFocusBinding
import com.project.pomodoro.ui.basicMode.BasicModeViewModel

class LongFocusFragment : Fragment() {
    private var _binding: FragmentLongFocusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val longFocusViewModel = ViewModelProvider(this).get(LongFocusViewModel::class.java)
        _binding = FragmentLongFocusBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}