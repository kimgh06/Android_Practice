package com.example.real_jook

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.HttpURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class MainActivity : AppCompatActivity() {
    private val apiKey = "9aa3bd11f23949c78169"
    private val apiUrl =
        "http://openapi.foodsafetykorea.go.kr/api/$apiKey/I2790/json/1/1000/DESC_KOR=죽"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Thread(){
            var response = makeApiRequest()
            updateUI(response)
        }.start()
    }

    private fun makeApiRequest(): String {
        val response = StringBuilder()
        try {
            val url = URL(apiUrl)
            var connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            if(connection.responseCode == HttpURLConnection.HTTP_OK){
                val streamReader = InputStreamReader(connection.inputStream)
                val buffered = BufferedReader(streamReader)
                while(true){
                    val data = buffered.readLine()?:break
                    response.append(data)
                }
                buffered.close()
                connection.disconnect()
            }
//            BufferedReader(InputStreamReader(connection.inputStream)).use {inp:BufferedReader->
//                var line: String?
//                while (inp.readLine().also { line = it }!=null){
//                    Log.d("res", "$line")
//                }
//            }
//            with(url.openConnection() as HttpURLConnection) {
//                requestMethod = "GET"
//                println("\nSend GET request URL : $url; Response Code : $responseCode")
//                val inputStream = inputStream
//                response.append(readResponse(inputStream))
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("reserror", "$e")
        }
        return response.toString()
    }

//    private fun readResponse(inputStream: java.io.InputStream): String {
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val response = StringBuilder()
//        var line: String?
//        while (reader.readLine().also { line = it } != null) {
//            response.append(line).append('\n')
//        }
//        return response.toString()
//    }

    private fun updateUI(response: String) {
        try{
            runOnUiThread {
    //            setContentView(linearLayout)
    //            linearLayout.orientation = LinearLayout.VERTICAL

                val responseOB = JSONObject(response)
                val i2790 = JSONObject(responseOB["I2790"].toString());
                val row = JSONArray(i2790["row"].toString())
                for(i in 0..row.length()-1){
                    val data = row[i] as JSONObject
                    Log.d("datass", "$i: $data")
                    makeTextView(data);
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    private fun makeTextView(job:JSONObject){
        val linearLayout = findViewById<LinearLayout>(R.id.contentBox)
        val textView = TextView(this)
        textView.text = job["MAKER_NAME"].toString()+" "+job["DESC_KOR"].toString()+" "+job["NUTR_CONT1"].toString()+"kcal "+job["SERVING_SIZE"].toString()+job["SERVING_UNIT"].toString()
        linearLayout.addView(textView)
    }
}
