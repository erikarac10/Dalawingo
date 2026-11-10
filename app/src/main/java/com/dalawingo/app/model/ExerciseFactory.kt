package com.dalawingo.app.model

import java.time.LocalDate
import kotlin.random.Random

/** Picture matching and speech/text production use the same vocabulary history. */
enum class ExerciseType {
    CHOOSE_TAGALOG, CHOOSE_ENGLISH, WORD_BANK, LISTEN,
    TYPE_TAGALOG, PICTURE_MATCH, IMAGE_FLASHCARD, SPEAK_TAGALOG, STORY,
    MATCH_AUDIO, SPEAK_SET, SPEAK_FLASHCARDS
}

data class WordTile(val text: String, val id: Int)

data class Exercise(
    val type: ExerciseType,
    val phrase: Phrase,
    val answers: List<String> = emptyList(),
    val tiles: List<WordTile> = emptyList(),
    val scene: String = "",
    val speaker: String = "",
    val pairs: List<Phrase> = emptyList()
) {
    val correctAnswer: String get() = when (type) {
        ExerciseType.CHOOSE_ENGLISH, ExerciseType.LISTEN -> phrase.english
        else -> phrase.tagalog
    }
}

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
    // Multiple words belong to a single matching/speaking mini-game.
    if (type == ExerciseType.MATCH_AUDIO || type == ExerciseType.SPEAK_SET ||
        type == ExerciseType.SPEAK_FLASHCARDS) {
        val needed = if (type == ExerciseType.MATCH_AUDIO) 4 else 5
        val choices = (listOf(phrase) + pool.shuffled())
            .distinctBy { it.tagalog.lowercase() }.take(needed)
        return Exercise(type = type, phrase = phrase, pairs = choices)
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
    if (type == ExerciseType.TYPE_TAGALOG || type == ExerciseType.SPEAK_TAGALOG ||
        type == ExerciseType.IMAGE_FLASHCARD) {
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

fun lessonExercises(lesson: Lesson, round: Int = 1): List<Exercise> {
    val unit = course.first { parent -> parent.lessons.any { it.id == lesson.id } }
    val pool = unit.lessons.flatMap { it.phrases }
    val plans = listOf(
        listOf(ExerciseType.CHOOSE_TAGALOG, ExerciseType.CHOOSE_ENGLISH,
            ExerciseType.LISTEN, ExerciseType.WORD_BANK, ExerciseType.CHOOSE_TAGALOG,
            ExerciseType.CHOOSE_ENGLISH),
        listOf(ExerciseType.MATCH_AUDIO, ExerciseType.CHOOSE_ENGLISH,
            ExerciseType.WORD_BANK, ExerciseType.LISTEN, ExerciseType.CHOOSE_TAGALOG,
            ExerciseType.SPEAK_SET),
        listOf(ExerciseType.SPEAK_TAGALOG, ExerciseType.WORD_BANK,
            ExerciseType.CHOOSE_ENGLISH, ExerciseType.LISTEN,
            ExerciseType.MATCH_AUDIO, ExerciseType.CHOOSE_TAGALOG),
        listOf(ExerciseType.CHOOSE_TAGALOG, ExerciseType.MATCH_AUDIO,
            ExerciseType.SPEAK_FLASHCARDS, ExerciseType.LISTEN,
            ExerciseType.SPEAK_TAGALOG, ExerciseType.WORD_BANK)
    )
    val phrases = lesson.phrases.shuffled(Random(lesson.id.hashCode() * 31 + round))
    return phrases.mapIndexed { index, phrase ->
        val requested = plans[(round - 1).coerceIn(0, 3)][index % 6]
        createExercise(if ((requested == ExerciseType.PICTURE_MATCH ||
                requested == ExerciseType.IMAGE_FLASHCARD) &&
            pictureFor(phrase.tagalog) == null) ExerciseType.CHOOSE_TAGALOG else requested,
            phrase, if (requested in setOf(ExerciseType.MATCH_AUDIO,
                ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS)) lesson.phrases
                else if (requested == ExerciseType.PICTURE_MATCH) allPhrases else pool)
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
        ExerciseType.WORD_BANK, ExerciseType.LISTEN, ExerciseType.MATCH_AUDIO,
        ExerciseType.SPEAK_TAGALOG).shuffled()
    return selected.values.shuffled().mapIndexed { i, phrase ->
        val kind = kinds[i % kinds.size]
        createExercise(if (kind == ExerciseType.PICTURE_MATCH && pictureFor(phrase.tagalog) == null)
            ExerciseType.CHOOSE_TAGALOG else kind, phrase,
            if (kind in setOf(ExerciseType.MATCH_AUDIO, ExerciseType.SPEAK_SET,
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
            createExercise(ExerciseType.STORY, phrase, unitPhrases,
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
