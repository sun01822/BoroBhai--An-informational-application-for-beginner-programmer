package com.sun.borobhai.helper

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.borobhai.R
import com.sun.borobhai.adapter.HorizontalListAdapter
import com.sun.borobhai.data.Language
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

object FragmentHelper {
    fun getFragmentWithValue(fragment: Fragment, value: String): Fragment {
        val args = Bundle()
        args.putString("value_key", value)
        fragment.arguments = args
        return fragment
    }
    fun loadJSONFromAsset(context: Context, fileName: String): JSONObject? {
        val json: JSONObject?
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val jsonString = String(buffer, Charsets.UTF_8)
            json = JSONObject(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
        return json
    }

    fun setupRecyclerView(context : Context, recyclerView: RecyclerView, dataList: List<String>, links: List<String>, image: List<Int>){
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = HorizontalListAdapter(context, dataList, links, image)
    }

    fun getListFromJSONArray(jsonArray: JSONArray): List<String> {
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }

    fun parseLanguageDataFromJSON(jsonString: String?, value: String): Language? {
        jsonString?.let {
            try {
                val jsonArray = JSONObject(jsonString).getJSONArray("languages")
                for (i in 0 until jsonArray.length()) {
                    val languageObject = jsonArray.getJSONObject(i)
                    val name = languageObject.getString("name")
                    if (name == value) {
                        val definition = languageObject.getString("definition")
                        val whyLearn = languageObject.getString("why_learn")
                        val bestBooks = getListFromJSONArray(languageObject.getJSONArray("best_books"))
                        val bestEditors = getListFromJSONArray(languageObject.getJSONArray("best_editors"))
                        val bestYouTubeChannels = getListFromJSONArray(languageObject.getJSONArray("best_youtube_channels"))
                        val onlineCompilers = getListFromJSONArray(languageObject.getJSONArray("online_compilers"))
                        val booksDownloadLinks = getListFromJSONArray(languageObject.getJSONArray("books_download_links"))
                        val editorsDownloadLinks = getListFromJSONArray(languageObject.getJSONArray("editors_download_links"))
                        val youtubeChannelsLinks = getListFromJSONArray(languageObject.getJSONArray("youtube_channels_links"))

                        return Language(
                            name,
                            definition,
                            whyLearn,
                            bestBooks,
                            bestEditors,
                            bestYouTubeChannels,
                            onlineCompilers,
                            booksDownloadLinks,
                            editorsDownloadLinks,
                            youtubeChannelsLinks
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }
}
