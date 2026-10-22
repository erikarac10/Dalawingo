package com.dalawingo.app

import android.os.Bundle
import android.os.SystemClock
import android.app.Activity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import com.dalawingo.app.model.AnswerAttempt
import com.dalawingo.app.model.CosmeticSlot
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalawingo.app.data.ProgressStore
import com.dalawingo.app.data.ROUNDS_PER_LESSON
import com.dalawingo.app.data.roundsFor
import com.dalawingo.app.model.CourseUnit
import com.dalawingo.app.model.Exercise
import com.dalawingo.app.model.Lesson
import com.dalawingo.app.model.PathActivity
import com.dalawingo.app.model.DailyQuest
import com.dalawingo.app.model.findDailyQuest
import com.dalawingo.app.model.pathActivityExercises
import com.dalawingo.app.model.lessonExercises
import com.dalawingo.app.model.nextLesson
import com.dalawingo.app.model.course
import com.dalawingo.app.model.practiceExercises
import com.dalawingo.app.ui.*
import com.dalawingo.app.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        window.statusBarColor = android.graphics.Color.WHITE
        @Suppress("DEPRECATION")
        window.navigationBarColor = android.graphics.Color.WHITE
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
            android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        setContent { DalawingoTheme { DalawingoApp() } }
    }
}

private enum class Tab(val label: String, val glyph: GameGlyph) {
    PLAYER("Player", GameGlyph.HOME),
    LESSONS("Learn", GameGlyph.LEARN),
    PROGRESS("Progress", GameGlyph.TROPHY)
}

private data class Run(
    val key: Long,
    val name: String,
    val lesson: Lesson?,
    val exercises: List<Exercise>,
    val practice: Boolean,
    val activity: PathActivity? = null,
    val previousRounds: Int = 0,
    val startedAtMillis: Long = SystemClock.elapsedRealtime()
)

private data class Finish(
    val name: String,
    val correct: Int,
    val total: Int,
    val xp: Int,
    val practice: Boolean,
    val pesosEarned: Int,
    val lesson: Lesson?,
    val roundCompleted: Int,
    val activity: PathActivity?,
    val earnedQuests: List<DailyQuest>,
    val streakMilestone: Int,
    val previousRounds: Int,
    val secondsElapsed: Int,
    val wasPerfectBefore: Boolean
)

