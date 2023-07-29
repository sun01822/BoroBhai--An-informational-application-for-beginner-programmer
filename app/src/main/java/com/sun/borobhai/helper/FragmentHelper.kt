package com.sun.borobhai.helper

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    fun setupRecyclerView(context: Context, recyclerView: RecyclerView, dataList: List<String>, links: List<String>, checker: Int, youtubeLink: String){
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = HorizontalListAdapter(context, dataList, links, checker, youtubeLink)
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
                        val workingField = languageObject.getString("working_field")
                        val bestBooks = getListFromJSONArray(languageObject.getJSONArray("best_books"))
                        val bestEditors = getListFromJSONArray(languageObject.getJSONArray("best_editors"))
                        val bestYouTubeChannels = getListFromJSONArray(languageObject.getJSONArray("best_youtube_channels"))
                        val onlineCompilers = getListFromJSONArray(languageObject.getJSONArray("online_compilers"))
                        val booksDownloadLinks = getListFromJSONArray(languageObject.getJSONArray("books_download_links"))
                        val editorsDownloadLinks = getListFromJSONArray(languageObject.getJSONArray("editors_download_links"))
                        val onlineCompilersLink = getListFromJSONArray(languageObject.getJSONArray("online_compilers_link"))
                        val youtubeChannelsLinks = getListFromJSONArray(languageObject.getJSONArray("youtube_channels_links"))

                        return Language(
                            name,
                            definition,
                            whyLearn,
                            workingField,
                            bestBooks,
                            bestEditors,
                            bestYouTubeChannels,
                            onlineCompilers,
                            booksDownloadLinks,
                            editorsDownloadLinks,
                            onlineCompilersLink,
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

    fun fetchDataFromJsonFile(context: Context, value: String, tvLanguageName: TextView, tvLanguageDefinition: TextView, tvWhyLearn: TextView
    , rvBestBooks : RecyclerView, rvBestEditors : RecyclerView, rvBestYouTubeChannels : RecyclerView, rvOnlineCompilers : RecyclerView){
        val jsonString = loadJSONFromAsset(context, "data.json")
        val languageData = parseLanguageDataFromJSON(jsonString?.toString(), value)

        languageData?.let {
            tvLanguageName.text = it.name
            tvLanguageDefinition.text = it.definition
            tvWhyLearn.text = it.whyLearn

            setupRecyclerView(
                context,
                rvBestBooks,
                it.bestBooks,
                it.booksDownloadLinks,
                0,
                "n"
            )
            setupRecyclerView(
                context,
                rvBestEditors,
                it.bestEditors,
                it.editorsDownloadLinks,
                1,
                "n"
            )
            setupRecyclerView(
                context,
                rvBestYouTubeChannels,
                it.bestYouTubeChannels,
                it.youtubeChannelsLinks,
                2,
                "y"
            )
            setupRecyclerView(
                context,
                rvOnlineCompilers,
                it.onlineCompilers,
                it.onlineCompilersLink,
                3,
                "n"
            )
        }
    }
}
