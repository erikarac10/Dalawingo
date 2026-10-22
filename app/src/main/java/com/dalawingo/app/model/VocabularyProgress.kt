package com.dalawingo.app.model

import java.util.Locale

/** The course teaches phrases; each Tagalog phrase is tracked independently. */
fun vocabularyKey(phrase: Phrase): String = phrase.tagalog.trim().lowercase(Locale.ROOT)

data class AnswerAttempt(
    val vocabularyId: String,
    val correct: Boolean,
    val exerciseType: ExerciseType? = null
)

enum class VocabularyLevel { NEW, WEAK, DEVELOPING, MASTERED }

data class VocabularyProgress(
    val attempts: Int = 0,
    val correct: Int = 0,
    val correctStreak: Int = 0,
    val intervalDays: Int = 0,
    val firstSeenMillis: Long = 0L,
    val lastReviewedDay: Long = -1L,
    val nextReviewDay: Long = 0L
) {
    val level: VocabularyLevel get() = when {
        attempts == 0 -> VocabularyLevel.NEW
        correctStreak >= 4 && intervalDays >= 7 -> VocabularyLevel.MASTERED
        attempts > correct && correctStreak < 2 -> VocabularyLevel.WEAK
        else -> VocabularyLevel.DEVELOPING
    }

    fun isDue(todayEpochDay: Long): Boolean =
        attempts > 0 && nextReviewDay <= todayEpochDay
}

/**
 * Review interval grows after consecutive correct answers; incorrect answers reset it.
 * Attempts are committed only when a full lesson or practice round is completed.
 */
fun updateVocabulary(
    current: Map<String, VocabularyProgress>,
    introduced: List<Phrase>,
    attempts: List<AnswerAttempt>,
    todayEpochDay: Long,
    nowMillis: Long
): Map<String, VocabularyProgress> {
    val result = current.toMutableMap()
    introduced.forEach { phrase ->
        val id = vocabularyKey(phrase)
        if (id !in result) result[id] = VocabularyProgress(firstSeenMillis = nowMillis)
    }
    attempts.forEach { attempt ->
        val previous = result[attempt.vocabularyId]
            ?: VocabularyProgress(firstSeenMillis = nowMillis)
        val streak = if (attempt.correct) previous.correctStreak + 1 else 0
        val interval = if (!attempt.correct) 1 else when (streak) {
            1 -> 1
            2 -> 3
            3 -> 7
            4 -> 14
            5 -> 30
            else -> 60
        }
        result[attempt.vocabularyId] = previous.copy(
            attempts = previous.attempts + 1,
            correct = previous.correct + if (attempt.correct) 1 else 0,
            correctStreak = streak,
            intervalDays = interval,
            lastReviewedDay = todayEpochDay,
            nextReviewDay = todayEpochDay + interval
        )
    }
    return result
}
