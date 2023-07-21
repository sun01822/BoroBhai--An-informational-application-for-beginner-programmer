package com.sun.borobhai.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.borobhai.R
import com.sun.borobhai.databinding.FragmentCPPBinding
import com.sun.borobhai.helper.FragmentHelper
import org.json.JSONException

class CPPFragment : Fragment() {
    private lateinit var binding : FragmentCPPBinding
    private lateinit var name : String
    private lateinit var definition : String
    private lateinit var whyLearn : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCPPBinding.inflate(layoutInflater)
        val value = arguments?.getString("value_key")
        val jsonString = FragmentHelper.loadJSONFromAsset(requireContext(), "data.json")

        jsonString?.let {
            try {
                val jsonArray = it.getJSONArray("languages")
                for (i in 0 until jsonArray.length()) {
                    val languageObject = jsonArray.getJSONObject(i)
                    name = languageObject.getString("name")
                    println("Name: $name\n\n")
                    if (name == value) {
                        definition = languageObject.getString("definition")
                        whyLearn = languageObject.getString("why_learn")
                        /*Toast.makeText(
                            requireContext(),
                            "Data fetched from JSON file",
                            Toast.LENGTH_SHORT
                        ).show()*/
                        break
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        binding.textView.text = definition
        return binding.root
    }
}