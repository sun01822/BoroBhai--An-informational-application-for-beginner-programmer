package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.borobhai.R
import com.sun.borobhai.databinding.FragmentCBinding
import com.sun.borobhai.databinding.FragmentKotlinBinding

class KotlinFragment : Fragment() {
    private lateinit var binding : FragmentKotlinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKotlinBinding.inflate(layoutInflater)
        val value = arguments?.getString("value_key")
        binding.textView.text = value
        return binding.root
    }
}