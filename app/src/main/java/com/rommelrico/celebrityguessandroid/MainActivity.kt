package com.rommelrico.celebrityguessandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.util.ArrayList

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun celebChosen(view: View) {
        // Placeholder.
    }

}
