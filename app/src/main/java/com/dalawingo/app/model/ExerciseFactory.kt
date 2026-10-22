package com.dalawingo.app.model

import java.time.LocalDate
import kotlin.random.Random

/** Picture matching and speech/text production use the same vocabulary history. */
enum class ExerciseType {
    CHOOSE_TAGALOG, CHOOSE_ENGLISH, WORD_BANK, LISTEN,
    TYPE_TAGALOG, PICTURE_MATCH, IMAGE_FLASHCARD, SPEAK_TAGALOG, STORY,
    MATCH_AUDIO, SPEAK_SET, SPEAK_FLASHCARDS,
    MATCH_TEXT, FILL_BLANK, COMPLETE_SENTENCE, LISTEN_MISSING,
    TYPE_HEARD, COMPLETE_CHAT
}

data class WordTile(val text: String, val id: Int)

data class Exercise(
    val type: ExerciseType,
    val phrase: Phrase,
    val answers: List<String> = emptyList(),
    val tiles: List<WordTile> = emptyList(),
    val scene: String = "",
    val speaker: String = "",
    val pairs: List<Phrase> = emptyList(),
    val promptText: String = "",
    val blankWord: String = ""
) {
    val correctAnswer: String get() = when (type) {
        ExerciseType.CHOOSE_ENGLISH, ExerciseType.LISTEN -> phrase.english
        ExerciseType.FILL_BLANK, ExerciseType.COMPLETE_SENTENCE,
        ExerciseType.LISTEN_MISSING -> blankWord
        else -> phrase.tagalog
    }
}

/** Authentic response prompts; we never pair an unrelated question with a random answer. */
private val chatPrompts = mapOf(
    "Kumusta" to "Kumusta!",
    "Magandang umaga" to "Umaga na!",
    "Magandang gabi" to "Gabi na!",
    "Masaya akong makilala ka" to "Kumusta! Ako si Ana.",
    "Kita tayo mamaya" to "Aalis muna ako.",
    "Paumanhin" to "May itatanong ako.",
    "Hindi ko maintindihan" to "Mabilis ba akong magsalita?",
    "Masarap ito" to "Kumusta ang pagkain?",
    "Puwede bang umorder?" to "Handa ka nang umorder?",
    "Magkano ito?" to "Gusto mong bilhin ito?",
    "Walang asukal" to "Gusto mo ba ng asukal?",
    "Nasaan ang bus?" to "Naghihintay ka ng bus?",
    "Tara na" to "Handa ka na ba?",
    "Naliligaw ako" to "Kailangan mo ba ng tulong?",
    "Kailangan ko ng tulong" to "May problema ba?",
    "Uuwi na ako" to "Saan ka pupunta?",
    "Kumakain ako" to "Anong ginagawa mo?",
    "Umiinom ako ng tubig" to "Anong ginagawa mo?",
    "Nag-aaral ako" to "Anong ginagawa mo?",
    "Nagbabasa ako" to "Anong ginagawa mo?",
    "Natutulog ako" to "Anong ginagawa mo?",
    "Salamat" to "Tinulungan kita!",
    "Walang anuman" to "Salamat sa tulong mo!",
    "Paalam" to "Aalis na ako.",
    "Ayos lang ako" to "Kumusta ka?",
    "Ang pangalan ko ay Ana" to "Ano ang pangalan mo?",
    "Kumusta ka?" to "Kumusta!",
    "Oo" to "Gusto mo ba ng tubig?",
    "Hindi" to "Gusto mo ba ng tubig?",
    "Pasensya na" to "Nasaktan ba kita?",
    "Walang problema" to "Pasensya na!",
    "Gutom ako" to "Kumusta ka?",
    "Gusto ko ng tubig" to "Ano ang gusto mo?",
    "Kain tayo" to "Handa na ang pagkain!",
    "Masaya ako" to "Kumusta ka?",
    "Pagod ako" to "Kumusta ka?",
    "Ingat ka" to "Aalis na ako.",
    "Mahal kita" to "Mahal kita!",
    "Miss na kita" to "Matagal na tayong hindi nagkita!"
)

/** Only show visual matches when the emoji actually depicts the intended vocabulary. */
private val pictureVocabulary = mapOf(
    "nanay" to "👩", "tatay" to "👨", "kaibigan" to "🧑‍🤝‍🧑",
    "bata" to "🧒", "guro" to "👩‍🏫", "pamilya" to "👨‍👩‍👧‍👦",
    "isa" to "1️⃣", "dalawa" to "2️⃣", "tatlo" to "3️⃣",
    "apat" to "4️⃣", "lima" to "5️⃣", "sampu" to "🔟",
    "kanin" to "🍚", "tubig" to "💧", "tinapay" to "🍞",
    "isda" to "🐟", "prutas" to "🍎", "kape" to "☕",
    "bahay" to "🏠", "paaralan" to "🏫", "palengke" to "🛒",
    "ospital" to "🏥", "istasyon" to "🚉", "dalampasigan" to "🏖️",
    "kaliwa" to "⬅️", "kanan" to "➡️", "diretso" to "⬆️"
)

