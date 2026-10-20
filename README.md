# Dalawingo 🇵🇭

**A playful Android app for learning Tagalog, one lesson at a time.**

Dalawingo combines bite-sized language lessons with an interactive learning path, daily practice, adaptive vocabulary review, and a customizable animal companion. It's a personal project built with **Kotlin** and **Jetpack Compose**.

> **Work in progress.** Features and artwork are still being refined. Some graphics currently use emoji or placeholder assets, and speech recognition depends on device support.

## Features

### 📚 Learn Tagalog

- **15 lessons**, with **four rounds per lesson node** and visual progress rings.
- A Duolingo-inspired learning path with unlockable lessons, unit headers, guidebooks, and animated progress reveals.
- Multiple-choice, word-bank, listening, speaking, audio-pair matching, speaking flashcards, and picture-based activities.
- Optional review challenges and short scripted stories along the path.
- Lesson results showing **XP, elapsed time, and accuracy**.

### 🧠 Practice and progress

- Daily practice using vocabulary from completed lessons.
- Adaptive review that tracks phrase-level attempts, accuracy, consecutive correct answers, and review timing.
- Vocabulary grouped into **New, Weak, Developing, and Mastered**, with additional spaced-review scheduling.
- XP, learning streaks, golden lesson mastery, and a **Tagalog Score** reflecting course and vocabulary progress.

### 🏆 Quests and rewards

- Three rotating daily quests with **Bronze, Silver, and Gold** rewards.
- **25 Pesos** for the first completed daily-practice session each day.
- Pesos earned from lessons, perfect scores, unit completion, quests, and achievements.
- Claimable achievements with unlock conditions, progress, dates, and rewards.
- Timed XP boosts that can be saved and activated later.

### 🐾 Companion and customization

- A customizable animal companion on the Player tab.
- A shop and wardrobe for pets, clothing, accessories, and backgrounds.
- Cosmetic purchases and achievement-exclusive unlocks saved locally.

## App navigation

| Screen | Purpose |
| --- | --- |
| **Player** | Animal companion, Pesos wallet, featured Daily Practice, and daily quests |
| **Learn** | Learning path, lesson rounds, guidebooks, and bonus activities |
| **Progress** | Learning statistics, vocabulary mastery, achievements, and boosts |

The app uses an icon-only bottom navigation bar. Units and guidebooks open from the Learn path rather than separate bottom tabs.

## Built with

- **Kotlin**
- **Jetpack Compose** for Android UI
- Android text-to-speech and speech-recognition APIs for audio activities
- **SharedPreferences** for on-device progress, rewards, and customization data

## Getting started

1. Clone or download the repository.
2. Open the project folder in **Android Studio**.
3. Let Gradle sync and install any SDK components Android Studio requests.
4. Run the `app` configuration on an Android emulator or device.
5. Grant microphone access when prompted if you want to use speaking activities.

For speaking exercises, use a device or emulator with a speech-recognition service that supports Filipino/Tagalog. Audio behavior may differ between devices.

## Project structure

```text
app/src/main/
├── AndroidManifest.xml
└── java/com/dalawingo/app/
    ├── MainActivity.kt       # Navigation and game flow
    ├── data/                 # Persistent learner progress and rewards
    ├── model/                # Course content, exercises, quests, achievements
    └── ui/                   # Compose screens, minigames, and components
```

## In progress

- Replacing placeholder emojis and graphics with a consistent custom icon and character-art system.
- Refining animations, lesson interactions, and responsive layouts.
- Testing speech recognition, accessibility, and Android device compatibility.

## Note

Dalawingo is an independent learning project inspired by the approachable, game-like design of language-learning apps such as Duolingo. It is **not affiliated with or endorsed by Duolingo**.
