package com.dalawingo.app.model

/** Milestones stay separate from the three daily quests. */
enum class AchievementMetric { LESSONS, GOLDEN, STREAK, XP, PRACTICES, MASTERED }
enum class AchievementReward { PESOS, XP, XP_BOOST, COSMETIC, PET }

data class AchievementDefinition(
    val id: String,
    val title: String,
    val emoji: String,
    val description: String,
    val metric: AchievementMetric,
    val target: Int,
    val rewardType: AchievementReward,
    val rewardAmount: Int = 0,
    val rewardId: String? = null
) {
    val rewardLabel: String get() = when (rewardType) {
        AchievementReward.PESOS -> "🪙 $rewardAmount Pesos"
        AchievementReward.XP -> "⚡ $rewardAmount XP"
        AchievementReward.XP_BOOST -> "⚡ 2× XP boost · 15 minutes"
        AchievementReward.COSMETIC -> "🎀 Exclusive cosmetic"
        AchievementReward.PET -> "🦌 Exclusive Philippine Deer"
    }
}

val achievements = listOf(
    AchievementDefinition("first_steps", "First Steps", "🌱", "Complete your first Tagalog lesson.", AchievementMetric.LESSONS, 1, AchievementReward.PESOS, 50),
    AchievementDefinition("golden_lesson", "Golden Lesson", "⭐", "Earn your first perfect-score golden lesson.", AchievementMetric.GOLDEN, 1, AchievementReward.PESOS, 30),
    AchievementDefinition("xp_collector", "XP Collector", "⚡", "Earn 100 total XP.", AchievementMetric.XP, 100, AchievementReward.XP, 100),
    AchievementDefinition("on_fire", "On Fire", "🔥", "Reach a 7-day learning streak.", AchievementMetric.STREAK, 7, AchievementReward.XP_BOOST, 1),
    AchievementDefinition("perfectionist", "Perfectionist", "👑", "Earn 5 golden lessons.", AchievementMetric.GOLDEN, 5, AchievementReward.COSMETIC, rewardId = "starlight_bow"),
    AchievementDefinition("practice_hero", "Practice Hero", "🧠", "Finish 10 practice rounds.", AchievementMetric.PRACTICES, 10, AchievementReward.XP_BOOST, 1),
    AchievementDefinition("word_wizard", "Word Wizard", "📚", "Master 20 Tagalog phrases.", AchievementMetric.MASTERED, 20, AchievementReward.XP, 100),
    AchievementDefinition("animal_lover", "Animal Lover", "🐾", "Complete 12 different lessons.", AchievementMetric.LESSONS, 12, AchievementReward.PET, rewardId = "deer"),
    AchievementDefinition("tagalog_champion", "Tagalog Champion", "🏆", "Complete all ${allLessons.size} lessons.", AchievementMetric.LESSONS, allLessons.size, AchievementReward.COSMETIC, rewardId = "room_stars")
)