fun pictureFor(tagalog: String): String? = pictureVocabulary[tagalog.trim().lowercase()]

private fun createExercise(
    type: ExerciseType, phrase: Phrase, pool: List<Phrase>,
    scene: String = "", speaker: String = ""
): Exercise {
    if (type == ExerciseType.COMPLETE_CHAT) {
        val prompt = chatPrompts[phrase.tagalog] ?: return createExercise(
            ExerciseType.CHOOSE_TAGALOG, phrase, pool
        )
        val incorrect = pool.map { it.tagalog }
            .filter { it != phrase.tagalog && it != prompt }.distinct().shuffled().take(2)
        return Exercise(type, phrase, answers = (incorrect + phrase.tagalog).shuffled(),
            promptText = prompt)
    }
    // Multiple words belong to a single matching/speaking mini-game.
    if (type == ExerciseType.MATCH_AUDIO || type == ExerciseType.MATCH_TEXT ||
        type == ExerciseType.SPEAK_SET || type == ExerciseType.SPEAK_FLASHCARDS) {
        val needed = if (type == ExerciseType.MATCH_AUDIO || type == ExerciseType.MATCH_TEXT) 4 else 5
        val choices = (listOf(phrase) + pool.shuffled())
            .distinctBy { it.tagalog.lowercase() }.take(needed)
        return Exercise(type = type, phrase = phrase, pairs = choices)
    }
    if (type == ExerciseType.FILL_BLANK || type == ExerciseType.COMPLETE_SENTENCE ||
        type == ExerciseType.LISTEN_MISSING) {
        val words = phrase.tagalog.replace(Regex("[?.!,]"), "")
            .split(Regex("\\s+")).filter { it.isNotBlank() }
        val blanks = if (type == ExerciseType.COMPLETE_SENTENCE) 2 else 1
        if (words.size < blanks + 1) return createExercise(ExerciseType.CHOOSE_ENGLISH, phrase, pool)
        val first = if (blanks == 2) (words.size - 2).coerceAtLeast(0)
            else if (words.size > 2) words.size / 2 else words.lastIndex
        val missing = words.subList(first, first + blanks)
        val shown = words.mapIndexed { i, word ->
            if (i in first until first + blanks) "_____" else word
        }.joinToString(" ")
        val poolWords = pool.flatMap { it.tagalog.replace(Regex("[?.!,]"), "")
            .split(Regex("\\s+")) }.filter { it.isNotBlank() && it !in missing }
            .distinct().shuffled().take(if (blanks == 1) 3 else 4)
        return if (type == ExerciseType.COMPLETE_SENTENCE) Exercise(
            type, phrase, tiles = (missing + poolWords).shuffled().mapIndexed { i, w -> WordTile(w, i) },
            promptText = shown, blankWord = missing.joinToString(" ")
        ) else Exercise(type, phrase, answers = (missing + poolWords).shuffled(),
            promptText = shown, blankWord = missing.joinToString(" "))
    }
    if (type == ExerciseType.WORD_BANK) {
        val tokens = phrase.tagalog.replace(Regex("[?.!,]"), "")
            .split(Regex("\\s+")).filter { it.isNotBlank() }
        val wrong = pool.flatMap { it.tagalog.replace(Regex("[?.!,]"), "")
            .split(Regex("\\s+")) }
            .filter { it.isNotBlank() && it !in tokens }.distinct().shuffled().take(4)
        return Exercise(type, phrase,
            tiles = (tokens + wrong).shuffled().mapIndexed { i, word -> WordTile(word, i) })
    }
    if (type in setOf(ExerciseType.TYPE_TAGALOG, ExerciseType.TYPE_HEARD, ExerciseType.SPEAK_TAGALOG, ExerciseType.IMAGE_FLASHCARD)) {
        return Exercise(type, phrase)
    }
    val tagalogOptions = type == ExerciseType.CHOOSE_TAGALOG ||
        type == ExerciseType.PICTURE_MATCH || type == ExerciseType.STORY
    val correct = if (tagalogOptions) phrase.tagalog else phrase.english
    val candidates = if (type == ExerciseType.PICTURE_MATCH) {
        pool.filter { pictureFor(it.tagalog) != null && it.tagalog != phrase.tagalog }
    } else pool
    val wrong = candidates.map { if (tagalogOptions) it.tagalog else it.english }
        .filter { it != correct }.distinct().shuffled().take(3)
    return Exercise(type, phrase, answers = (wrong + correct).shuffled(),
        scene = scene, speaker = speaker)
}

