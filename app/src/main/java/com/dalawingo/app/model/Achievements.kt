package com.dalawingo.app.model

/** Milestones stay separate from the three daily quests. */
enum class AchievementMetric {
    LESSONS, GOLDEN, STREAK, XP, PRACTICES, MASTERED,
    EARLY_DAYS, NIGHT_DAYS, QUESTS, FULL_QUEST_DAYS,
    SPEAKING, LISTENING, MATCHING, SENTENCES, STORIES,
    CORRECTIONS, DUE_RECALL, COSMETICS, ROOMS, BONUS, PERFECT_NODES,
    UNITS, COURSE
}
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
        AchievementReward.PESOS -> "$rewardAmount Pesos"
        AchievementReward.XP -> "$rewardAmount XP"
        AchievementReward.XP_BOOST -> "2× XP boost · 15 minutes"
        AchievementReward.COSMETIC -> "Exclusive cosmetic"
        AchievementReward.PET -> "Exclusive Philippine Deer"
    }
}

/** Achievements are solo-friendly; none require a league, friends, or subscriptions. */
val achievements = listOf(
    AchievementDefinition("first_steps", "First Steps", "", "Complete your first lesson node.", AchievementMetric.LESSONS, 1, AchievementReward.PESOS, 50),
    AchievementDefinition("golden_lesson", "Perfect Lessons", "", "Earn your first perfect lesson.", AchievementMetric.GOLDEN, 1, AchievementReward.PESOS, 30),
    AchievementDefinition("xp_collector", "XP Collector", "", "Earn 100 XP.", AchievementMetric.XP, 100, AchievementReward.XP, 100),
    AchievementDefinition("on_fire", "On Fire", "", "Maintain a 7-day streak.", AchievementMetric.STREAK, 7, AchievementReward.XP_BOOST, 1),
    AchievementDefinition("perfectionist", "Perfectionist", "", "Earn 5 perfect lessons.", AchievementMetric.GOLDEN, 5, AchievementReward.COSMETIC, rewardId = "starlight_bow"),
    AchievementDefinition("practice_hero", "Practice Hero", "", "Complete 10 practice rounds.", AchievementMetric.PRACTICES, 10, AchievementReward.XP_BOOST, 1),
    AchievementDefinition("word_wizard", "Word Wizard", "", "Master 20 Tagalog phrases.", AchievementMetric.MASTERED, 20, AchievementReward.XP, 100),
    AchievementDefinition("animal_lover", "Animal Lover", "", "Complete 12 lessons to meet the Philippine Deer.", AchievementMetric.LESSONS, 12, AchievementReward.PET, rewardId = "deer"),
    AchievementDefinition("tagalog_champion", "Tagalog Champion", "", "Finish a complete Tagalog unit.", AchievementMetric.UNITS, 1, AchievementReward.COSMETIC, rewardId = "room_stars"),
    AchievementDefinition("early_bird", "Early Bird", "", "Study before 9 AM on 5 days.", AchievementMetric.EARLY_DAYS, 5, AchievementReward.PESOS, 25),
    AchievementDefinition("night_owl", "Night Owl", "", "Study after 10 PM on 5 days.", AchievementMetric.NIGHT_DAYS, 5, AchievementReward.PESOS, 25),
    AchievementDefinition("quest_hunter", "Quest Hunter", "", "Complete 25 daily quests.", AchievementMetric.QUESTS, 25, AchievementReward.PESOS, 75),
    AchievementDefinition("full_house", "Full House", "", "Finish all three quests in one day.", AchievementMetric.FULL_QUEST_DAYS, 1, AchievementReward.PESOS, 30),
    AchievementDefinition("daldal", "Daldal!", "", "Speak 10 Tagalog phrases correctly.", AchievementMetric.SPEAKING, 10, AchievementReward.PESOS, 35),
    AchievementDefinition("all_ears", "All Ears", "", "Answer 20 listening prompts correctly.", AchievementMetric.LISTENING, 20, AchievementReward.PESOS, 35),
    AchievementDefinition("matchmaker", "Matchmaker", "", "Correctly match 20 vocabulary pairs.", AchievementMetric.MATCHING, 20, AchievementReward.PESOS, 35),
    AchievementDefinition("sentence_architect", "Sentence Architect", "", "Build 20 correct Tagalog sentences.", AchievementMetric.SENTENCES, 20, AchievementReward.PESOS, 35),
    AchievementDefinition("storyteller", "Storyteller", "", "Complete 3 mini-stories.", AchievementMetric.STORIES, 3, AchievementReward.PESOS, 35),
    AchievementDefinition("comeback_kid", "Comeback Kid", "", "Correct 5 phrases you previously missed.", AchievementMetric.CORRECTIONS, 5, AchievementReward.PESOS, 50),
    AchievementDefinition("long_term_memory", "Long-Term Memory", "", "Recall 10 phrases when they are due for review.", AchievementMetric.DUE_RECALL, 10, AchievementReward.PESOS, 50),
    AchievementDefinition("fashionista", "Fashionista", "", "Collect 5 cosmetic items.", AchievementMetric.COSMETICS, 5, AchievementReward.PESOS, 50),
    AchievementDefinition("home_sweet_home", "Home Sweet Home", "", "Use 3 different room backgrounds.", AchievementMetric.ROOMS, 3, AchievementReward.PESOS, 50),
    AchievementDefinition("island_explorer", "Island Explorer", "", "Complete 3 optional path activities.", AchievementMetric.BONUS, 3, AchievementReward.PESOS, 50),
    AchievementDefinition("bida_bida", "Bida-Bida!", "", "Earn four perfect round scores in the same lesson, including reviews.", AchievementMetric.PERFECT_NODES, 1, AchievementReward.PESOS, 75),
    AchievementDefinition("course_conqueror", "Course Conqueror", "", "Finish every released lesson at least once.", AchievementMetric.COURSE, allLessons.size, AchievementReward.PESOS, 100)
)
