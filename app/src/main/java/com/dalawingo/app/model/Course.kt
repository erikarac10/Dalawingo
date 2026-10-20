package com.dalawingo.app.model

/** All content is bundled with the app, so lessons work completely offline. */
data class Phrase(val english: String, val tagalog: String)
data class Lesson(val id: String, val title: String, val emoji: String, val phrases: List<Phrase>)
data class CourseUnit(
    val number: Int,
    val title: String,
    val description: String,
    val emoji: String,
    val lessons: List<Lesson>
)

private fun lesson(id: String, title: String, emoji: String, vararg words: Pair<String, String>) =
    Lesson(id, title, emoji, words.map { Phrase(it.first, it.second) })

val course: List<CourseUnit> = listOf(
    CourseUnit(1, "First words", "Greet people and introduce yourself", "👋", listOf(
        lesson("1-1", "Say hello", "👋",
            "Hello" to "Kumusta",
            "Good morning" to "Magandang umaga",
            "Good evening" to "Magandang gabi",
            "Goodbye" to "Paalam",
            "Thank you" to "Salamat",
            "You're welcome" to "Walang anuman"),
        lesson("1-2", "Introduce yourself", "💬",
            "My name is Ana" to "Ang pangalan ko ay Ana",
            "What is your name?" to "Ano ang pangalan mo?",
            "How are you?" to "Kumusta ka?",
            "I'm fine" to "Ayos lang ako",
            "Nice to meet you" to "Masaya akong makilala ka",
            "See you later" to "Kita tayo mamaya"),
        lesson("1-3", "Be polite", "🌷",
            "Yes" to "Oo",
            "No" to "Hindi",
            "Excuse me" to "Paumanhin",
            "I'm sorry" to "Pasensya na",
            "I don't understand" to "Hindi ko maintindihan",
            "No problem" to "Walang problema")
    )),
    CourseUnit(2, "The basics", "Learn people, numbers and questions", "✨", listOf(
        lesson("2-1", "People around you", "👨‍👩‍👧",
            "Mother" to "Nanay",
            "Father" to "Tatay",
            "Friend" to "Kaibigan",
            "Child" to "Bata",
            "Teacher" to "Guro",
            "Family" to "Pamilya"),
        lesson("2-2", "Count to ten", "🔢",
            "One" to "Isa",
            "Two" to "Dalawa",
            "Three" to "Tatlo",
            "Four" to "Apat",
            "Five" to "Lima",
            "Ten" to "Sampu"),
        lesson("2-3", "Ask questions", "❓",
            "What is this?" to "Ano ito?",
            "Where are you?" to "Nasaan ka?",
            "Who are you?" to "Sino ka?",
            "Why?" to "Bakit?",
            "When?" to "Kailan?",
            "How much?" to "Magkano?")
    )),
    CourseUnit(3, "Food & drinks", "Order food and talk about meals", "🍚", listOf(
        lesson("3-1", "What's on the table?", "🍎",
            "Rice" to "Kanin",
            "Water" to "Tubig",
            "Bread" to "Tinapay",
            "Fish" to "Isda",
            "Fruit" to "Prutas",
            "Coffee" to "Kape"),
        lesson("3-2", "Let's eat!", "🍽️",
            "I'm hungry" to "Gutom ako",
            "I'm thirsty" to "Uhaw ako",
            "It's delicious" to "Masarap ito",
            "I want water" to "Gusto ko ng tubig",
            "Let's eat" to "Kain tayo",
            "I'm full" to "Busog na ako"),
        lesson("3-3", "At a café", "☕",
            "May I order?" to "Puwede bang umorder?",
            "How much is this?" to "Magkano ito?",
            "One coffee, please" to "Isang kape po",
            "No sugar" to "Walang asukal",
            "Do you have water?" to "May tubig ba kayo?",
            "The bill, please" to "Ang bill po")
    )),
    CourseUnit(4, "Around town", "Find places and get around", "🗺️", listOf(
        lesson("4-1", "Places", "🏠",
            "House" to "Bahay",
            "School" to "Paaralan",
            "Market" to "Palengke",
            "Hospital" to "Ospital",
            "Station" to "Istasyon",
            "Beach" to "Dalampasigan"),
        lesson("4-2", "Find your way", "🧭",
            "Left" to "Kaliwa",
            "Right" to "Kanan",
            "Straight ahead" to "Diretso",
            "Near" to "Malapit",
            "Far" to "Malayo",
            "Here" to "Dito"),
        lesson("4-3", "Go somewhere", "🚌",
            "Where is the bus?" to "Nasaan ang bus?",
            "Let's go" to "Tara na",
            "Stop here" to "Dito po ang baba",
            "I need help" to "Kailangan ko ng tulong",
            "I'm lost" to "Naliligaw ako",
            "Thank you for your help" to "Salamat sa tulong mo")
    )),
    CourseUnit(5, "Real conversations", "Share how you feel and what you do", "💞", listOf(
        lesson("5-1", "How you feel", "😊",
            "I'm tired" to "Pagod ako",
            "I'm happy" to "Masaya ako",
            "I'm busy" to "Abala ako",
            "I'm ready" to "Handa na ako",
            "It's hot" to "Mainit",
            "It's raining" to "Umuulan"),
        lesson("5-2", "Everyday actions", "📖",
            "I'm eating" to "Kumakain ako",
            "I'm drinking water" to "Umiinom ako ng tubig",
            "I'm going home" to "Uuwi na ako",
            "I'm studying" to "Nag-aaral ako",
            "I'm reading" to "Nagbabasa ako",
            "I'm sleeping" to "Natutulog ako"),
        lesson("5-3", "Stay connected", "💌",
            "I love you" to "Mahal kita",
            "I miss you" to "Miss na kita",
            "Take care" to "Ingat ka",
            "Happy birthday" to "Maligayang kaarawan",
            "Good luck" to "Suwertihin ka sana",
            "See you tomorrow" to "Kita tayo bukas")
    ))
)

val allLessons: List<Lesson> = course.flatMap { it.lessons }
val allPhrases: List<Phrase> = allLessons.flatMap { it.phrases }

fun isUnlocked(lesson: Lesson, completed: Set<String>): Boolean {
    val index = allLessons.indexOfFirst { it.id == lesson.id }
    return index == 0 || (index > 0 && allLessons[index - 1].id in completed)
}

fun nextLesson(completed: Set<String>): Lesson? = allLessons.firstOrNull { it.id !in completed }