/** Every path node is an ordinary lesson. Exercise families are mixed each run,
 *  including speaking, listening, translation, grammar, and short conversations.
 *  Written production is Tagalog-only; English is learned through choice and word banks.
 */
fun lessonExercises(lesson: Lesson, round: Int = 1): List<Exercise> {
    val unit = course.first { parent -> parent.lessons.any { it.id == lesson.id } }
    val pool = unit.lessons.flatMap { it.phrases }
    val phraseOrder = lesson.phrases.shuffled()
    val families = mutableListOf(
        ExerciseType.CHOOSE_TAGALOG, ExerciseType.CHOOSE_ENGLISH,
        ExerciseType.WORD_BANK, ExerciseType.FILL_BLANK,
        ExerciseType.COMPLETE_SENTENCE, ExerciseType.LISTEN,
        ExerciseType.LISTEN_MISSING, ExerciseType.MATCH_AUDIO,
        ExerciseType.MATCH_TEXT, ExerciseType.TYPE_TAGALOG,
        ExerciseType.TYPE_HEARD, ExerciseType.SPEAK_TAGALOG,
        ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS,
        ExerciseType.COMPLETE_CHAT
    )
    // In each six-question lesson, include different skills instead of only choice questions.
    val listening = listOf(ExerciseType.LISTEN, ExerciseType.MATCH_AUDIO,
        ExerciseType.LISTEN_MISSING, ExerciseType.TYPE_HEARD).random()
    val production = listOf(ExerciseType.WORD_BANK, ExerciseType.TYPE_TAGALOG,
        ExerciseType.COMPLETE_SENTENCE, ExerciseType.FILL_BLANK).random()
    val speaking = listOf(ExerciseType.SPEAK_TAGALOG, ExerciseType.SPEAK_SET,
        ExerciseType.SPEAK_FLASHCARDS, ExerciseType.MATCH_TEXT).random()
    val chosen = (listOf(listening, production, speaking) +
        families.filterNot { it in setOf(listening, production, speaking) }
            .shuffled().take(3)).shuffled()
    return phraseOrder.mapIndexed { index, phrase ->
        val requested = chosen[index % chosen.size]
        // Real conversation pairs only; otherwise use a translation question.
        val selected = if (requested == ExerciseType.COMPLETE_CHAT)
            lesson.phrases.firstOrNull { it.tagalog in chatPrompts } ?: phrase else phrase
        createExercise(requested, selected,
            if (requested in setOf(ExerciseType.MATCH_AUDIO, ExerciseType.MATCH_TEXT,
                    ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS)) lesson.phrases else pool)
    }
}

/** Six distinct learned phrases, weighted toward weak, due, recent and older. */
fun practiceExercises(
    completed: Set<String>,
    vocabulary: Map<String, VocabularyProgress> = emptyMap(),
    todayEpochDay: Long = LocalDate.now().toEpochDay()
): List<Exercise> {
    val learned = allLessons.filter { it.id in completed }.flatMap { it.phrases }
    val pool = (if (learned.isEmpty()) allPhrases.take(18) else learned)
        .distinctBy(::vocabularyKey)
    val selected = linkedMapOf<String, Phrase>()
    fun addFrom(options: List<Phrase>, count: Int) {
        var added = 0
        options.shuffled().forEach { phrase ->
            if (added < count && selected.size < 6 &&
                selected.putIfAbsent(vocabularyKey(phrase), phrase) == null) added++
        }
    }
    val weak = pool.filter { vocabulary[vocabularyKey(it)]?.level == VocabularyLevel.WEAK }
    val due = pool.filter { vocabulary[vocabularyKey(it)]?.isDue(todayEpochDay) == true }
    val recent = pool.sortedByDescending { vocabulary[vocabularyKey(it)]?.firstSeenMillis ?: 0L }.take(12)
    val older = pool.sortedBy { vocabulary[vocabularyKey(it)]?.firstSeenMillis ?: 0L }
        .take((pool.size / 2).coerceAtLeast(1))
    addFrom(weak, 2)
    addFrom(due, 2)
    addFrom(recent.filter { vocabulary[vocabularyKey(it)]?.level == VocabularyLevel.NEW }, 1)
    if (selected.size < 5) addFrom(recent, 1)
    addFrom(older, 1)
    addFrom(pool.filter { vocabulary[vocabularyKey(it)]?.level != VocabularyLevel.MASTERED }, 6)
    addFrom(pool, 6)
    val kinds = listOf(ExerciseType.CHOOSE_ENGLISH, ExerciseType.CHOOSE_TAGALOG,
        ExerciseType.WORD_BANK, ExerciseType.LISTEN_MISSING, ExerciseType.MATCH_AUDIO,
        ExerciseType.FILL_BLANK).shuffled()
    return selected.values.shuffled().mapIndexed { i, phrase ->
        val kind = kinds[i % kinds.size]
        createExercise(if (kind == ExerciseType.PICTURE_MATCH && pictureFor(phrase.tagalog) == null)
            ExerciseType.CHOOSE_TAGALOG else kind, phrase,
            if (kind in setOf(ExerciseType.MATCH_AUDIO, ExerciseType.MATCH_TEXT, ExerciseType.SPEAK_SET,
                ExerciseType.SPEAK_FLASHCARDS)) pool else allPhrases)
    }
}

