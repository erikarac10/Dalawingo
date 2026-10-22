# Dalawingo — October 22, 2026 UI and Gameplay Update

This ZIP contains an updated **Android Studio/Jetpack Compose project**. Open the extracted `Duolingo` project folder in Android Studio (File → Open); do not import the ZIP as a drawable.

## Installation / preserving progress

1. Extract this ZIP. Back up your previous Android Studio project before replacing it.
2. Open the new `Duolingo` folder in Android Studio. Let Gradle sync, then **Build → Make Project** (`Ctrl+F9`).
3. Run on your Android phone or emulator. **Install as an update to the same `com.dalawingo.app` app** to preserve saved lessons, Pesos, pets, achievements, and other SharedPreferences. Do not uninstall the existing app or clear its app data first. Existing `golden` storage is retained for compatibility, but the visible term is **Perfect Lessons**.
4. On the first speaking activity, Android should prompt for microphone permission. Tap **Allow**. Your device needs a compatible Filipino/Tagalog recognition service; support varies by OS and installed language packs.
5. To see Compose Previews, open `app/src/main/java/com/dalawingo/app/ui/Previews.kt` and click **Build & Refresh**.

## What changed

- **Player/Home:** Full-width pet scene with same-height header as Learn; no vertical scrolling; compact Daily Practice section with rotating short call-to-action, animated Peso sparkle and 25-Peso first-practice reward; green glossy Daily Quest progress bars turning gold when completed.
- **Learn:** Raised section/book controls with press-down feedback; animated attention star and expanding ring only on the current unfinished lesson; completed checkmarks and golden perfected nodes; the `1/4` label and `START` float badge are removed. Lesson pop-up explains rounds/XP and the +10-Peso first-perfect bonus on completed reviews. Bigger node-unlock animation on return to the path.
- **Progress:** Statistics first, horizontally scrolling **Achievements** second, Vocabulary mastery third; no Tagalog Score or redundant vocabulary navigation button. The 25 achievements use coloured illustrated/flat-art PNGs when unlocked, and grayscale PNGs when locked. Claimable badges and Claim Reward buttons pulse.
- **Exercises:** New *Fill in the Blank*, *Complete the Sentence*, *Listen for the Missing Word*, *Written Matching Pairs*, and *Type the Answer* mixed with existing activities. Word-bank tiles play audio when tapped and leave grey placeholders; matching pairs select blue, wobble red when wrong, and bounce green when correct. Pressed buttons depress. In-app microphone has a custom vector icon and runtime permission request; it no longer relies on the external recognition prompt. Recording waveform is animated.
- **Feedback/Results:** Correct/incorrect WAV sound effects, slide-up answer feedback, per-lesson **ON A ROLL** counter, glossy progress bar, large confetti celebration, sequential XP/time/accuracy stat cards, and falling/collecting Pesos. No irrelevant gift reward.
- **Assets:** Existing custom drawables are preserved. New monochrome vector assets live in `app/src/main/res/drawable/`; the complete 25 PNG achievement icon set lives in `app/src/main/res/drawable-nodpi/`. Two sound assets are in `app/src/main/res/raw/`.
- **Rewards and saves:** Daily Practice remains +25 Pesos on the first completion each day; first full lesson completion +20; first perfect score in that lesson +10; first full unit completion +50. Achievement claim IDs and `dalawingo_progress_v1` saved data remain compatible. New stat counters only collect events after this update. One-time claim rules remain unchanged.

## Caveats / remaining visual work

- **Full Android build and on-device testing have not been performed in this environment.** Model-only Kotlin tests passed across the 15 × 4 × 6 generated lesson exercises; drawable/manifest XML and resource references were validated. Run Gradle in Android Studio and test speech recognition on a phone before release.
- The ZIP contains **10 supplied animal achievement PNGs**, plus **15 newly drawn simple icon-based PNGs** (no characters). These 15 work immediately but can be replaced with bespoke Filipino-animal illustrations later. Achievement *levels/tier-specific illustrations* are not implemented; each of the 25 families has one claimable milestone for now.
- Certain vocabulary-picture emojis, cosmetic placeholders, pet representations, and the optional **lock emoji** remain where no matching supplied vector or PNG existed. Systematic replacements were made for navigation/UI graphics with available or newly created vectors; don't mistake the remaining content emojis for missing icon references.
- This update uses a simple in-app matching/motion system. Word-bank tiles animate into the response area, but do not use exact position-to-position spring trajectories identical to the commercial app.
- Audio uses Android's local TextToSpeech and SpeechRecognizer. The app cannot guarantee high-quality Filipino voices, perfect accent recognition, or an installed recognition pack on every Android device.

## Asset names

See `app/src/main/res/drawable-nodpi/` for all `badge_*.png` images, e.g. `badge_first_steps.png`, `badge_perfect_lessons.png`, `badge_on_fire.png`, `badge_early_riser.png`, and `badge_tagalog_champion.png`. Compose uses `painterResource(R.drawable.badge_first_steps)` etc. New UI vector icons follow `ic_*.xml` in `res/drawable/`.
