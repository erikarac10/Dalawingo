package com.dalawingo.app.model

import kotlin.random.Random

enum class QuestTier(val label: String, val medal: String, val pesos: Int) {
    BRONZE("BRONZE", "🥉", 5),
    SILVER("SILVER", "🥈", 10),
    GOLD("GOLD", "🥇", 15)
}

enum class QuestMetric {
    XP, LESSONS, CORRECT, ANSWERED, COMBO, PERFECT, PRACTICE_CORRECT
}

data class QuestStats(
    val xp: Int = 0,
    val lessons: Int = 0,
    val correct: Int = 0,
    val answered: Int = 0,
    val bestCombo: Int = 0,
    val perfectLessons: Int = 0,
    val practiceCorrect: Int = 0
)

data class DailyQuest(
    val id: String,
    val tier: QuestTier,
    val metric: QuestMetric,
    val target: Int,
    val title: String
) {
    fun progress(stats: QuestStats): Int = when (metric) {
        QuestMetric.XP -> stats.xp
        QuestMetric.LESSONS -> stats.lessons
        QuestMetric.CORRECT -> stats.correct
        QuestMetric.ANSWERED -> stats.answered
        QuestMetric.COMBO -> stats.bestCombo
        QuestMetric.PERFECT -> stats.perfectLessons
        QuestMetric.PRACTICE_CORRECT -> stats.practiceCorrect
    }

    fun isCompleted(stats: QuestStats): Boolean = progress(stats) >= target
}

/** All quest targets can be achieved by replaying unlocked lessons or doing practice. */
val dailyQuestPool = listOf(
    DailyQuest("b_xp20", QuestTier.BRONZE, QuestMetric.XP, 20, "Earn 20 XP"),
    DailyQuest("b_lesson1", QuestTier.BRONZE, QuestMetric.LESSONS, 1, "Finish 1 lesson"),
    DailyQuest("b_correct5", QuestTier.BRONZE, QuestMetric.CORRECT, 5, "5 correct answers"),
    DailyQuest("b_answered6", QuestTier.BRONZE, QuestMetric.ANSWERED, 6, "Answer 6 questions"),
    DailyQuest("b_combo3", QuestTier.BRONZE, QuestMetric.COMBO, 3, "3 correct in a row"),

    DailyQuest("s_xp40", QuestTier.SILVER, QuestMetric.XP, 40, "Earn 40 XP"),
    DailyQuest("s_lesson2", QuestTier.SILVER, QuestMetric.LESSONS, 2, "Finish 2 lessons"),
    DailyQuest("s_correct12", QuestTier.SILVER, QuestMetric.CORRECT, 12, "12 correct answers"),
    DailyQuest("s_answered14", QuestTier.SILVER, QuestMetric.ANSWERED, 14, "Answer 14 questions"),
    DailyQuest("s_combo4", QuestTier.SILVER, QuestMetric.COMBO, 4, "4 correct in a row"),
    DailyQuest("s_perfect1", QuestTier.SILVER, QuestMetric.PERFECT, 1, "1 perfect lesson"),
    DailyQuest("s_practice6", QuestTier.SILVER, QuestMetric.PRACTICE_CORRECT, 6, "6 practice answers correct"),

    DailyQuest("g_xp65", QuestTier.GOLD, QuestMetric.XP, 65, "Earn 65 XP"),
    DailyQuest("g_lesson3", QuestTier.GOLD, QuestMetric.LESSONS, 3, "Finish 3 lessons"),
    DailyQuest("g_correct20", QuestTier.GOLD, QuestMetric.CORRECT, 20, "20 correct answers"),
    DailyQuest("g_answered24", QuestTier.GOLD, QuestMetric.ANSWERED, 24, "Answer 24 questions"),
    DailyQuest("g_combo6", QuestTier.GOLD, QuestMetric.COMBO, 6, "6 correct in a row"),
    DailyQuest("g_perfect2", QuestTier.GOLD, QuestMetric.PERFECT, 2, "2 perfect lessons"),
    DailyQuest("g_practice12", QuestTier.GOLD, QuestMetric.PRACTICE_CORRECT, 12, "12 practice answers correct")
)

private val questById = dailyQuestPool.associateBy { it.id }
fun findDailyQuest(id: String): DailyQuest? = questById[id]

/** A stable daily shuffle. Three different types, one of each reward tier. */
fun selectDailyQuests(epochDay: Long): List<DailyQuest> {
    val random = Random((epochDay xor 0x4D41_4E47L).toInt())
    val chosen = mutableListOf<DailyQuest>()
    val usedTypes = mutableSetOf<QuestMetric>()
    QuestTier.entries.forEach { tier ->
        val choices = dailyQuestPool.filter { it.tier == tier && it.metric !in usedTypes }
        val selected = choices[random.nextInt(choices.size)]
        chosen += selected
        usedTypes += selected.metric
    }
    return chosen
}

/** Maximum consecutive correct answers within one completed round. */
fun longestCorrectRun(answers: List<AnswerAttempt>): Int {
    var current = 0
    var best = 0
    answers.forEach { attempt ->
        current = if (attempt.correct) current + 1 else 0
        if (current > best) best = current
    }
    return best
}
