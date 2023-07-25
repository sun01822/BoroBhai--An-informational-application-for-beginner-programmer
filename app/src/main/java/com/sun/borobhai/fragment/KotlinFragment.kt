package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.borobhai.R
import com.sun.borobhai.databinding.FragmentKotlinBinding
import com.sun.borobhai.helper.FragmentHelper
import com.sun.borobhai.helper.FragmentHelper.loadJSONFromAsset
import com.sun.borobhai.helper.FragmentHelper.parseLanguageDataFromJSON
import com.sun.borobhai.helper.FragmentHelper.setupRecyclerView

class KotlinFragment : Fragment() {
    private lateinit var binding : FragmentKotlinBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKotlinBinding.inflate(layoutInflater)

        val value = arguments?.getString("value_key")

        val jsonString = loadJSONFromAsset(requireContext(), "data.json")
        val languageData = parseLanguageDataFromJSON(jsonString?.toString(), value!!)

        languageData?.let {
            binding.tvLanguageName.text = it.name
            binding.tvLanguageDefinition.text = it.definition
            binding.tvWhyLearn.text = it.whyLearn

            setupRecyclerView(
                requireContext(),
                binding.rvBestBooks,
                it.bestBooks,
                it.booksDownloadLinks,
                0,
                0
            )
            setupRecyclerView(
                requireContext(),
                binding.rvBestEditors,
                it.bestEditors,
                it.editorsDownloadLinks,
                1,
                0
            )
            setupRecyclerView(
                requireContext(),
                binding.rvBestYouTubeChannels,
                it.bestYouTubeChannels,
                it.youtubeChannelsLinks,
                2,
                1
            )
            setupRecyclerView(
                requireContext(),
                binding.rvOnlineCompilers,
                it.onlineCompilers,
                it.onlineCompilersLink,
                3,
                0
            )
        }
        return binding.root
    }
}