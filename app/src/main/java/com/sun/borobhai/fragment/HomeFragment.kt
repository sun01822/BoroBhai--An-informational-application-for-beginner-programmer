package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.borobhai.R
import com.sun.borobhai.adapter.GridAdapter
import com.sun.borobhai.data.GridItem
import com.sun.borobhai.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var gridAdapter: GridAdapter
    private lateinit var binding : FragmentHomeBinding

    private val gridItems =listOf(
        GridItem("C programming", "https://res.cloudinary.com/practicaldev/image/fetch/s--jfMWJNMp--/c_imagga_scale,f_auto,fl_progressive,h_900,q_auto,w_1600/https://dev-to-uploads.s3.amazonaws.com/uploads/articles/b22wdroslo9khnoyssmn.png"),
        GridItem("C++", "https://www.nicepng.com/png/detail/111-1116276_computer-science-i-syllabus-and-grading-policy-c.png"),
        GridItem("Java", "https://static.vecteezy.com/system/resources/previews/020/111/555/non_2x/java-editorial-logo-free-download-free-vector.jpg"),
        GridItem("Python", "https://www.pngitem.com/pimgs/m/159-1595932_python-logo-png-transparent-images-logo-transparent-background.png"),
        GridItem("JavaScript", "https://thumbs.dreamstime.com/b/javascript-logo-editorial-illustrative-white-background-eps-download-vector-jpeg-banner-javascript-logo-editorial-illustrative-208329459.jpg"),
        GridItem("Kotlin", "https://download.logo.wine/logo/Kotlin_(programming_language)/Kotlin_(programming_language)-Logo.wine.png"),
        GridItem("PHP", "data:image/png;base64,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"),
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        gridRecyclerView = binding.gridRecyclerView
        gridRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        gridAdapter = GridAdapter(requireContext(), gridItems)
        gridRecyclerView.adapter = gridAdapter
        gridAdapter.setOnclickListener(object : GridAdapter.OnClickListener{
            override fun onClick(position: Int, gridItem: GridItem) {
                println("Position: ${position}")
                println("GridItem: ${gridItem.name} ${gridItem.imageUrl}")
                val fragment = when (position) {
                    0 -> CFragment()
                    1 -> CPPFragment()
                    2 -> JavaFragment()
                    3 -> PythonFragment()
                    4 -> JavaScriptFragment()
                    5 -> KotlinFragment()
                    6 -> PhpFragment()
                    else -> {
                        null
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment!!)
                    .addToBackStack(null)
                    .commit()
            }

            override fun onClick(p0: View?) {

            }

        })
        return binding.root
    }
}