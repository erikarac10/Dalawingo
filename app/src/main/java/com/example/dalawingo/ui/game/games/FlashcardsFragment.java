package com.example.dalawingo.ui.game.games;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dalawingo.R;
import com.example.dalawingo.data.Phrase;
import com.example.dalawingo.data.Unit;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.game.shared.Animations;
import com.example.dalawingo.ui.game.shared.SharedFunctions;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FlashcardsFragment extends Fragment {

    private static final String PREFS_NAME = "app_prefs";

    // Deck
    private List<Phrase> flashcards = new ArrayList<>();

    // Navigation: stores phrase indices in the order they were first seen
    private final List<Integer> seenOrder = new ArrayList<>();

    /**
     * pos is either:
     *   0 .. seenOrder.size()-1 : on a real card
     *   SUMMARY_POS (== flashcards.size()) : summary screen (special marker)
     */
    private int pos = 0;

    private boolean showingEnglish = true;
    private String currentUnitKey;

    private boolean shuffleOn = false;
    private final Random rng = new Random();

    private SharedPreferences prefs;

    // Track states by phraseIndex (not pos)
    private final Map<Integer, Integer> flashcardStates = new HashMap<>();

    // UI
    private TextView flashCardText, tvCounter;
    private MaterialCardView flashCard, markRight, markLeft;
    private ImageView iconShuffle;

    // Prevent replaying summary effects repeatedly
    private boolean summaryEffectsPlayed = false;

    // FIX: own the stroke animation so we can cancel it on navigation
    @Nullable private ValueAnimator strokeAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_flashcards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load current unit
        currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");
        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        if (currentUnit != null && currentUnit.getPhrases() != null) {
            flashcards = currentUnit.getPhrases();
        } else {
            flashcards = new ArrayList<>();
        }

        // Bind UI
        flashCardText = view.findViewById(R.id.flashCardText);
        tvCounter = view.findViewById(R.id.tvCounter);
        flashCard = view.findViewById(R.id.flashCard);
        markRight = view.findViewById(R.id.markRight);
        markLeft = view.findViewById(R.id.markLeft);

        ImageButton btnPrev = view.findViewById(R.id.btnBack);
        ImageButton btnNext = view.findViewById(R.id.btnNext);
        ImageView iconWrong = view.findViewById(R.id.iconWrong);
        ImageView iconRight = view.findViewById(R.id.iconRight);
        MaterialCardView btnReset = view.findViewById(R.id.fcbtnResetGame);

        iconShuffle = view.findViewById(R.id.iconShuffle);

        // Restore state
        restoreShuffleState();
        restoreDeckState();
        restoreFlipState();

        // If deck state is empty (first time), initialize
        if (seenOrder.isEmpty() && !flashcards.isEmpty()) {
            int first = shuffleOn ? rng.nextInt(flashcards.size()) : 0;
            seenOrder.add(first);
            pos = 0;
            saveDeckState();
        }

        // Safety: if restore left us on summary, don't keep replaying effects
        summaryEffectsPlayed = isOnSummary();

        // Initial UI
        updateShuffleIcon();
        updateUIForCurrentPosition();

        // Click listeners
        flashCard.setOnClickListener(v -> playFlipAnimation());
        btnNext.setOnClickListener(v -> handleNext());
        btnPrev.setOnClickListener(v -> handlePrev());
        iconWrong.setOnClickListener(v -> markFlashcard(-1));
        iconRight.setOnClickListener(v -> markFlashcard(1));
        btnReset.setOnClickListener(v -> resetFlashcards());

        iconShuffle.setOnClickListener(v -> toggleShuffle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelStrokeAnim();
    }

    /** -----------------------------
     *  Shuffle toggle
     *  ----------------------------- */
    private void toggleShuffle() {
        SharedFunctions.playSound(requireContext(), R.raw.button_press);
        shuffleOn = !shuffleOn;
        saveShuffleState();
        updateShuffleIcon();
        // Intentionally do NOT reshuffle past cards.
    }

    private void updateShuffleIcon() {
        if (iconShuffle == null) return;
        iconShuffle.setImageResource(shuffleOn ? R.drawable.ic_shuffle_on : R.drawable.ic_shuffle);
    }

    private void saveShuffleState() {
        prefs.edit()
                .putBoolean(prefKey("flash_shuffle_on_"), shuffleOn)
                .apply();
    }

    private void restoreShuffleState() {
        shuffleOn = prefs.getBoolean(prefKey("flash_shuffle_on_"), false);
    }

    /** -----------------------------
     *  Core handlers
     *  ----------------------------- */
    private void handleNext() {
        SharedFunctions.playSound(requireContext(), R.raw.button_press);

        final int size = flashcards.size();
        if (size == 0) return;

        // FIX: stop any prior mark animation before changing cards
        cancelStrokeAnim();

        // If currently on summary, reset
        if (isOnSummary()) {
            resetFlashcards();
            return;
        }

        // If we're not at the end of seenOrder, Next revisits history
        if (pos < seenOrder.size() - 1) {
            pos++;
            showingEnglish = true;
            summaryEffectsPlayed = false;
            saveFlipState();
            saveDeckState();
            updateUIForCurrentPosition();
            return;
        }

        // We're at end of history: choose NEW unseen card or finish
        if (countUniqueSeen(size) >= size) {
            goToSummary();
            return;
        }

        int nextPhraseIndex;
        if (shuffleOn) {
            nextPhraseIndex = pickRandomUnseenIndex(size);
        } else {
            nextPhraseIndex = pickFirstUnseenIndex(size);
        }

        addToSeenOrderIfMissing(nextPhraseIndex);

        pos = seenOrder.size() - 1;
        showingEnglish = true;
        summaryEffectsPlayed = false;

        saveFlipState();
        saveDeckState();
        updateUIForCurrentPosition();
    }

    private void handlePrev() {
        SharedFunctions.playSound(requireContext(), R.raw.button_press);

        final int size = flashcards.size();
        if (size == 0) return;

        // FIX: stop any prior mark animation before changing cards
        cancelStrokeAnim();

        if (isOnSummary()) {
            markRight.setVisibility(VISIBLE);
            markLeft.setVisibility(VISIBLE);

            pos = Math.max(0, seenOrder.size() - 1);
            showingEnglish = true;
            summaryEffectsPlayed = false;

            saveFlipState();
            saveDeckState();
            updateUIForCurrentPosition();

            tvCounter.setTypeface(null, Typeface.BOLD);
            return;
        }

        if (pos > 0) {
            pos--;
            showingEnglish = true;
            summaryEffectsPlayed = false;
            saveFlipState();
            saveDeckState();
            updateUIForCurrentPosition();
        }

        tvCounter.setTypeface(null, Typeface.BOLD);
    }

    private void goToSummary() {
        pos = flashcards.size(); // summary marker
        saveDeckState();
        updateUIForCurrentPosition();
    }

    private boolean isOnSummary() {
        return pos >= flashcards.size() && flashcards.size() > 0;
    }

    /** -----------------------------
     *  Choosing unseen cards
     *  ----------------------------- */
    private int pickRandomUnseenIndex(int size) {
        boolean[] seen = buildSeenFlags(size);
        int remaining = 0;
        for (boolean b : seen) if (!b) remaining++;

        if (remaining <= 0) return 0;

        int k = rng.nextInt(remaining);
        for (int i = 0; i < size; i++) {
            if (!seen[i]) {
                if (k == 0) return i;
                k--;
            }
        }
        return pickFirstUnseenIndex(size);
    }

    private int pickFirstUnseenIndex(int size) {
        boolean[] seen = buildSeenFlags(size);
        for (int i = 0; i < size; i++) {
            if (!seen[i]) return i;
        }
        return 0;
    }

    private boolean[] buildSeenFlags(int size) {
        boolean[] seen = new boolean[size];
        for (int idx : seenOrder) {
            if (idx >= 0 && idx < size) seen[idx] = true;
        }
        return seen;
    }

    private int countUniqueSeen(int size) {
        boolean[] seen = buildSeenFlags(size);
        int c = 0;
        for (boolean b : seen) if (b) c++;
        return c;
    }

    private void addToSeenOrderIfMissing(int phraseIndex) {
        for (int existing : seenOrder) {
            if (existing == phraseIndex) return;
        }
        seenOrder.add(phraseIndex);
    }

    /** -----------------------------
     *  Marking
     *  ----------------------------- */
    private void markFlashcard(int state) {
        final int size = flashcards.size();
        if (size == 0 || isOnSummary()) return;
        if (!isPosOnRealCard()) return;

        int phraseIndex = seenOrder.get(pos);

        int colorResId = (state == 1) ? R.color.pastel_green : R.color.pastel_pink;
        SharedFunctions.playSound(requireContext(), state == 1 ? R.raw.right : R.raw.wrong);

        // FIX: cancel any in-flight animation, then animate stroke to mark color
        cancelStrokeAnim();
        animateStrokeTo(ContextCompat.getColor(requireContext(), colorResId));

        flashcardStates.put(phraseIndex, state);
    }

    /** -----------------------------
     *  UI updates
     *  ----------------------------- */
    private void updateUIForCurrentPosition() {
        final int size = flashcards.size();

        if (size == 0) {
            cancelStrokeAnim();
            flashCardText.setText("");
            tvCounter.setText("0/0");
            markRight.setVisibility(GONE);
            markLeft.setVisibility(GONE);
            flashCard.setStrokeColor(Color.TRANSPARENT);
            return;
        }

        sanitizeState();

        if (isOnSummary()) {
            showSummaryCardOnce();
            return;
        }

        if (!isPosOnRealCard()) {
            pos = Math.max(0, seenOrder.size() - 1);
        }

        int phraseIndex = seenOrder.get(pos);
        Phrase phrase = flashcards.get(phraseIndex);

        flashCardText.setText(showingEnglish ? phrase.english : phrase.tagalog);

        tvCounter.setTypeface(null, Typeface.BOLD);
        tvCounter.setText((pos + 1) + "/" + size);

        // FIX: stop any previous stroke animation *before* applying this card's stroke
        cancelStrokeAnim();
        applyStrokeForPhraseIndex(phraseIndex);

        markRight.setVisibility(VISIBLE);
        markLeft.setVisibility(VISIBLE);
    }

    private void applyStrokeForPhraseIndex(int phraseIndex) {
        Integer state = flashcardStates.get(phraseIndex);
        if (state != null && state != 0) {
            int colorResId = (state == 1) ? R.color.pastel_green : R.color.pastel_pink;
            flashCard.setStrokeColor(ContextCompat.getColor(requireContext(), colorResId));
        } else {
            flashCard.setStrokeColor(Color.TRANSPARENT);
        }
    }

    private boolean isPosOnRealCard() {
        return pos >= 0 && pos < seenOrder.size();
    }

    private void playFlipAnimation() {
        final int size = flashcards.size();
        if (size == 0 || isOnSummary()) return;
        if (!isPosOnRealCard()) return;

        SharedFunctions.playSound(requireContext(), R.raw.flip);

        flashCard.setCameraDistance(8000 * getResources().getDisplayMetrics().density);
        AnimatorSet flipOut = (AnimatorSet) AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_out);
        AnimatorSet flipIn = (AnimatorSet) AnimatorInflater.loadAnimator(requireContext(), R.animator.flip_in);

        flipOut.setTarget(flashCard);
        flipIn.setTarget(flashCard);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                showingEnglish = !showingEnglish;

                if (!isPosOnRealCard()) return;
                int phraseIndex = seenOrder.get(pos);
                Phrase current = flashcards.get(phraseIndex);
                flashCardText.setText(showingEnglish ? current.english : current.tagalog);

                saveFlipState();
                flipIn.start();
            }
        });

        flipOut.start();
    }

    /** -----------------------------
     *  Stroke animation helpers (FIX)
     *  ----------------------------- */
    private void cancelStrokeAnim() {
        if (strokeAnimator != null) {
            strokeAnimator.cancel();
            strokeAnimator = null;
        }
        // also stop any ViewPropertyAnimator just in case you later animate the card itself
        if (flashCard != null) flashCard.animate().cancel();
    }

    private void animateStrokeTo(@ColorInt int targetColor) {
        // If stroke width is 0, strokeColor won’t show; assume your style sets it.
        final int startColor = flashCard.getStrokeColor();

        // If already there, just set and bail
        if (startColor == targetColor) {
            flashCard.setStrokeColor(targetColor);
            return;
        }

        strokeAnimator = ValueAnimator.ofArgb(startColor, targetColor);
        strokeAnimator.setDuration(180); // quick feedback
        strokeAnimator.addUpdateListener(a -> {
            int c = (int) a.getAnimatedValue();
            flashCard.setStrokeColor(c);
        });
        strokeAnimator.start();
    }

    /** -----------------------------
     *  Summary (play effects only once per entry)
     *  ----------------------------- */
    private void showSummaryCardOnce() {
        flashCardText.setText("⭐ Magaling ⭐");

        String summary = "Correct: " + getCorrectCount()
                + "\nIncorrect: " + getWrongCount()
                + "\nUnanswered: " + getUnmarkedCount();

        tvCounter.setTypeface(null, Typeface.NORMAL);
        tvCounter.setText(summary);

        markRight.setVisibility(GONE);
        markLeft.setVisibility(GONE);

        // Always cancel stroke animations and hide stroke on summary
        cancelStrokeAnim();
        flashCard.setStrokeColor(Color.TRANSPARENT);

        if (!summaryEffectsPlayed) {
            summaryEffectsPlayed = true;

            SharedFunctions.playSound(requireContext(), R.raw.congrats);

            flashCardText.setAlpha(0f);
            flashCardText.animate().alpha(1f).setDuration(600).start();

            tvCounter.setAlpha(0f);
            tvCounter.animate().alpha(1f).setDuration(600).start();

            LottieAnimationView celebrateAnim = null;
            if (getActivity() != null) {
                celebrateAnim = getActivity().findViewById(R.id.fireworkAnim);
            }
            if (celebrateAnim != null) {
                Animations.playFireworks(celebrateAnim);
            }

            SharedFunctions.saveBestScore(
                    prefs,
                    currentUnitKey + "_best_score_flashcards",
                    getCorrectCount(),
                    flashcards.size()
            );
        }
    }

    /** -----------------------------
     *  Counts
     *  ----------------------------- */
    private int getCorrectCount() {
        int count = 0;
        for (Integer value : flashcardStates.values()) {
            if (value != null && value == 1) count++;
        }
        return count;
    }

    private int getWrongCount() {
        int count = 0;
        for (Integer value : flashcardStates.values()) {
            if (value != null && value == -1) count++;
        }
        return count;
    }

    private int getUnmarkedCount() {
        int count = 0;
        for (int i = 0; i < flashcards.size(); i++) {
            Integer value = flashcardStates.get(i);
            if (value == null || value == 0) count++;
        }
        return count;
    }

    /** -----------------------------
     *  Reset
     *  ----------------------------- */
    private void resetFlashcards() {
        SharedFunctions.playSound(requireContext(), R.raw.button_press);

        cancelStrokeAnim();

        flashcardStates.clear();
        showingEnglish = true;

        seenOrder.clear();
        pos = 0;

        summaryEffectsPlayed = false;

        if (!flashcards.isEmpty()) {
            int first = shuffleOn ? rng.nextInt(flashcards.size()) : 0;
            seenOrder.add(first);
        }

        saveFlipState();
        saveDeckState();

        flashCard.setOnClickListener(v -> playFlipAnimation());
        flashCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pastel_yellow));
        flashCard.setStrokeColor(Color.TRANSPARENT);

        markRight.setVisibility(VISIBLE);
        markLeft.setVisibility(VISIBLE);

        tvCounter.setTypeface(null, Typeface.BOLD);
        updateUIForCurrentPosition();
    }

    /** -----------------------------
     *  Persist flip + deck state
     *  ----------------------------- */
    private void saveFlipState() {
        prefs.edit()
                .putBoolean(prefKey("flash_showing_english_"), showingEnglish)
                .apply();
    }

    private void restoreFlipState() {
        showingEnglish = prefs.getBoolean(prefKey("flash_showing_english_"), true);
    }

    private void saveDeckState() {
        JSONArray arr = new JSONArray();
        for (int idx : seenOrder) arr.put(idx);

        prefs.edit()
                .putString(prefKey("flash_seen_order_"), arr.toString())
                .putInt(prefKey("flash_pos_"), pos)
                .apply();
    }

    private void restoreDeckState() {
        seenOrder.clear();

        String json = prefs.getString(prefKey("flash_seen_order_"), null);
        pos = prefs.getInt(prefKey("flash_pos_"), 0);

        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    int v = arr.getInt(i);
                    seenOrder.add(v);
                }
            } catch (JSONException ignored) {
                seenOrder.clear();
                pos = 0;
            }
        }

        sanitizeState();
    }

    /** -----------------------------
     *  State safety / sanitizing
     *  ----------------------------- */
    private void sanitizeState() {
        final int size = flashcards.size();

        boolean[] used = new boolean[Math.max(0, size)];
        for (int i = seenOrder.size() - 1; i >= 0; i--) {
            int v = seenOrder.get(i);
            if (v < 0 || v >= size) {
                seenOrder.remove(i);
                continue;
            }
            if (used[v]) {
                seenOrder.remove(i);
            } else {
                used[v] = true;
            }
        }

        if (seenOrder.isEmpty() && size > 0) {
            seenOrder.add(0);
        }

        if (pos < 0) pos = 0;

        if (pos >= size) {
            if (countUniqueSeen(size) >= size) {
                pos = size;
            } else {
                pos = Math.max(0, seenOrder.size() - 1);
            }
        } else {
            if (pos >= seenOrder.size()) {
                pos = Math.max(0, seenOrder.size() - 1);
            }
        }
    }

    private String prefKey(String prefix) {
        return prefix + currentUnitKey;
    }
}
