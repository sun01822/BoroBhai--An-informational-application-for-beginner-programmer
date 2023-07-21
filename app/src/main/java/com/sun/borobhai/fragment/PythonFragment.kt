package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.borobhai.databinding.FragmentPythonBinding

class PythonFragment : Fragment() {
    private lateinit var binding : FragmentPythonBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPythonBinding.inflate(layoutInflater)
        val value = arguments?.getString("value_key")
        binding.textView.text = value
        return binding.root
    }
}