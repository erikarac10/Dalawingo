package com.example.dalawingo.ui.game.games;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SpotErrorFragment extends Fragment {

    private LinearLayout spTextContainer, spInputContainer;
    private TextView gsEnglish, gsCounter;
    private MaterialCardView resetBtn;

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private String originalWord;
    private String corruptedWord;
    private char corruptedChar;
    private int corruptedIndex;
    private int selectedIndex = -1;
    private final Random random = new Random();

    private String currentUnitKey;

    private final int[] vowelIds = {R.id.vowelA, R.id.vowelE, R.id.vowelI, R.id.vowelO, R.id.vowelU};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_spot_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spTextContainer = view.findViewById(R.id.spTextContainer);
        spInputContainer = view.findViewById(R.id.spInputContainer);
        gsEnglish = view.findViewById(R.id.gsEnglish);
        gsCounter = view.findViewById(R.id.gsCounter);
        resetBtn = view.findViewById(R.id.gsbtnResetGame);

        resetBtn.setOnClickListener(v -> resetGame());

        currentUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", requireContext().MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");

        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = new ArrayList<>();
        for (Phrase p : currentUnit.getPhrases()) {
            if (!p.tagalog.contains(" ") && hasVowel(p.tagalog)) {
                phrases.add(p);
            }
        }
        Collections.shuffle(phrases);

        loadNextQuestion();
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions || questionIndex >= phrases.size()) {
            showSummary();
            return;
        }

        Phrase phrase = phrases.get(questionIndex);
        originalWord = phrase.tagalog.trim();
        gsEnglish.setText(phrase.english);
        gsCounter.setText((questionIndex + 1) + "/" + totalQuestions);

        corruptedWord = corruptVowel(originalWord);
        renderWord(corruptedWord);

        SharedFunctions.animateSpotErrorFadeIn(spTextContainer);
        resetVowelButtons();
        spInputContainer.setVisibility(View.GONE);

        resetBtn.setVisibility(View.GONE);
    }

    /** ---------------- Rendering ---------------- */

    private void renderWord(String word) {
        spTextContainer.removeAllViews();
        selectedIndex = -1;

        String vowels = "AEIOU";
        for (int i = 0; i < word.length(); i++) {
            char c = Character.toUpperCase(word.charAt(i));

            TextView letterView = new TextView(requireContext());
            letterView.setText(String.valueOf(c));
            letterView.setTextSize(33);
            letterView.setTypeface(null, Typeface.BOLD);
            letterView.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
            letterView.setPadding(6, 0, 6, 0);

            int index = i;
            if (vowels.indexOf(c) != -1) {
                letterView.setOnClickListener(v -> {
                    resetWordHighlights();
                    letterView.setPaintFlags(letterView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    selectedIndex = index;

                    spInputContainer.setVisibility(View.VISIBLE);
                    setupVowelGuessing();
                });
            }

            spTextContainer.addView(letterView);
        }
    }

    private void resetWordHighlights() {
        for (int j = 0; j < spTextContainer.getChildCount(); j++) {
            TextView child = (TextView) spTextContainer.getChildAt(j);
            child.setPaintFlags(child.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
            child.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        }
    }

    private void resetVowelButtons() {
        for (int id : vowelIds) {
            TextView vowelView = requireView().findViewById(id);
            vowelView.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
            vowelView.setPaintFlags(vowelView.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    /** ---------------- Guess Handling ---------------- */

    private void setupVowelGuessing() {
        for (int id : vowelIds) {
            TextView vowelView = requireView().findViewById(id);

            vowelView.setOnClickListener(v -> {
                if (selectedIndex == -1) return;
                resetVowelButtons();

                vowelView.setPaintFlags(vowelView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                char chosen = Character.toUpperCase(vowelView.getText().toString().charAt(0));

                updateLetter(chosen, ContextCompat.getColor(requireContext(), R.color.brown));

                if (selectedIndex == corruptedIndex &&
                        Character.toUpperCase(chosen) == Character.toUpperCase(corruptedChar)) {
                    SharedFunctions.playSound(requireContext(), R.raw.right);
                    correctCount++;
                    updateLetter(originalWord.toUpperCase().charAt(selectedIndex),
                            ContextCompat.getColor(requireContext(), R.color.pastel_green));
                    vowelView.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_green));

                    SharedFunctions.animateSpotErrorPop(spTextContainer.getChildAt(selectedIndex));
                    SharedFunctions.animateSpotErrorPop(vowelView);

                } else {
                    SharedFunctions.playSound(requireContext(), R.raw.wrong);
                    updateLetter(chosen, ContextCompat.getColor(requireContext(), R.color.pastel_red));
                    vowelView.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_red));

                    SharedFunctions.animateSpotErrorShake(spTextContainer.getChildAt(selectedIndex));
                    SharedFunctions.animateSpotErrorShake(vowelView);
                }

                spTextContainer.postDelayed(this::next, 1000);
            });
        }
    }

    /** ---------------- Helpers ---------------- */

    private void updateLetter(char newChar, int color) {
        if (selectedIndex >= 0 && selectedIndex < spTextContainer.getChildCount()) {
            TextView letterView = (TextView) spTextContainer.getChildAt(selectedIndex);
            letterView.setText(String.valueOf(Character.toUpperCase(newChar)));
            letterView.setTextColor(color);
        }
    }

    private boolean hasVowel(String word) {
        return word.matches(".*[aeiouAEIOU].*");
    }

    private String corruptVowel(String word) {
        List<Integer> vowelIndexes = new ArrayList<>();
        String vowels = "aeiou";
        for (int i = 0; i < word.length(); i++) {
            if (vowels.indexOf(Character.toLowerCase(word.charAt(i))) != -1) {
                vowelIndexes.add(i);
            }
        }
        if (vowelIndexes.isEmpty()) return word;

        corruptedIndex = vowelIndexes.get(random.nextInt(vowelIndexes.size()));
        corruptedChar = word.charAt(corruptedIndex);

        char replacement;
        do {
            replacement = vowels.charAt(random.nextInt(vowels.length()));
        } while (Character.toLowerCase(replacement) == Character.toLowerCase(corruptedChar));

        if (Character.isUpperCase(corruptedChar)) {
            replacement = Character.toUpperCase(replacement);
        }

        return word.substring(0, corruptedIndex) + replacement + word.substring(corruptedIndex + 1);
    }

    /** ---------------- Navigation ---------------- */

    private void next() {
        questionIndex++;
        spTextContainer.postDelayed(this::loadNextQuestion, 500);
    }

    /** ---------------- Summary + Reset ---------------- */
    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.saveBestScore(
                requireActivity().getSharedPreferences("app_prefs", requireContext().MODE_PRIVATE),
                currentUnitKey + "_best_score_spot_error",
                correctCount,
                totalQuestions
        );

        spTextContainer.removeAllViews();

        TextView title = new TextView(requireContext());

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                title,
                gsCounter,
                gsEnglish,
                "Score: " + correctCount + " / " + totalQuestions,
                resetBtn
        );
        spTextContainer.addView(title);
        spInputContainer.setVisibility(View.GONE);
    }

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        spTextContainer.removeAllViews();
        spInputContainer.setVisibility(View.GONE);

        gsCounter.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.GONE);

        loadNextQuestion();
    }
}
