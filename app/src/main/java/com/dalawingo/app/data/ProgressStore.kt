package com.dalawingo.app.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dalawingo.app.model.AnswerAttempt
import com.dalawingo.app.model.ExerciseType
import com.dalawingo.app.model.AchievementDefinition
import com.dalawingo.app.model.AchievementMetric
import com.dalawingo.app.model.AchievementReward
import com.dalawingo.app.model.VocabularyLevel
import com.dalawingo.app.model.achievements
import com.dalawingo.app.model.QuestStats
import com.dalawingo.app.model.findDailyQuest
import com.dalawingo.app.model.longestCorrectRun
import com.dalawingo.app.model.selectDailyQuests
import com.dalawingo.app.model.VocabularyProgress
import com.dalawingo.app.model.updateVocabulary
import org.json.JSONObject
import com.dalawingo.app.model.CosmeticSlot
import com.dalawingo.app.model.cosmetics
import com.dalawingo.app.model.course
import com.dalawingo.app.model.allLessons
import com.dalawingo.app.model.allPhrases
import com.dalawingo.app.model.vocabularyKey
import kotlin.math.roundToInt
import com.dalawingo.app.model.pets
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.max

data class LearnerProgress(
    val name: String = "Learner",
    val completed: Set<String> = emptySet(),
    /** Four finished rounds turn one path node into a completed lesson. */
    val lessonRounds: Map<String, Int> = emptyMap(),
    val completedActivities: Set<String> = emptySet(),
    val golden: Set<String> = emptySet(),
    val rewardedUnits: Set<Int> = emptySet(),
    val xp: Int = 0,
    val pesos: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val lastActiveDate: String = "",
    val dailyDate: String = "",
    val dailyXp: Int = 0,
    val dailyLessons: Int = 0,
    val dailyCorrect: Int = 0,
    val dailyAnswered: Int = 0,
    val dailyPerfectLessons: Int = 0,
    val dailyBestCombo: Int = 0,
    val dailyPracticeCorrect: Int = 0,
    val dailyQuestIds: List<String> = emptyList(),
    val dailyQuestClaimed: Set<String> = emptySet(),
    val dailyPractices: Int = 0,
    val totalPractices: Int = 0,
    val vocabulary: Map<String, VocabularyProgress> = emptyMap(),
    val animalId: String = "tarsier",
    val ownedCosmetics: Set<String> = setOf("room_cozy"),
    val equippedHat: String? = null,
    val equippedOutfit: String? = null,
    val equippedAccessory: String? = null,
    val equippedRoom: String = "room_cozy",
    val achievementUnlockedAt: Map<String, String> = emptyMap(),
    val claimedAchievements: Set<String> = emptySet(),
    val xpBoostTokens: Int = 0,
    val xpBoostUntilMillis: Long = 0L,
    val unlockedAnimals: Set<String> = emptySet(),
    val earlyDays: Int = 0,
    val nightDays: Int = 0,
    val lastEarlyDay: String = "",
    val lastNightDay: String = "",
    val totalQuests: Int = 0,
    val fullQuestDays: Int = 0,
    val lastFullQuestDay: String = "",
    val speakingCorrect: Int = 0,
    val listeningCorrect: Int = 0,
    val matchingCorrect: Int = 0,
    val sentencesCorrect: Int = 0,
    val mistakesCorrected: Int = 0,
    val dueRecallCorrect: Int = 0,
    val visitedRooms: Set<String> = setOf("room_cozy"),
    val perfectRoundMasks: Map<String, Int> = emptyMap()
)

