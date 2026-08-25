package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TypeInFragment extends Fragment {

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private TextView tiEnglish, tiCounter;
    private TextInputEditText tiInput;
    private MaterialCardView tiInputCard, resetBtn;

    private Random random;
    private Phrase correctPhrase;
    private boolean answered = false;
    private String currentUnitKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_type_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tiEnglish = view.findViewById(R.id.tiEnglish);
        tiCounter = view.findViewById(R.id.tiCounter);
        tiInput = view.findViewById(R.id.tiInput);
        tiInputCard = view.findViewById(R.id.tiInputCard);
        resetBtn = view.findViewById(R.id.tibtnResetGame);

        currentUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", requireContext().MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");

        Unit currentUnit = UnitData.getUnit(currentUnitKey);

        phrases = new ArrayList<>();
        for (Phrase p : currentUnit.getPhrases()) {
            if (!p.tagalog.contains("_")) phrases.add(p);
        }

        random = new Random();
        Collections.shuffle(phrases);

        loadNextQuestion();

        tiInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (!answered) checkAnswer(s.toString().trim());
            }
        });

        resetBtn.setOnClickListener(v -> resetGame());
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions) {
            showSummary();
            return;
        }

        answered = false;
        tiInput.setText("");
        tiInput.setVisibility(View.VISIBLE);
        tiCounter.setVisibility(View.VISIBLE);

        tiInputCard.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
        tiEnglish.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));

        correctPhrase = phrases.get(questionIndex % phrases.size());
        SharedFunctions.animateTypeInNewTerm(tiEnglish, correctPhrase.english);

        tiCounter.setText((questionIndex + 1) + "/" + totalQuestions);
        resetBtn.setVisibility(View.GONE);
    }

    private void checkAnswer(String userInput) {
        if (userInput.isEmpty()) return;

        String normalizedInput = normalize(userInput);

        String tagalog = correctPhrase.tagalog;
        String[] accepted;

        if (tagalog.contains("/")) {
            accepted = tagalog.split("\\s*/\\s*"); // "A / B" -> ["A", "B"]
        } else {
            accepted = new String[]{ tagalog };
        }

        boolean isCorrect = false;
        for (String option : accepted) {
            if (normalizedInput.equals(normalize(option.trim()))) {
                isCorrect = true;
                break;
            }
        }

        if (isCorrect) {
            answered = true;
            SharedFunctions.playSound(requireContext(), R.raw.right);
            correctCount++;
            SharedFunctions.animateTypeInCorrect(tiInputCard, tiInput, () -> {
                tiEnglish.animate().alpha(0f).setDuration(250).withEndAction(() -> {
                    questionIndex++;
                    loadNextQuestion();
                });
            }, requireContext());
        }
    }

    /** Normalize strings for comparison */
    private String normalize(String s) {
        return s
                .toLowerCase()
                .replaceAll("[.!?]", "")     // ignore punctuation you already ignore
                .replaceAll("\\s+", " ")     // collapse multiple spaces
                .trim();
    }

    /** ---------------- Summary ---------------- */

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);
        SharedFunctions.saveBestScore(
                requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE),
                currentUnitKey + "_best_score_type_in",
                correctCount,
                totalQuestions
        );

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                tiEnglish,
                tiInput,
                tiCounter,
                "Completed",
                resetBtn
        );
    }

    /** ---------------- Reset ---------------- */

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        tiInput.setVisibility(View.VISIBLE);
        tiCounter.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.GONE);

        loadNextQuestion();
    }
}
