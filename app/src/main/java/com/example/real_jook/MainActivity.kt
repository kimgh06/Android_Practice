package com.example.real_jook

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val apiKey = "9aa3bd11f23949c78169"
    private val apiUrl =
        "http://openapi.foodsafetykorea.go.kr/api/$apiKey/I2790/json/1/1000/DESC_KOR=닭죽"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val response = makeApiRequest()
//        updateUI(response)
        Log.d("url_res", "$response")
    }

    private fun makeApiRequest(): String {
        val response = StringBuilder()
        try {
            val url = URL(apiUrl)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                println("\nSend GET request URL : $url; Response Code : $responseCode")
                val inputStream = inputStream
                response.append(readResponse(inputStream))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response.toString()
    }

    private fun readResponse(inputStream: java.io.InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line).append('\n')
        }
        return response.toString()
    }

    private fun updateUI(response: String) {
        runOnUiThread {
            val linearLayout = LinearLayout(this)
////            setContentView(linearLayout)
//            linearLayout.orientation = LinearLayout.VERTICAL

            val textView = TextView(this)
            textView.text = response // Display the API response in the TextView
            linearLayout.addView(textView)
        }
    }
}
