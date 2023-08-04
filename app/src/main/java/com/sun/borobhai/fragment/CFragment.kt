package com.sun.borobhai.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.borobhai.databinding.FragmentLanguagesBinding
import com.sun.borobhai.helper.FragmentHelper.fetchDataFromJsonFile

class CFragment : Fragment() {
    private lateinit var binding: FragmentLanguagesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguagesBinding.inflate(layoutInflater)
        val value = arguments?.getString("value_key")

        fetchDataFromJsonFile(requireContext(), value.toString(), binding.tvLanguageName,
            binding.tvLanguageDefinition, binding.tvWhyLearn, binding.rvBestBooks,
            binding.rvBestEditors, binding.rvBestYouTubeChannels, binding.rvOnlineCompilers)

        return binding.root
    }
}