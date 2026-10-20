package com.dalawingo.app.model

/** Optional activities on the learning path. They never block regular lessons. */
enum class PathActivityType { PICTURE, LISTENING, REVIEW, STORY }

data class PathActivity(
    val id: String,
    val unitNumber: Int,
    val type: PathActivityType,
    val title: String,
    val emoji: String
)

fun interludeFor(unit: CourseUnit): PathActivity {
    val type = when (unit.number) {
        2, 3 -> PathActivityType.PICTURE
        4 -> PathActivityType.REVIEW
        else -> PathActivityType.LISTENING
    }
    return PathActivity(
        id = "extra_${unit.number}_mid",
        unitNumber = unit.number,
        type = type,
        title = when (type) {
            PathActivityType.PICTURE -> "Picture match"
            PathActivityType.LISTENING -> "Listening lab"
            else -> "Quick review"
        },
        emoji = when (type) {
            PathActivityType.PICTURE -> "🖼️"
            PathActivityType.LISTENING -> "🎧"
            else -> "🔁"
        }
    )
}

fun storyFor(unit: CourseUnit) = PathActivity(
    id = "extra_${unit.number}_story",
    unitNumber = unit.number,
    type = PathActivityType.STORY,
    title = "Mini story",
    emoji = "📖"
)

val allPathActivities: List<PathActivity> = course.flatMap {
    listOf(interludeFor(it), storyFor(it))
}

/** Activities are unlocked based on lesson progress, not on bonus-activity completion. */
fun isActivityUnlocked(activity: PathActivity, completed: Set<String>): Boolean {
    val unit = course.firstOrNull { it.number == activity.unitNumber } ?: return false
    val prerequisite = if (activity.type == PathActivityType.STORY) unit.lessons
        else unit.lessons.take(2)
    return prerequisite.all { it.id in completed }
}
