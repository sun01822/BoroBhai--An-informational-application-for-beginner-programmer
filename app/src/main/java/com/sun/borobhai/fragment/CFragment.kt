package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sun.borobhai.R
import com.sun.borobhai.databinding.FragmentCBinding

class CFragment : Fragment() {
    private lateinit var binding : FragmentCBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCBinding.inflate(layoutInflater)
        val value = arguments?.getString("value_key")
        binding.textView.text = value
        return binding.root
    }
    companion object {
        fun newInstance(value: String): CFragment {
            val fragment = CFragment()
            val args = Bundle()
            args.putString("value_key", value)
            fragment.arguments = args
            return fragment
        }
    }

}