fun AchievementDefinition.currentValue(progress: LearnerProgress): Int = when (metric) {
    AchievementMetric.LESSONS -> progress.completed.size
    AchievementMetric.GOLDEN -> progress.golden.size
    AchievementMetric.STREAK -> progress.bestStreak
    AchievementMetric.XP -> progress.xp
    AchievementMetric.PRACTICES -> progress.totalPractices
    AchievementMetric.MASTERED -> progress.vocabulary.values.count { it.level == VocabularyLevel.MASTERED }
    AchievementMetric.EARLY_DAYS -> progress.earlyDays
    AchievementMetric.NIGHT_DAYS -> progress.nightDays
    AchievementMetric.QUESTS -> progress.totalQuests
    AchievementMetric.FULL_QUEST_DAYS -> progress.fullQuestDays
    AchievementMetric.SPEAKING -> progress.speakingCorrect
    AchievementMetric.LISTENING -> progress.listeningCorrect
    AchievementMetric.MATCHING -> progress.matchingCorrect
    AchievementMetric.SENTENCES -> progress.sentencesCorrect
    AchievementMetric.STORIES -> progress.completedActivities.count { it.endsWith("_story") }
    AchievementMetric.CORRECTIONS -> progress.mistakesCorrected
    AchievementMetric.DUE_RECALL -> progress.dueRecallCorrect
    AchievementMetric.COSMETICS -> progress.ownedCosmetics.size
    AchievementMetric.ROOMS -> progress.visitedRooms.size
    AchievementMetric.BONUS -> progress.completedActivities.size
    AchievementMetric.PERFECT_NODES -> progress.perfectRoundMasks.values.count { it and 15 == 15 }
    AchievementMetric.UNITS -> course.count { unit -> unit.lessons.all { it.id in progress.completed } }
    AchievementMetric.COURSE -> allLessons.count { it.id in progress.completed }
}

const val ROUNDS_PER_LESSON = 4

/** Older saves with completed lessons are treated as 4/4, never reset. */
fun LearnerProgress.roundsFor(lessonId: String): Int =
    if (lessonId in completed) ROUNDS_PER_LESSON
    else (lessonRounds[lessonId] ?: 0).coerceIn(0, ROUNDS_PER_LESSON)

/** 0–100 local learning-progress indicator, not an externally validated proficiency rating. */
fun LearnerProgress.tagalogScore(): Int {
    val lessonFraction = allLessons.sumOf { roundsFor(it.id) }.toDouble() /
        (allLessons.size.coerceAtLeast(1) * ROUNDS_PER_LESSON)
    val unique = allPhrases.distinctBy(::vocabularyKey)
    val vocabularyFraction = unique.sumOf { phrase ->
        when (vocabulary[vocabularyKey(phrase)]?.level) {
            VocabularyLevel.MASTERED -> 1.0
            VocabularyLevel.DEVELOPING -> 0.55
            VocabularyLevel.WEAK -> 0.15
            VocabularyLevel.NEW -> 0.05
            null -> 0.0
        }
    } / unique.size.coerceAtLeast(1)
    return ((lessonFraction * .65 + vocabularyFraction * .35) * 100)
        .roundToInt().coerceIn(0, 100)
}

fun LearnerProgress.questStats() = QuestStats(
    xp = dailyXp,
    lessons = dailyLessons,
    correct = dailyCorrect,
    answered = dailyAnswered,
    bestCombo = dailyBestCombo,
    perfectLessons = dailyPerfectLessons,
    practiceCorrect = dailyPracticeCorrect
)

class ProgressStore(context: Context) {
    private val prefs = context.getSharedPreferences("dalawingo_progress_v1", Context.MODE_PRIVATE)
    var state by mutableStateOf(loadAndRefresh())
        private set

    private fun today() = LocalDate.now().toString()

    private fun loadVocabulary(): Map<String, VocabularyProgress> = runCatching {
        val saved = JSONObject(prefs.getString("vocabulary_v1", "{}") ?: "{}")
        buildMap {
            val keys = saved.keys()
            while (keys.hasNext()) {
                val id = keys.next()
                val json = saved.getJSONObject(id)
                put(id, VocabularyProgress(
                    attempts = json.optInt("attempts"),
                    correct = json.optInt("correct"),
                    correctStreak = json.optInt("streak"),
                    intervalDays = json.optInt("interval"),
                    firstSeenMillis = json.optLong("introduced"),
                    lastReviewedDay = json.optLong("last", -1L),
                    nextReviewDay = json.optLong("due")
                ))
            }
        }
    }.getOrDefault(emptyMap())

    private fun saveVocabulary(vocabulary: Map<String, VocabularyProgress>): String {
        val data = JSONObject()
        vocabulary.forEach { (id, item) ->
            data.put(id, JSONObject().apply {
                put("attempts", item.attempts)
                put("correct", item.correct)
                put("streak", item.correctStreak)
                put("interval", item.intervalDays)
                put("introduced", item.firstSeenMillis)
                put("last", item.lastReviewedDay)
                put("due", item.nextReviewDay)
            })
        }
        return data.toString()
    }
    private fun readAchievementDates(): Map<String, String> = runCatching {
        val obj = JSONObject(prefs.getString("achievementUnlockedAt", "{}") ?: "{}")
        buildMap {
            val keys = obj.keys()
            while (keys.hasNext()) {
                val id = keys.next()
                put(id, obj.optString(id))
            }
        }
    }.getOrDefault(emptyMap())

