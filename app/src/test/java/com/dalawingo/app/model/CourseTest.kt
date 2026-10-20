package com.dalawingo.app.model

import org.junit.Assert.*
import org.junit.Test

class CourseTest {
    @Test fun `course has five units and all lessons have six phrases`() {
        assertEquals(5, course.size)
        assertEquals(15, allLessons.size)
        assertEquals(90, allPhrases.size)
        assertTrue(allLessons.all { it.phrases.size == 6 })
    }

    @Test fun `lesson unlocking follows the path`() {
        assertTrue(isUnlocked(allLessons.first(), emptySet()))
        assertFalse(isUnlocked(allLessons[1], emptySet()))
        assertTrue(isUnlocked(allLessons[1], setOf(allLessons[0].id)))
        assertEquals(allLessons[1], nextLesson(setOf(allLessons[0].id)))
    }

    @Test fun `every generated question can be solved`() {
        allLessons.forEach { lesson ->
            val exercises = lessonExercises(lesson)
            assertEquals(6, exercises.size)
            exercises.forEach { exercise ->
                if (exercise.type == ExerciseType.WORD_BANK) {
                    val bank = exercise.tiles.map { it.text }.toMutableList()
                    val answer = exercise.phrase.tagalog.replace(Regex("[?.!,]"), "")
                        .split(Regex("\\s+"))
                    answer.forEach { word -> assertTrue("Missing $word in ${lesson.id}", bank.remove(word)) }
                } else {
                    assertTrue(exercise.correctAnswer in exercise.answers)
                }
            }
        }
    }

    @Test fun `practice works even before a lesson is completed`() {
        assertEquals(6, practiceExercises(emptySet()).size)
        assertEquals(6, practiceExercises(setOf("1-1")).size)
    }
}
