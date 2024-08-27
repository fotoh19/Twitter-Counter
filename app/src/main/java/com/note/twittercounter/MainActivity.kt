package com.note.twittercounter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText

class MainActivity : AppCompatActivity() {
    private val maxChar = 280
    private  val urlLength = 23
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val inputText: AppCompatEditText = findViewById(R.id.inputText)
        val charCount: TextView = findViewById(R.id.charCount)
        val charRemain: TextView = findViewById(R.id.charRemain)
        val copyButton: AppCompatButton = findViewById(R.id.copybtn)
        val clearButton: AppCompatButton = findViewById(R.id.clearbtn)


            inputText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s.toString()
                    val (charCounter, isValid) = validateAndCountTwitterCharacters(input)
                    val charRemaining = maxChar - charCounter

                    charCount.text = "$charCounter / $maxChar"
                    charRemain.text = "$charRemaining"

                    if (!isValid || charCounter >= maxChar) {
                        inputText.filters = arrayOf(InputFilter.LengthFilter(maxChar))
                    } else {
                        inputText.filters = arrayOf()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
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

        private fun validateAndCountTwitterCharacters(input: String): Pair<Int, Boolean> {
            var count = 0
            val urlRegex = Regex("(https?://[\\w-]+(\\.[\\w-]+)+[/#?]?.*$)")
            var remainingText = input

            val urlMatches = urlRegex.findAll(input)
            for (urlMatch in urlMatches) {
                count += urlLength
                remainingText = remainingText.replace(urlMatch.value, "")
            }

            for (char in remainingText) {
                count += when {
                    char.isSurrogate() -> 1
                    else -> 1
                }
            }

            val isValid = count <= maxChar

            return Pair(count, isValid)
        }
    }
