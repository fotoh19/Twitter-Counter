
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TwitterViewModel : ViewModel() {

     val maxChar = 280
     val urlLength = 23

    private val _charCount = MutableLiveData<Int>()
    val charCount: LiveData<Int> = _charCount

    private val _charRemaining = MutableLiveData<Int>()
    val charRemaining: LiveData<Int> = _charRemaining

    private val _isValid = MutableLiveData<Boolean>()
    val isValid: LiveData<Boolean> = _isValid

    fun onTextChanged(input: String) {
        val (charCounter, valid) = validateAndCountTwitterCharacters(input)
        _charCount.value = charCounter
        _charRemaining.value = maxChar - charCounter
        _isValid.value = valid
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