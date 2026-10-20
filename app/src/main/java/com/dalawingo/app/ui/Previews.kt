
package com.dalawingo.app.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.model.course
import com.dalawingo.app.model.lessonExercises
import java.time.LocalDate

// Fake player data used only for previews.
private fun sampleProgress() = LearnerProgress(
    name = "Learner",
    streak = 7,
    bestStreak = 12,
    xp = 1250,
    pesos = 250,
    completed = setOf(course.first().lessons.first().id),
    lessonRounds = mapOf(
        course.first().lessons.first().id to 4,
        course.first().lessons[1].id to 2
    ),
    golden = setOf(course.first().lessons.first().id),
    dailyDate = LocalDate.now().toString(),
    dailyXp = 20,
    dailyLessons = 1,
    dailyCorrect = 8
)

// LEARN TAB
@Preview(
    name = "Learn",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun LearnPreview() {
    DalawingoTheme {
        PathScreen(
            progress = sampleProgress(),
            listState = rememberLazyListState(),
            onLesson = {},
            onActivity = {},
            onUnit = {},
            onGuide = {},
            onLocked = {},
            onPesos = {}
        )
    }
}

// PROGRESS TAB
@Preview(
    name = "Progress",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun ProgressPreview() {
    DalawingoTheme {
        ProfileScreen(
            progress = sampleProgress(),
            onClaimAchievement = { false },
            onActivateBoost = { false }
        )
    }
}

// UNITS SCREEN
@Preview(
    name = "Units",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun UnitsPreview() {
    DalawingoTheme {
        UnitsScreen(
            progress = sampleProgress(),
            selectedUnit = course.first(),
            onClose = {},
            onLesson = {},
            onGuide = {},
            onLocked = {}
        )
    }
}

// SHOP
@Preview(
    name = "Shop",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun ShopPreview() {
    DalawingoTheme {
        CosmeticShopScreen(
            progress = sampleProgress(),
            onClose = {},
            onBuy = { false },
            onEquip = {},
            onWardrobe = {}
        )
    }
}

// PET CUSTOMIZATION
@Preview(
    name = "Wardrobe",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun WardrobePreview() {
    DalawingoTheme {
        PetWardrobeScreen(
            progress = sampleProgress(),
            onClose = {},
            onChoosePet = {},
            onEquip = {},
            onUnequip = {},
            onShop = {}
        )
    }
}

// LESSON
@Preview(
    name = "Lesson",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun LessonPreview() {
    val lesson = course.first().lessons.first()

    DalawingoTheme {
        LessonScreen(
            title = lesson.title,
            exercises = lessonExercises(lesson, round = 1),
            practice = false,
            roundNumber = 1,
            onExit = {},
            onFinished = { _, _, _ -> }
        )
    }
}

// LESSON COMPLETION
@Preview(
    name = "Lesson Results",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun LessonResultsPreview() {
    DalawingoTheme {
        LessonResultScreen(
            title = "Greetings",
            correct = 6,
            total = 6,
            xp = 27,
            practice = false,
            pesosEarned = 30,
            roundCompleted = 4,
            onContinue = {},
            onReview = {}
        )
    }
}

// PESOS WALLET
@Preview(
    name = "Pesos Wallet",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)
@Composable
fun WalletPreview() {
    DalawingoTheme {
        PesosWalletDialog(
            pesos = 250,
            onClose = {},
            onShop = {}
        )
    }
}
@Preview(
    name = "Player",
    showBackground = true,
    widthDp = 393,
    heightDp = 852
)

@Composable
fun PlayerPreview() {
    DalawingoTheme {
        HomeScreen(
            progress = sampleProgress(),
            onContinue = {},
            onPractice = {},
            onLearn = {},
            onPesos = {},
            onCustomize = {}
        )
    }
}