package com.note.twittercounter.view

import TwitterViewModel
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.note.twittercounter.R
import com.note.twittercounter.model.createRetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST


class MainActivity : AppCompatActivity() {

    private val twitterViewModel = TwitterViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText: AppCompatEditText = findViewById(R.id.inputText)
        val charCount: TextView = findViewById(R.id.charCount)
        val charRemain: TextView = findViewById(R.id.charRemain)
        val copyButton: AppCompatButton = findViewById(R.id.copybtn)
        val clearButton: AppCompatButton = findViewById(R.id.clearbtn)
        val postButton:AppCompatButton = findViewById(R.id.postbtn)

        twitterViewModel.charCount.observe(this) { count ->
            charCount.text = "$count / ${twitterViewModel.maxChar}"
        }

        twitterViewModel.charRemaining.observe(this) { remaining ->
            charRemain.text = "$remaining"
        }

        twitterViewModel.isValid.observe(this) { isValid ->
            if (!isValid || twitterViewModel.charCount.value!! >= twitterViewModel.maxChar) {
                inputText.filters = arrayOf(InputFilter.LengthFilter(twitterViewModel.maxChar))
            } else {
                inputText.filters = arrayOf()
            }
        }

        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                twitterViewModel.onTextChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        copyButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", inputText.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Text Copied", Toast.LENGTH_SHORT).show()
        }

        clearButton.setOnClickListener {
            inputText.text?.clear()
        }
        postButton.setOnClickListener {
            val tweetText = inputText.text.toString()
            if (tweetText.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    postTweet(tweetText)

                }
            } else {
                Toast.makeText(this, "Please enter text for the tweet", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun postTweet(status: String) {
        val twitterApi = createRetrofitInstance()

        val call = twitterApi.postTweet(status)
        call.enqueue(object : Callback<POST> {
            override fun onResponse(call: Call<POST>, response: Response<POST>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity,"tweet post",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity,"tweet not post",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<POST>, t: Throwable) {

                Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
            }
        })
    }

    }
