package com.sun.borobhai.helper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
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
}
