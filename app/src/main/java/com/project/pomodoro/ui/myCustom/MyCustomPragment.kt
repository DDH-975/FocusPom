package com.project.pomodoro.ui.myCustom

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.R
import com.project.pomodoro.databinding.FragmentHomeBinding
import com.project.pomodoro.databinding.FragmentMyCustomBinding
import com.project.pomodoro.databinding.FragmentShortfocusBinding
import com.project.pomodoro.ui.home.HomeViewModel

class MyCustomPragment : Fragment() {
    private var _binding: FragmentMyCustomBinding? = null

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

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}