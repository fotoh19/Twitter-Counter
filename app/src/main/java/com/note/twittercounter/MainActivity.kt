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
    private val urlLength = 23

    @SuppressLint("SuspiciousIndentation")
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

            @SuppressLint("SetTextI18n")
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

    private fun countTwitterCharacters(text: String): Int {
        var count = 0
        var index = 0

        while (index < text.length) {
            val codePoint = text.codePointAt(index)
            val charCount = Character.charCount(codePoint)

            val isSpecialCharacter = codePoint in 0x1F600..0x1F64F || // Emoticons
                    codePoint in 0x1F300..0x1F5FF || // Misc symbols and pictographs
                    codePoint in 0x1F680..0x1F6FF || // Transport and map symbols
                    codePoint in 0x1F700..0x1F77F || // Alchemical symbols
                    codePoint in 0x1F780..0x1F7FF || // Geometric shapes
                    codePoint in 0x1F800..0x1F8FF || // Supplemental Arrows-C
                    codePoint in 0x1F900..0x1F9FF || // Supplemental Symbols and Pictographs
                    codePoint in 0x1FA00..0x1FA6F || // Chess symbols
                    codePoint in 0x1FA70..0x1FAFF || // Symbols and Pictographs Extended-A
                    codePoint in 0x2600..0x26FF ||   // Misc symbols
                    codePoint in 0x2700..0x27BF ||   // Dingbats
                    codePoint in 0x2300..0x23FF     // Misc technical
            val additionalCount = if (isSpecialCharacter) 2 else 1

            count += additionalCount
            index += charCount
        }

        return count
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

        count += countTwitterCharacters(remainingText)

        val isValid = count <= maxChar

        return Pair(count, isValid)
    }
}