    private fun saveAchievementDates(dates: Map<String, String>): String = JSONObject().apply {
        dates.forEach { (id, day) -> put(id, day) }
    }.toString()

    private fun readSet(key: String): Set<String> = prefs.getString(key, "").orEmpty()
        .split("|").filter { it.isNotEmpty() }.toSet()

    private fun readLessonRounds(): Map<String, Int> = runCatching {
        val json = JSONObject(prefs.getString("lessonRounds_v1", "{}") ?: "{}")
        buildMap {
            val keys = json.keys()
            while (keys.hasNext()) {
                val id = keys.next()
                put(id, json.optInt(id).coerceIn(0, ROUNDS_PER_LESSON))
            }
        }
    }.getOrDefault(emptyMap())

    private fun saveLessonRounds(rounds: Map<String, Int>): String = JSONObject().apply {
        rounds.forEach { (id, count) -> put(id, count.coerceIn(0, ROUNDS_PER_LESSON)) }
    }.toString()

    private fun readPerfectRoundMasks(): Map<String, Int> = runCatching {
        val json = JSONObject(prefs.getString("perfectRoundMasks", "{}") ?: "{}")
        buildMap {
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                put(key, json.optInt(key))
            }
        }
    }.getOrDefault(emptyMap())

    private fun loadAndRefresh(): LearnerProgress {
        var model = LearnerProgress(
            name = prefs.getString("name", "Learner") ?: "Learner",
            completed = readSet("completed"),
            lessonRounds = readLessonRounds(),
            completedActivities = readSet("completedActivities"),
            golden = readSet("golden"),
            rewardedUnits = readSet("rewardedUnits").mapNotNull { it.toIntOrNull() }.toSet(),
            xp = prefs.getInt("xp", 0),
            pesos = prefs.getInt("pesos", 0),
            streak = prefs.getInt("streak", 0),
            bestStreak = prefs.getInt("bestStreak", 0),
            lastActiveDate = prefs.getString("lastActiveDate", "") ?: "",
            dailyDate = prefs.getString("dailyDate", "") ?: "",
            dailyXp = prefs.getInt("dailyXp", 0),
            dailyLessons = prefs.getInt("dailyLessons", 0),
            dailyCorrect = prefs.getInt("dailyCorrect", 0),
            dailyAnswered = prefs.getInt("dailyAnswered", 0),
            dailyPerfectLessons = prefs.getInt("dailyPerfectLessons", 0),
            dailyBestCombo = prefs.getInt("dailyBestCombo", 0),
            dailyPracticeCorrect = prefs.getInt("dailyPracticeCorrect", 0),
            dailyQuestIds = prefs.getString("dailyQuestIds", "").orEmpty()
                .split("|").filter { it.isNotEmpty() },
            dailyQuestClaimed = readSet("dailyQuestClaimed"),
            dailyPractices = prefs.getInt("dailyPractices", 0),
            totalPractices = prefs.getInt("totalPractices", 0),
            vocabulary = loadVocabulary(),
            animalId = prefs.getString("animalId", "tarsier") ?: "tarsier",
            ownedCosmetics = readSet("ownedCosmetics") + "room_cozy",
            equippedHat = prefs.getString("equippedHat", null),
            equippedOutfit = prefs.getString("equippedOutfit", null),
            equippedAccessory = prefs.getString("equippedAccessory", null),
            equippedRoom = prefs.getString("equippedRoom", "room_cozy") ?: "room_cozy",
            achievementUnlockedAt = readAchievementDates(),
            claimedAchievements = readSet("claimedAchievements"),
            xpBoostTokens = prefs.getInt("xpBoostTokens", 0),
            xpBoostUntilMillis = prefs.getLong("xpBoostUntilMillis", 0L),
            unlockedAnimals = readSet("unlockedAnimals"),
            earlyDays = prefs.getInt("earlyDays", 0),
            nightDays = prefs.getInt("nightDays", 0),
            lastEarlyDay = prefs.getString("lastEarlyDay", "") ?: "",
            lastNightDay = prefs.getString("lastNightDay", "") ?: "",
            totalQuests = prefs.getInt("totalQuests", 0),
            fullQuestDays = prefs.getInt("fullQuestDays", 0),
            lastFullQuestDay = prefs.getString("lastFullQuestDay", "") ?: "",
            speakingCorrect = prefs.getInt("speakingCorrect", 0),
            listeningCorrect = prefs.getInt("listeningCorrect", 0),
            matchingCorrect = prefs.getInt("matchingCorrect", 0),
            sentencesCorrect = prefs.getInt("sentencesCorrect", 0),
            mistakesCorrected = prefs.getInt("mistakesCorrected", 0),
            dueRecallCorrect = prefs.getInt("dueRecallCorrect", 0),
            visitedRooms = readSet("visitedRooms") + "room_cozy" +
                (prefs.getString("equippedRoom", "room_cozy") ?: "room_cozy"),
            perfectRoundMasks = readPerfectRoundMasks()
        )
        model = readyForToday(model)
        val previous = runCatching { LocalDate.parse(model.lastActiveDate) }.getOrNull()
        if (previous != null && ChronoUnit.DAYS.between(previous, LocalDate.now()) > 1) {
            model = model.copy(streak = 0)
        }
        model = refreshAchievements(model, migrating = !prefs.getBoolean("achievementTrackingInitialized", false))
        persist(model)
        prefs.edit().putBoolean("achievementTrackingInitialized", true).apply()
        return model
    }

    private fun persist(next: LearnerProgress) {
        prefs.edit()
            .putString("name", next.name)
            .putString("completed", next.completed.sorted().joinToString("|"))
            .putString("lessonRounds_v1", saveLessonRounds(next.lessonRounds))
            .putString("completedActivities", next.completedActivities.sorted().joinToString("|"))
            .putString("golden", next.golden.sorted().joinToString("|"))
            .putString("rewardedUnits", next.rewardedUnits.sorted().joinToString("|"))
            .putInt("xp", next.xp)
            .putInt("pesos", next.pesos)
            .putInt("streak", next.streak)
            .putInt("bestStreak", next.bestStreak)
            .putString("lastActiveDate", next.lastActiveDate)
            .putString("dailyDate", next.dailyDate)
            .putInt("dailyXp", next.dailyXp)
            .putInt("dailyLessons", next.dailyLessons)
            .putInt("dailyCorrect", next.dailyCorrect)
            .putInt("dailyAnswered", next.dailyAnswered)
            .putInt("dailyPerfectLessons", next.dailyPerfectLessons)
            .putInt("dailyBestCombo", next.dailyBestCombo)
            .putInt("dailyPracticeCorrect", next.dailyPracticeCorrect)
            .putString("dailyQuestIds", next.dailyQuestIds.joinToString("|"))
            .putString("dailyQuestClaimed", next.dailyQuestClaimed.sorted().joinToString("|"))
            .putInt("dailyPractices", next.dailyPractices)
            .putInt("totalPractices", next.totalPractices)
            .putString("vocabulary_v1", saveVocabulary(next.vocabulary))
            .putString("animalId", next.animalId)
            .putString("ownedCosmetics", next.ownedCosmetics.sorted().joinToString("|"))
            .putString("equippedHat", next.equippedHat)
            .putString("equippedOutfit", next.equippedOutfit)
            .putString("equippedAccessory", next.equippedAccessory)
            .putString("equippedRoom", next.equippedRoom)
            .putString("achievementUnlockedAt", saveAchievementDates(next.achievementUnlockedAt))
            .putString("claimedAchievements", next.claimedAchievements.sorted().joinToString("|"))
            .putInt("xpBoostTokens", next.xpBoostTokens)
            .putLong("xpBoostUntilMillis", next.xpBoostUntilMillis)
            .putString("unlockedAnimals", next.unlockedAnimals.sorted().joinToString("|"))
            .putInt("earlyDays", next.earlyDays)
            .putInt("nightDays", next.nightDays)
            .putString("lastEarlyDay", next.lastEarlyDay)
            .putString("lastNightDay", next.lastNightDay)
            .putInt("totalQuests", next.totalQuests)
            .putInt("fullQuestDays", next.fullQuestDays)
            .putString("lastFullQuestDay", next.lastFullQuestDay)
            .putInt("speakingCorrect", next.speakingCorrect)
            .putInt("listeningCorrect", next.listeningCorrect)
            .putInt("matchingCorrect", next.matchingCorrect)
            .putInt("sentencesCorrect", next.sentencesCorrect)
            .putInt("mistakesCorrected", next.mistakesCorrected)
            .putInt("dueRecallCorrect", next.dueRecallCorrect)
            .putString("visitedRooms", next.visitedRooms.sorted().joinToString("|"))
            .putString("perfectRoundMasks", JSONObject().apply {
                next.perfectRoundMasks.forEach { (k,v) -> put(k,v) }
            }.toString())
            .apply()
    }

    /** Select today's quests just once; also handles the app staying open past midnight. */
    private fun readyForToday(old: LearnerProgress): LearnerProgress {
        val current = today()
        if (old.dailyDate != current) {
            return old.copy(
                dailyDate = current,
                dailyXp = 0,
                dailyLessons = 0,
                dailyCorrect = 0,
                dailyAnswered = 0,
                dailyPerfectLessons = 0,
                dailyBestCombo = 0,
                dailyPracticeCorrect = 0,
                dailyPractices = 0,
                dailyQuestIds = selectDailyQuests(LocalDate.now().toEpochDay()).map { it.id },
                dailyQuestClaimed = emptySet()
            )
        }
        // Migration from the original fixed daily quests.
        return if (old.dailyQuestIds.size != 3 ||
            old.dailyQuestIds.any { findDailyQuest(it) == null }) {
            old.copy(
                dailyQuestIds = selectDailyQuests(LocalDate.now().toEpochDay()).map { it.id },
                dailyQuestClaimed = emptySet()
            )
        } else old
    }

    /** Each tier pays only once on the date, even if the player keeps replaying. */
    private fun grantDailyQuestRewards(next: LearnerProgress): LearnerProgress {
        val newlyFinished = next.dailyQuestIds.mapNotNull(::findDailyQuest)
            .filter { it.id !in next.dailyQuestClaimed && it.isCompleted(next.questStats()) }
        val allClaimed = next.dailyQuestClaimed + newlyFinished.map { it.id }
        val achievedAll = next.dailyQuestIds.size == 3 &&
            next.dailyQuestIds.all { it in allClaimed } && next.lastFullQuestDay != today()
        return next.copy(
            pesos = next.pesos + newlyFinished.sumOf { it.tier.pesos },
            dailyQuestClaimed = allClaimed,
            totalQuests = next.totalQuests + newlyFinished.size,
            fullQuestDays = next.fullQuestDays + if (achievedAll) 1 else 0,
            lastFullQuestDay = if (achievedAll) today() else next.lastFullQuestDay
        )
    }

    /** Unlock milestones on completion, never from tapping an achievement card. */
    private fun refreshAchievements(old: LearnerProgress, migrating: Boolean = false): LearnerProgress {
        val dates = old.achievementUnlockedAt.toMutableMap()
        for (achievement in achievements) {
            if (achievement.id !in dates && achievement.currentValue(old) >= achievement.target) {
                // Historical progress doesn't contain the actual date it was earned.
                dates[achievement.id] = if (migrating) "earlier" else today()
            }
        }
        return old.copy(achievementUnlockedAt = dates)
    }

    private fun update(transform: (LearnerProgress) -> LearnerProgress) {
        state = refreshAchievements(transform(state))
        persist(state)
    }

    private fun recordStudy(old: LearnerProgress, attempts: List<AnswerAttempt>): LearnerProgress {
        val hour = java.time.LocalTime.now().hour
        val day = today()
        val corrected = attempts.count { attempt ->
            attempt.correct && (old.vocabulary[attempt.vocabularyId]?.let {
                it.attempts > it.correct
            } == true)
        }
        val due = attempts.count { attempt ->
            attempt.correct && old.vocabulary[attempt.vocabularyId]?.isDue(LocalDate.now().toEpochDay()) == true
        }
        return old.copy(
            earlyDays = old.earlyDays + if (hour < 9 && old.lastEarlyDay != day) 1 else 0,
            nightDays = old.nightDays + if (hour >= 22 && old.lastNightDay != day) 1 else 0,
            lastEarlyDay = if (hour < 9) day else old.lastEarlyDay,
            lastNightDay = if (hour >= 22) day else old.lastNightDay,
            speakingCorrect = old.speakingCorrect + attempts.count { it.correct && it.exerciseType in setOf(
                ExerciseType.SPEAK_TAGALOG, ExerciseType.SPEAK_SET, ExerciseType.SPEAK_FLASHCARDS) },
            listeningCorrect = old.listeningCorrect + attempts.count { it.correct && it.exerciseType in setOf(
                ExerciseType.LISTEN, ExerciseType.MATCH_AUDIO, ExerciseType.LISTEN_MISSING,
                ExerciseType.TYPE_HEARD) },
            matchingCorrect = old.matchingCorrect + attempts.count { it.correct && it.exerciseType in setOf(
                ExerciseType.MATCH_AUDIO, ExerciseType.MATCH_TEXT) },
            sentencesCorrect = old.sentencesCorrect + attempts.count { it.correct && it.exerciseType in setOf(
                ExerciseType.WORD_BANK, ExerciseType.COMPLETE_SENTENCE, ExerciseType.FILL_BLANK,
                ExerciseType.TYPE_TAGALOG, ExerciseType.COMPLETE_CHAT) },
            mistakesCorrected = old.mistakesCorrected + corrected,
            dueRecallCorrect = old.dueRecallCorrect + due
        )
    }

    fun rename(name: String) = update { it.copy(name = name.trim().take(24).ifEmpty { "Learner" }) }

    private fun updatedStreak(old: LearnerProgress): Pair<Int, Int> {
        val now = LocalDate.now()
        val previous = runCatching { LocalDate.parse(old.lastActiveDate) }.getOrNull()
        val streak = when {
            previous == now -> old.streak
            previous == now.minusDays(1) -> old.streak + 1
            else -> 1
        }
        return streak to max(old.bestStreak, streak)
    }

    fun finishLesson(
        lessonId: String,
        correct: Int,
        total: Int,
        answers: List<AnswerAttempt> = emptyList()
    ): Int {
        val baseXp = 10 + correct * 2 + (if (total > 0 && correct == total) 5 else 0)
        val xpEarned = baseXp * if (state.xpBoostUntilMillis > System.currentTimeMillis()) 2 else 1
        update { stored ->
            val old = readyForToday(stored)
            val previousRounds = old.roundsFor(lessonId)
            val newRounds = (previousRounds + 1).coerceAtMost(ROUNDS_PER_LESSON)
            val firstCompletion = previousRounds < ROUNDS_PER_LESSON && newRounds == ROUNDS_PER_LESSON
            // A node becomes Perfect only on its final (4/4) round or on a perfect
            // review of an already-completed node. Earlier perfect rounds still count
            // for daily quests, but no longer award a gold node prematurely.
            val firstPerfect = total > 0 && correct == total &&
                newRounds == ROUNDS_PER_LESSON && lessonId !in old.golden
            val nowCompleted = if (firstCompletion) old.completed + lessonId else old.completed
            val newUnits = course.filter { unit ->
                unit.number !in old.rewardedUnits &&
                    unit.lessons.all { it.id in nowCompleted }
            }.map { it.number }.toSet()
            val coinsEarned = (if (firstCompletion) 20 else 0) +
                (if (firstPerfect) 10 else 0) + newUnits.size * 50
            val (streak, best) = updatedStreak(old)
            val answerCount = if (answers.isNotEmpty()) answers.size else total
            val longestRun = if (answers.isNotEmpty()) longestCorrectRun(answers)
                else if (total > 0 && correct == total) total else 0
            val updated = old.copy(
                completed = nowCompleted,
                lessonRounds = old.lessonRounds + (lessonId to newRounds),
                // A perfect review also advances the four-perfect-round achievement.
                // Existing mastery and first-perfect Peso rewards are never reset.
                perfectRoundMasks = if (total > 0 && correct == total) {
                    val existingMask = old.perfectRoundMasks[lessonId] ?: 0
                    val slot = if (previousRounds < ROUNDS_PER_LESSON) previousRounds
                        else (0 until ROUNDS_PER_LESSON).firstOrNull {
                            existingMask and (1 shl it) == 0
                        } ?: 3
                    old.perfectRoundMasks + (lessonId to (existingMask or (1 shl slot)))
                } else old.perfectRoundMasks,
                golden = if (firstPerfect) old.golden + lessonId else old.golden,
                rewardedUnits = old.rewardedUnits + newUnits,
                xp = old.xp + xpEarned,
                pesos = old.pesos + coinsEarned,
                dailyXp = old.dailyXp + xpEarned,
                dailyLessons = old.dailyLessons + 1,
                dailyCorrect = old.dailyCorrect + correct,
                dailyAnswered = old.dailyAnswered + answerCount,
                dailyPerfectLessons = old.dailyPerfectLessons +
                    if (total > 0 && correct == total) 1 else 0,
                dailyBestCombo = max(old.dailyBestCombo, longestRun),
                streak = streak,
                bestStreak = best,
                lastActiveDate = today(),
                vocabulary = updateVocabulary(
                    old.vocabulary,
                    course.flatMap { it.lessons }
                        .firstOrNull { it.id == lessonId }?.phrases.orEmpty(),
                    answers,
                    LocalDate.now().toEpochDay(),
                    System.currentTimeMillis()
                )
            )
            grantDailyQuestRewards(recordStudy(updated, answers).copy(
                // Corrected / due history is evaluated from the OLD state, not newly recorded attempts.
                mistakesCorrected = old.mistakesCorrected + answers.count { attempt ->
                    attempt.correct && (old.vocabulary[attempt.vocabularyId]?.let {
                        it.attempts > it.correct
                    } == true)
                },
                dueRecallCorrect = old.dueRecallCorrect + answers.count { attempt ->
                    attempt.correct && old.vocabulary[attempt.vocabularyId]?.isDue(LocalDate.now().toEpochDay()) == true
                }
            ))
        }
        return xpEarned
    }

    fun finishPractice(
        correct: Int,
        total: Int,
        answers: List<AnswerAttempt> = emptyList()
    ): Int {
        val baseXp = 5 + correct * 2 + (if (total > 0 && correct == total) 5 else 0)
        val earnedXp = baseXp * if (state.xpBoostUntilMillis > System.currentTimeMillis()) 2 else 1
        update { stored ->
            val old = readyForToday(stored)
            val (streak, best) = updatedStreak(old)
            val firstPracticeToday = old.dailyPractices == 0
            val answerCount = if (answers.isNotEmpty()) answers.size else total
            val longestRun = if (answers.isNotEmpty()) longestCorrectRun(answers)
                else if (total > 0 && correct == total) total else 0
            val updated = old.copy(
                xp = old.xp + earnedXp,
                pesos = old.pesos + if (firstPracticeToday) 25 else 0,
                dailyXp = old.dailyXp + earnedXp,
                dailyCorrect = old.dailyCorrect + correct,
                dailyAnswered = old.dailyAnswered + answerCount,
                dailyBestCombo = max(old.dailyBestCombo, longestRun),
                dailyPracticeCorrect = old.dailyPracticeCorrect + correct,
                dailyPractices = old.dailyPractices + 1,
                totalPractices = old.totalPractices + 1,
                streak = streak,
                bestStreak = best,
                lastActiveDate = today(),
                vocabulary = updateVocabulary(
                    old.vocabulary,
                    emptyList(),
                    answers,
                    LocalDate.now().toEpochDay(),
                    System.currentTimeMillis()
                )
            )
            grantDailyQuestRewards(recordStudy(updated, answers).copy(
                // Corrected / due history is evaluated from the OLD state, not newly recorded attempts.
                mistakesCorrected = old.mistakesCorrected + answers.count { attempt ->
                    attempt.correct && (old.vocabulary[attempt.vocabularyId]?.let {
                        it.attempts > it.correct
                    } == true)
                },
                dueRecallCorrect = old.dueRecallCorrect + answers.count { attempt ->
                    attempt.correct && old.vocabulary[attempt.vocabularyId]?.isDue(LocalDate.now().toEpochDay()) == true
                }
            ))
        }
        return earnedXp
    }

    /** Bonus path activities are repeatable and never pay the Daily Practice coin bonus. */
    fun finishPathActivity(
        activityId: String,
        correct: Int,
        total: Int,
        answers: List<AnswerAttempt> = emptyList()
    ): Int {
        val baseXp = 5 + correct * 2 + (if (total > 0 && correct == total) 5 else 0)
        val earnedXp = baseXp * if (state.xpBoostUntilMillis > System.currentTimeMillis()) 2 else 1
        update { stored ->
            val old = readyForToday(stored)
            val (streak, best) = updatedStreak(old)
            val longestRun = if (answers.isNotEmpty()) longestCorrectRun(answers)
                else if (total > 0 && correct == total) total else 0
            val updated = old.copy(
                completedActivities = old.completedActivities + activityId,
                xp = old.xp + earnedXp,
                dailyXp = old.dailyXp + earnedXp,
                dailyCorrect = old.dailyCorrect + correct,
                dailyAnswered = old.dailyAnswered + if (answers.isNotEmpty()) answers.size else total,
                dailyBestCombo = max(old.dailyBestCombo, longestRun),
                streak = streak,
                bestStreak = best,
                lastActiveDate = today(),
                vocabulary = updateVocabulary(
                    old.vocabulary, emptyList(), answers,
                    LocalDate.now().toEpochDay(), System.currentTimeMillis()
                )
            )
            grantDailyQuestRewards(recordStudy(updated, answers).copy(
                // Corrected / due history is evaluated from the OLD state, not newly recorded attempts.
                mistakesCorrected = old.mistakesCorrected + answers.count { attempt ->
                    attempt.correct && (old.vocabulary[attempt.vocabularyId]?.let {
                        it.attempts > it.correct
                    } == true)
                },
                dueRecallCorrect = old.dueRecallCorrect + answers.count { attempt ->
                    attempt.correct && old.vocabulary[attempt.vocabularyId]?.isDue(LocalDate.now().toEpochDay()) == true
                }
            ))
        }
        return earnedXp
    }

    fun chooseAnimal(id: String) {
        if (pets.none { it.id == id }) return
        if (id == "deer" && id !in state.unlockedAnimals) return
        update { it.copy(animalId = id) }
    }

    fun buyCosmetic(id: String): Boolean {
        val item = cosmetics.firstOrNull { it.id == id } ?: return false
        val old = state
        if (item.achievementOnly || id in old.ownedCosmetics || old.pesos < item.price ||
            old.xp < item.xpNeeded || old.golden.size < item.goldenNeeded) return false
        update { it.copy(
            pesos = it.pesos - item.price,
            ownedCosmetics = it.ownedCosmetics + id
        ) }
        return true
    }

    fun equipCosmetic(id: String) {
        val item = cosmetics.firstOrNull { it.id == id } ?: return
        if (id !in state.ownedCosmetics) return
        update { old -> when (item.slot) {
            CosmeticSlot.HAT -> old.copy(equippedHat = id)
            CosmeticSlot.OUTFIT -> old.copy(equippedOutfit = id)
            CosmeticSlot.ACCESSORY -> old.copy(equippedAccessory = id)
            CosmeticSlot.ROOM -> old.copy(equippedRoom = id, visitedRooms = old.visitedRooms + id)
        } }
    }

    fun unequipCosmetic(slot: CosmeticSlot) = update { old -> when (slot) {
        CosmeticSlot.HAT -> old.copy(equippedHat = null)
        CosmeticSlot.OUTFIT -> old.copy(equippedOutfit = null)
        CosmeticSlot.ACCESSORY -> old.copy(equippedAccessory = null)
        CosmeticSlot.ROOM -> old.copy(equippedRoom = "room_cozy")
    } }

    /** Claims can happen only once; all rewards save with the same progress state. */
    fun claimAchievement(id: String): Boolean {
        val item = achievements.firstOrNull { it.id == id } ?: return false
        if (id !in state.achievementUnlockedAt || id in state.claimedAchievements) return false
        update { old ->
            var next = old.copy(claimedAchievements = old.claimedAchievements + id)
            next = when (item.rewardType) {
                AchievementReward.PESOS -> next.copy(pesos = next.pesos + item.rewardAmount)
                AchievementReward.XP -> next.copy(xp = next.xp + item.rewardAmount)
                AchievementReward.XP_BOOST -> next.copy(xpBoostTokens = next.xpBoostTokens + item.rewardAmount)
                AchievementReward.COSMETIC -> next.copy(ownedCosmetics = next.ownedCosmetics + listOfNotNull(item.rewardId))
                AchievementReward.PET -> next.copy(unlockedAnimals = next.unlockedAnimals + listOfNotNull(item.rewardId))
            }
            next
        }
        return true
    }

    /** Timer starts only when activated, never on claiming the achievement. */
    fun activateXpBoost(): Boolean {
        if (state.xpBoostTokens <= 0 || state.xpBoostUntilMillis > System.currentTimeMillis()) return false
        val until = System.currentTimeMillis() + 15 * 60 * 1000L
        update { it.copy(xpBoostTokens = it.xpBoostTokens - 1, xpBoostUntilMillis = until) }
        return true
    }

}
