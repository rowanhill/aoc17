import java.io.File

fun main(args: Array<String>) {
    var validPhrases = 0;
    var validAnagramPhrases = 0;
    File("src/input.txt").readLines().forEach { line ->
        val words = line.split(" ")
        if (isPhraseValid(words)) {
            validPhrases += 1
        }
        if (isPhraseAnagramValid(words)) {
            validAnagramPhrases += 1
        }
    }
    println(validPhrases)
    println(validAnagramPhrases)
}

fun isPhraseValid(words: List<String>): Boolean {
    val seenWords = HashSet<String>();
    words.forEach { word ->
        if (seenWords.contains(word)) {
            return false
        } else {
            seenWords.add(word)
        }
    }
    return true
}

fun isPhraseAnagramValid(words: List<String>): Boolean {
    val seenOrderedWords = HashSet<String>();
    words.forEach { word ->
        val orderedWordArray = word.toCharArray()
        orderedWordArray.sort()
        val orderedWord = orderedWordArray.joinToString()
        if (seenOrderedWords.contains(orderedWord)) {
            return false
        } else {
            seenOrderedWords.add(orderedWord)
        }
    }
    return true
}