package com.dalawingo.app.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dalawingo.app.R

@Composable
fun DalaIcon(@DrawableRes resource: Int, description: String?, size: Dp = 24.dp,
             tint: Color? = null, modifier: Modifier = Modifier) {
    if (tint == null) Image(painterResource(resource), description, modifier.size(size))
    else Icon(painterResource(resource), description, modifier.size(size), tint = tint)
}

fun badgeFor(id: String): Int? = when (id) {
    "first_steps" -> R.drawable.badge_first_steps
    "golden_lesson", "perfect_lessons" -> R.drawable.badge_perfect_lessons
    "xp_collector" -> R.drawable.badge_xp_collector
    "on_fire" -> R.drawable.badge_on_fire
    "early_bird" -> R.drawable.badge_early_riser
    "night_owl" -> R.drawable.badge_sleepwalker
    "word_wizard", "word_collector" -> R.drawable.badge_word_collector
    "speed_racer" -> R.drawable.badge_speed_racer
    "flawless_finisher", "perfectionist" -> R.drawable.badge_flawless_finisher
    "tagalog_champion", "course_conqueror" -> R.drawable.badge_tagalog_champion
    "practice_hero" -> R.drawable.badge_practice_hero
    "animal_lover" -> R.drawable.badge_animal_lover
    "quest_hunter" -> R.drawable.badge_quest_hunter
    "full_house" -> R.drawable.badge_full_house
    "daldal" -> R.drawable.badge_daldal
    "all_ears" -> R.drawable.badge_all_ears
    "matchmaker" -> R.drawable.badge_matchmaker
    "sentence_architect" -> R.drawable.badge_sentence_architect
    "storyteller" -> R.drawable.badge_storyteller
    "comeback_kid" -> R.drawable.badge_comeback_kid
    "long_term_memory" -> R.drawable.badge_long_term_memory
    "fashionista" -> R.drawable.badge_fashionista
    "home_sweet_home" -> R.drawable.badge_home_sweet_home
    "island_explorer" -> R.drawable.badge_island_explorer
    "bida_bida" -> R.drawable.badge_bida_bida
    else -> null
}
fun iconForAchievement(id: String): Int = when (id) {
    "practice_hero", "quest_hunter", "full_house" -> R.drawable.ic_chest
    "animal_lover", "fashionista", "home_sweet_home" -> R.drawable.ic_paw
    "daldal", "storyteller" -> R.drawable.ic_microphone
    "all_ears" -> R.drawable.ic_headphone
    "matchmaker", "sentence_architect", "comeback_kid" -> R.drawable.ic_check
    "long_term_memory", "island_explorer" -> R.drawable.ic_book
    "bida_bida" -> R.drawable.ic_star
    else -> R.drawable.ic_star
}
