package com.rommelrico.celebrityguessandroid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    var celebURLs = ArrayList<String>()
    var celebNames = ArrayList<String>()
    var chosenCeleb = 0
    var answers = arrayOfNulls<String>(4)
    var locationOfCorrectAnswer = 0
    var imageView: ImageView? = null
    var button0: Button? = null
    var button1: Button? = null
    var button2: Button? = null
    var button3: Button? = null

    inner class DownloadTask: AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String): String? {
            var result = ""
            val url: URL
            var urlConnection: HttpURLConnection? = null

            try {
                url = URL(urls[0])
                urlConnection = url.openConnection() as HttpURLConnection

                val input = urlConnection.inputStream
                val reader = InputStreamReader(input)
                var data = reader.read()

                while (data != -1) {
                    val current = data.toChar()
                    result += current
                    data = reader.read()
                }

                return result

            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }

    inner class ImageDownloader: AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            try {
                val url = URL(urls[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                return BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

    }

    fun newQuestion() {
        try {
            val rand = Random()
            chosenCeleb = rand.nextInt(celebURLs.size)
            val imageTask = ImageDownloader()
            val celebImage = imageTask.execute(celebURLs[chosenCeleb]).get()
            imageView?.setImageBitmap(celebImage)
            locationOfCorrectAnswer = rand.nextInt(4)

            var incorrectAnswerLocation: Int
            for (i in 0..3) {
                if (i == locationOfCorrectAnswer) {
                    answers[i] = celebNames[chosenCeleb]
                } else {
                    incorrectAnswerLocation = rand.nextInt(celebURLs.size)

                    while (incorrectAnswerLocation == chosenCeleb) {
                        incorrectAnswerLocation = rand.nextInt(celebURLs.size)
                    }

                    answers[i] = celebNames[incorrectAnswerLocation]
                }
            }

            button0?.text = answers[0]
            button1?.text = answers[1]
            button2?.text = answers[2]
            button3?.text = answers[3]
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        button0 = findViewById(R.id.button0)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)

        val task = DownloadTask()
        var result: String? = null

        try {
            result = task.execute("http://www.posh24.se/kandisar").get()

            val splitResult = result!!.split("<div class=\"listedArticles\">".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var p = Pattern.compile("img src=\"(.*?)\"")
            var m = p.matcher(splitResult[0])

            while (m.find()) {
                celebURLs.add(m.group(1))
            }

            p = Pattern.compile("alt=\"(.*?)\"")
            m = p.matcher(splitResult[0])

            while (m.find()) {
                celebNames.add(m.group(1))
            }

            newQuestion()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun celebChosen(view: View) {
        if (view.tag.toString() == Integer.toString(locationOfCorrectAnswer)) {
            Toast.makeText(applicationContext, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Wrong! It was ${celebNames[chosenCeleb]}", Toast.LENGTH_SHORT).show()
        }

        newQuestion()
    }

}