/** These are small scripted conversations, not a claim of open-ended AI dialogue. */
private val storyPrompts = mapOf(
    1 to listOf(
        "Ana smiles when you arrive. Greet her.",
        "It's morning. How do you greet a friend?",
        "Ana helps you. What do you say?",
        "You're leaving. Say goodbye.",
        "Someone asks if you are okay. Say you're fine.",
        "You meet someone new. Be friendly."
    ),
    2 to listOf(
        "You meet someone's mother. What is 'mother'?",
        "A classmate is your friend. What do you call them?",
        "Someone asks you to count. Say 'one'.",
        "They ask for another number. Say 'two'.",
        "Ask 'What is this?'",
        "You need to ask 'Where are you?'"
    ),
    3 to listOf(
        "You see a bowl of rice. Name it.",
        "You are thirsty. Name the drink 'water'.",
        "Name the food 'bread'.",
        "You enjoy the meal. Say it's delicious.",
        "At the café, order coffee.",
        "Ask how much it costs."
    ),
    4 to listOf(
        "You're visiting town. Say 'house'.",
        "Find the school. Say 'school'.",
        "Point to the hospital. Say 'hospital'.",
        "A friend asks which way. Say 'left'.",
        "You're getting on a bus. Ask where it is.",
        "You need help. Ask for help."
    ),
    5 to listOf(
        "A friend asks how you feel. Say you're happy.",
        "After a long day, say you're tired.",
        "Say you're drinking water.",
        "Tell someone you're going home.",
        "Send a caring message: 'Take care'.",
        "Say 'See you tomorrow'."
    )
)

private fun storyPhraseSequence(unit: CourseUnit): List<Phrase> {
    val p = unit.lessons.flatMap { it.phrases }
    val positions = when (unit.number) {
        1 -> listOf(0, 1, 4, 3, 9, 10)
        2 -> listOf(0, 2, 6, 7, 12, 13)
        3 -> listOf(0, 1, 2, 8, 14, 13)
        4 -> listOf(0, 1, 3, 6, 12, 15)
        else -> listOf(1, 0, 7, 8, 14, 17)
    }
    return positions.map { p[it] }
}

fun pathActivityExercises(activity: PathActivity, completed: Set<String>): List<Exercise> {
    val unit = course.first { it.number == activity.unitNumber }
    val unitPhrases = unit.lessons.flatMap { it.phrases }
    val learned = allLessons.filter { it.id in completed }.flatMap { it.phrases }
    val pool = (if (learned.isEmpty()) unitPhrases else learned).distinctBy(::vocabularyKey)
    return when (activity.type) {
        PathActivityType.STORY -> storyPhraseSequence(unit).mapIndexed { index, phrase ->
            createExercise(if (index % 2 == 1 && phrase.tagalog in chatPrompts)
                ExerciseType.COMPLETE_CHAT else ExerciseType.STORY, phrase, unitPhrases,
                scene = storyPrompts[unit.number].orEmpty()[index],
                speaker = if (index % 2 == 0) "ANA" else "YOU")
        }
        PathActivityType.PICTURE -> {
            val eligible = pool.filter { pictureFor(it.tagalog) != null }
            val fallback = allPhrases.filter { pictureFor(it.tagalog) != null }
            // A newly unlocked picture lab can have fewer than six picturable words.
            (eligible.ifEmpty { fallback }).distinctBy(::vocabularyKey)
                .shuffled().take(6).mapIndexed { i, phrase ->
                    createExercise(if (i % 2 == 0) ExerciseType.PICTURE_MATCH
                        else ExerciseType.SPEAK_FLASHCARDS, phrase, fallback)
                }
        }
        PathActivityType.LISTENING -> pool.shuffled().take(6)
            .mapIndexed { i, phrase -> createExercise(
                if (i % 3 == 0) ExerciseType.MATCH_AUDIO else ExerciseType.LISTEN, phrase, pool
            ) }
        PathActivityType.REVIEW -> pool.shuffled().take(6).mapIndexed { i, phrase ->
            createExercise(listOf(ExerciseType.WORD_BANK, ExerciseType.SPEAK_SET,
                ExerciseType.CHOOSE_ENGLISH, ExerciseType.MATCH_AUDIO)[i % 4], phrase, pool)
        }
    }
}
