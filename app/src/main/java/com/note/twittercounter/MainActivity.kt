package com.note.twittercounter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class MainActivity : AppCompatActivity() {
    private val maxChar = 280
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val inputText: AppCompatEditText = findViewById(R.id.inputText)
        val charCount: TextView = findViewById(R.id.charCount)
        val charRemain: TextView = findViewById(R.id.charRemain)
        val copyButton: AppCompatButton = findViewById(R.id.copybtn)
        val clearButton: AppCompatButton = findViewById(R.id.clearbtn)
        val postButton: AppCompatButton = findViewById(R.id.postbtn)

        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }


            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCounter = s?.length ?: 0
                val charRemaining = maxChar - charCounter
                charCount.text = "$charCounter /280"
                charRemain.text = "$charRemaining"

                if (charCounter >= maxChar) {
                    inputText.filters = arrayOf(android.text.InputFilter.LengthFilter(maxChar))
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }


        })
        copyButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", inputText.text.toString())
            clipboard.setPrimaryClip(clip)
        }
        clearButton.setOnClickListener {
            inputText.text?.clear()
        }

    }

}
