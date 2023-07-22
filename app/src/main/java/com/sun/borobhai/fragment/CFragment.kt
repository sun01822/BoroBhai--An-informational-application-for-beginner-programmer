package com.sun.borobhai.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.borobhai.R
import com.sun.borobhai.databinding.FragmentCBinding
import com.sun.borobhai.helper.FragmentHelper.loadJSONFromAsset
import com.sun.borobhai.helper.FragmentHelper.parseLanguageDataFromJSON
import com.sun.borobhai.helper.FragmentHelper.setupRecyclerView

class CFragment : Fragment() {
    private lateinit var binding: FragmentCBinding
    private lateinit var booksImage : List<Int>
    private lateinit var compilerImage : List<Int>
    private lateinit var editorImage : List<Int>
    private lateinit var youtubeImage : List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCBinding.inflate(layoutInflater)
        booksImage = listOf(R.drawable.book)
        compilerImage = listOf(R.drawable.compiler)
        editorImage = listOf(R.drawable.coding)
        youtubeImage = listOf(R.drawable.youtuber)

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
                booksImage
            )
            setupRecyclerView(
                requireContext(),
                binding.rvBestEditors,
                it.bestEditors,
                it.editorsDownloadLinks,
                editorImage
            )
            setupRecyclerView(
                requireContext(),
                binding.rvBestYouTubeChannels,
                it.bestYouTubeChannels,
                it.youtubeChannelsLinks,
                youtubeImage
            )
           setupRecyclerView(
                requireContext(),
                binding.rvOnlineCompilers,
                it.onlineCompilers,
                emptyList(),
                compilerImage
            )
        }
        return binding.root
    }
}