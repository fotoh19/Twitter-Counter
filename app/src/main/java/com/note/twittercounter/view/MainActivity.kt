package com.note.twittercounter.view

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
import com.note.twittercounter.ViewModel.TwitterViewModel

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
    }
}