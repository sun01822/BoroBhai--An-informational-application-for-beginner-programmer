package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.borobhai.databinding.FragmentPhpBinding
import com.sun.borobhai.helper.FragmentHelper.fetchDataFromJsonFile

class PhpFragment : Fragment() {
    private lateinit var binding : FragmentPhpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhpBinding.inflate(layoutInflater)

        val value = arguments?.getString("value_key")
        fetchDataFromJsonFile(
            requireContext(), value.toString(), binding.tvLanguageName,
            binding.tvLanguageDefinition, binding.tvWhyLearn, binding.rvBestBooks,
            binding.rvBestEditors, binding.rvBestYouTubeChannels, binding.rvOnlineCompilers
        )
        return binding.root
    }
}