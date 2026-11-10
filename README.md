# Dalawingo 🇵🇭

**A Duolingo-inspired Android learning app for Tagalog.** Original name, mascot, artwork, course content, and implementation. Written in **Kotlin + Jetpack Compose**, designed for phones, and runnable without a server or a login.

## Run in Android Studio

1. Unzip the `Dalawingo-Android-Studio.zip` archive.
2. Open **Android Studio → Open** and select the **Dalawingo** folder (the folder containing `settings.gradle.kts`). **Do not create a second project.**
3. Allow Gradle to sync and install missing SDK components when prompted. Java 17 or higher is required. The project targets Android SDK 36.
4. Choose an Android emulator or your connected phone (Android 8.0 / API 26 or newer) and click the green **Run ▶** button.

The first Gradle sync requires internet access to download the Android and Compose dependencies. The **app itself works offline**.

### Features

- 🏠 **Player landing screen**: the default tab, featuring your tarsier friend, progress card, Continue button and daily quests.
- 📚 **Learn**: colorful, zigzag lesson path with locked nodes, completed stars, and an unlock sequence.
- 🗺️ **Units**: five expandable chapter cards. Future units unlock after completing previous lessons; each unit has a guidebook.
- ✏️ **Interactive lessons**: choose translations, arrange Tagalog word tiles, and identify phrases spoken using Android Text-to-Speech. Every answer gives immediate feedback.
- 💗 **Hearts**: up to five, lose one for a wrong answer. Practice earns one back; spend 50 gems for a refill, or return the next day.
- ⚡ **XP, gems and streaks**: earned through completed lessons and practice; tracked across app launches.
- 🎯 **Daily quests**: earn XP, finish a lesson, and practice.
- 👤 **Profile**: change your name, see total XP / day streak / lesson count, unlock achievements and manage hearts.
- 🇵🇭 **Tagalog only**: 5 units, 15 lessons, 90 hand-written English↔Tagalog phrase pairs.
- 💾 **Offline save**: progress stored in Android SharedPreferences, on this device.

### How progression works

All lessons are initially locked **except the first**. Finish lesson 1 to unlock lesson 2, and so on. Complete three lessons to unlock the next unit. Finished lessons can be replayed without spending hearts. Practice uses the phrases from lessons you've learned, or early-course phrases if you are new. Daily streaks advance when you finish a lesson or practice on consecutive calendar days.

### Listening exercises

Dalawingo asks Android's Text-to-Speech engine to pronounce Tagalog. Whether Tagalog speech is available depends on the TTS voices installed on your device. All listening questions include **SHOW TEXT INSTEAD**, so nothing is blocked if your device has no Tagalog voice.

### Project map

```text
app/src/main/java/com/dalawingo/app/
  MainActivity.kt              App navigation / session flow
  model/Course.kt              Tagalog course content + lesson unlocks
  model/ExerciseFactory.kt     Question generation
  data/ProgressStore.kt        Device-only saved progress, XP and streaks
  ui/Theme.kt                  Colors
  ui/Components.kt             Buttons, cards, custom tarsier mascot
  ui/HomeScreen.kt             Player dashboard + quests
  ui/PathScreen.kt             Learning path
  ui/UnitsScreen.kt            Expandable units + vocabulary guidebook
  ui/LessonScreen.kt           Interactive exercise / results screens
  ui/ProfileScreen.kt          Stats and achievements
```

Tests: `app/src/test/java/com/dalawingo/app/model/CourseTest.kt`. Run `./gradlew testDebugUnitTest` or select **Run Tests** in Android Studio.

### Build setup

- Android Gradle Plugin 8.13.2, Gradle 8.13, Kotlin 2.2.21, Compose BOM 2026.04.01.
- The included `gradle-wrapper.jar` is a **small self-contained Gradle bootstrap launcher** whose Java source is at `build-support/GradleWrapperMain.java`. It downloads the pinned official Gradle distribution during first sync and then invokes it. For a conventional official Gradle wrapper, run `gradle wrapper --gradle-version 8.13` from the project directory once Gradle is installed.
- Android Studio can generate an APK: **Build → Build Bundle(s) / APK(s) → Build APK(s)**.

**Scope**: A playable, offline, single-player Duolingo-style clone; not the commercial Duolingo app or its copyrighted assets. There is no login, cloud sync, server leaderboard, paid shop, or remotely generated lessons.