@Composable
private fun DalawingoApp() {
    val context = LocalContext.current
    val store = remember { ProgressStore(context.applicationContext) }
    val progress = store.state

    var tab by remember { mutableStateOf(Tab.PLAYER) }
    var currentRun by remember { mutableStateOf<Run?>(null) }
    var finished by remember { mutableStateOf<Finish?>(null) }
    var guidebook by remember { mutableStateOf<CourseUnit?>(null) }
    var selectedUnit by remember { mutableStateOf<CourseUnit?>(null) }
    var shopOpen by remember { mutableStateOf(false) }
    var walletOpen by remember { mutableStateOf(false) }
    var wardrobeOpen by remember { mutableStateOf(false) }
    var celebratePractice by remember { mutableStateOf(false) }
    var pathReveal by remember { mutableStateOf<PathReveal?>(null) }
    val pathScrollState = rememberLazyListState()
    val navigationScope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity
    SideEffect {
        // Match the Android status bar to the full-bleed pet banner on Player.
        val showingPlayer = tab == Tab.PLAYER && currentRun == null && finished == null &&
            selectedUnit == null && !shopOpen && !wardrobeOpen
        @Suppress("DEPRECATION")
        activity?.window?.statusBarColor = (if (showingPlayer)
            petRoomColor(progress.equippedRoom) else Color.White).toArgb()
    }

    fun startLesson(lesson: Lesson) {
        selectedUnit = null
        currentRun = Run(
            key = System.nanoTime(),
            name = lesson.title,
            lesson = lesson,
            exercises = lessonExercises(lesson, store.state.roundsFor(lesson.id).coerceAtMost(3) + 1),
            practice = false,
            previousRounds = store.state.roundsFor(lesson.id)
        )
    }

    fun startPractice() {
        currentRun = Run(
            key = System.nanoTime(),
            name = "Daily practice",
            lesson = null,
            exercises = practiceExercises(store.state.completed, store.state.vocabulary),
            practice = true
        )
    }

    fun startPathActivity(activity: PathActivity) {
        selectedUnit = null
        currentRun = Run(
            key = System.nanoTime(),
            name = activity.title,
            lesson = null,
            exercises = pathActivityExercises(activity, store.state.completed),
            practice = false,
            activity = activity
        )
    }

    fun showLocked() {
        Toast.makeText(
            context,
            "Finish the previous lessons to unlock this!",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun completeCurrent(correct: Int, total: Int, answers: List<AnswerAttempt>) {
        val running = currentRun ?: return
        val before = store.state
        val gained = when {
            running.activity != null -> store.finishPathActivity(
                running.activity.id, correct, total, answers)
            running.practice -> store.finishPractice(correct, total, answers)
            else -> store.finishLesson(running.lesson!!.id, correct, total, answers)
        }
        val earnedPesos = store.state.pesos - before.pesos
        val newlyClaimed = (store.state.dailyQuestClaimed - before.dailyQuestClaimed)
            .mapNotNull(::findDailyQuest)
        val streakNow = store.state.streak
        val milestone = if (streakNow != before.streak &&
            streakNow in setOf(3, 7, 14, 30, 100)) streakNow else 0
        if (running.practice && earnedPesos > 0) celebratePractice = true
        finished = Finish(
            running.name, correct, total, gained, running.practice, earnedPesos,
            running.lesson,
            running.lesson?.let { store.state.roundsFor(it.id) } ?: 0,
            running.activity, newlyClaimed, milestone, running.previousRounds,
            ((SystemClock.elapsedRealtime() - running.startedAtMillis) / 1000L).toInt().coerceAtLeast(0),
            running.lesson?.id in before.golden
        )
        currentRun = null
    }

    fun returnToLearnPath(result: Finish) {
        // A score is saved as soon as the exercise finishes, not when Continue is tapped.
        // Only a genuinely new round gets a progress/unlock reveal.
        pathReveal = result.lesson?.let { lesson ->
            val advanced = result.roundCompleted > result.previousRounds
            val firstPerfect = !result.wasPerfectBefore && lesson.id in store.state.golden
            if (!advanced && !firstPerfect) return@let null
            val nextUnlocked = if (advanced && result.previousRounds < ROUNDS_PER_LESSON &&
                result.roundCompleted == ROUNDS_PER_LESSON)
                nextLesson(store.state.completed)?.id else null
            val unitFinished = result.roundCompleted == ROUNDS_PER_LESSON &&
                course.any { unit ->
                    unit.lessons.lastOrNull()?.id == lesson.id &&
                        unit.lessons.all { it.id in store.state.completed }
                }
            PathReveal(
                token = System.nanoTime(),
                lessonId = lesson.id,
                fromRounds = result.previousRounds,
                toRounds = result.roundCompleted,
                newlyUnlockedLessonId = nextUnlocked,
                unitCompleted = unitFinished,
                justPerfected = (firstPerfect && result.roundCompleted == ROUNDS_PER_LESSON) ||
                    (advanced && result.previousRounds < ROUNDS_PER_LESSON &&
                        result.roundCompleted == ROUNDS_PER_LESSON &&
                        lesson.id in store.state.golden)
            )
        }
        finished = null
        tab = Tab.LESSONS
    }

    BackHandler(
        currentRun != null || finished != null || guidebook != null ||
            selectedUnit != null || shopOpen || wardrobeOpen || walletOpen
    ) {
        when {
            guidebook != null -> guidebook = null
            currentRun != null -> currentRun = null
            finished != null -> returnToLearnPath(finished!!)
            walletOpen -> walletOpen = false
            shopOpen -> shopOpen = false
            wardrobeOpen -> wardrobeOpen = false
            selectedUnit != null -> selectedUnit = null
        }
    }

    Box(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        when {
            currentRun != null -> {
                val running = currentRun!!
                key(running.key) {
                    LessonScreen(
                        title = running.name,
                        exercises = running.exercises,
                        practice = running.practice,
                        roundNumber = running.lesson?.let {
                            (store.state.roundsFor(it.id) + 1).coerceAtMost(4)
                        } ?: 1,
                        onExit = { currentRun = null },
                        onFinished = ::completeCurrent
                    )
                }
            }

            finished != null -> {
                val result = finished!!
                LessonResultScreen(
                    title = result.name,
                    correct = result.correct,
                    total = result.total,
                    xp = result.xp,
                    practice = result.practice,
                    pesosEarned = result.pesosEarned,
                    roundCompleted = result.roundCompleted,
                    pathActivity = result.activity != null,
                    earnedQuests = result.earnedQuests,
                    streakMilestone = result.streakMilestone,
                    secondsElapsed = result.secondsElapsed,
                    onContinue = { returnToLearnPath(result) },
                    onReview = { returnToLearnPath(result) }
                )
            }

            guidebook != null -> GuidebookScreen(
                unit = guidebook!!,
                onClose = { guidebook = null }
            )

            shopOpen -> CosmeticShopScreen(
                progress = progress,
                onClose = { shopOpen = false },
                onBuy = store::buyCosmetic,
                onEquip = store::equipCosmetic,
                onWardrobe = { shopOpen = false; wardrobeOpen = true }
            )

            wardrobeOpen -> PetWardrobeScreen(
                progress = progress,
                onClose = { wardrobeOpen = false },
                onChoosePet = store::chooseAnimal,
                onEquip = store::equipCosmetic,
                onUnequip = store::unequipCosmetic,
                onShop = { wardrobeOpen = false; shopOpen = true }
            )

            selectedUnit != null -> UnitsScreen(
                progress = progress,
                selectedUnit = selectedUnit!!,
                onClose = { selectedUnit = null },
                onLesson = { lesson ->
                    tab = Tab.LESSONS
                    startLesson(lesson)
                },
                onGuide = { guidebook = it },
                onLocked = ::showLocked,
                onSelectUnit = { unit ->
                    val position = course.takeWhile { it.number != unit.number }
                        .sumOf { 1 + it.lessons.size + 1 }
                    selectedUnit = null
                    tab = Tab.LESSONS
                    navigationScope.launch { pathScrollState.animateScrollToItem(position) }
                }
            )

            else -> Column {
                Box(Modifier.weight(1f)) {
                    when (tab) {
                        Tab.PLAYER -> HomeScreen(
                            progress = progress,
                            onContinue = {
                                nextLesson(progress.completed)?.let { startLesson(it) }
                                    ?: run { tab = Tab.LESSONS }
                            },
                            onPractice = ::startPractice,
                            onLearn = { tab = Tab.LESSONS },
                            onPesos = { walletOpen = true },
                            onCustomize = { wardrobeOpen = true },
                            celebratePractice = celebratePractice,
                            onCelebrationShown = { celebratePractice = false }
                        )

                        Tab.LESSONS -> PathScreen(
                            progress = progress,
                            listState = pathScrollState,
                            onLesson = ::startLesson,
                            onActivity = ::startPathActivity,
                            onUnit = { selectedUnit = it },
                            onGuide = { guidebook = it },
                            onLocked = ::showLocked,
                            onPesos = { walletOpen = true },
                            reveal = pathReveal,
                            onRevealFinished = { pathReveal = null }
                        )

                        Tab.PROGRESS -> ProfileScreen(
                            progress = progress,
                            onClaimAchievement = store::claimAchievement,
                            onActivateBoost = store::activateXpBoost
                        )
                    }
                }
                BottomTabs(tab) { tab = it }
            }
        }

        if (walletOpen) {
            PesosWalletDialog(
                pesos = progress.pesos,
                onClose = { walletOpen = false },
                onShop = { walletOpen = false; shopOpen = true }
            )
        }
    }
}

@Composable
private fun BottomTabs(selected: Tab, onSelect: (Tab) -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Color.White)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Tab.entries.forEach { item ->
            val chosen = item == selected
            val color = if (chosen) LeafDark else Color(0xFFACACAC)
            Box(
                Modifier.weight(1f).height(49.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (chosen) Leaf.copy(alpha = .12f) else Color.Transparent)
                    .then(if (chosen) Modifier.border(2.dp, Leaf.copy(alpha = .28f), RoundedCornerShape(15.dp)) else Modifier)
                    .semantics { contentDescription = item.label; role = Role.Tab }
                    .clickable { onSelect(item) },
                contentAlignment = Alignment.Center
            ) {
                DalaIcon(when (item) {
                    Tab.PLAYER -> R.drawable.ic_home
                    Tab.LESSONS -> R.drawable.ic_book
                    Tab.PROGRESS -> R.drawable.ic_trophy
                }, item.label, 29.dp, color)
            }
        }
    }
}
