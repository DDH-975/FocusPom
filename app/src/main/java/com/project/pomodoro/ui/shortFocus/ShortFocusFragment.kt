package com.project.pomodoro.ui.shortFocus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.project.pomodoro.databinding.FragmentShortfocusBinding


class ShortFocusFragment : Fragment() {

    private var _binding: FragmentShortfocusBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shortFocusViewModel =
            ViewModelProvider(this).get(ShortFocusViewModel::class.java)

        _binding = FragmentShortfocusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}