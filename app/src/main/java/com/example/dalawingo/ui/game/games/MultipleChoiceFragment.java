package com.example.dalawingo.ui.game.games;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipleChoiceFragment extends Fragment {

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private MaterialCardView[] optionCards;
    private TextView[] optionTexts;
    private TextView mcCounter, mcTagalog;
    private MaterialCardView resetBtn;

    private Random random;
    private Phrase correctPhrase;

    private SharedPreferences prefs;
    private String currentUnitKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_multiple_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mcCounter = view.findViewById(R.id.mcCounter);
        mcTagalog = view.findViewById(R.id.mcTagalog);
        resetBtn = view.findViewById(R.id.mcbtnResetGame);

        optionCards = new MaterialCardView[]{
                view.findViewById(R.id.mcOption1),
                view.findViewById(R.id.mcOption2),
                view.findViewById(R.id.mcOption3),
                view.findViewById(R.id.mcOption4)
        };

        optionTexts = new TextView[]{
                view.findViewById(R.id.mcOption1Text),
                view.findViewById(R.id.mcOption2Text),
                view.findViewById(R.id.mcOption3Text),
                view.findViewById(R.id.mcOption4Text)
        };

        prefs = requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE);
        currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");

        resetBtn.setOnClickListener(v -> resetGame());

        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = new ArrayList<>(currentUnit.getPhrases());

        random = new Random();
        Collections.shuffle(phrases);

        loadNextQuestion();
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions) {
            showSummary();
            return;
        }

        correctPhrase = phrases.get(questionIndex % phrases.size());
        SharedFunctions.animateNewWord(mcTagalog, correctPhrase.tagalog);

        // Generate options
        List<String> options = new ArrayList<>();
        options.add(correctPhrase.english);
        while (options.size() < 4) {
            Phrase p = phrases.get(random.nextInt(phrases.size()));
            if (!options.contains(p.english)) options.add(p.english);
        }
        Collections.shuffle(options);

        for (int i = 0; i < 4; i++) {
            resetOptionStyle(optionCards[i], optionTexts[i]);
            optionTexts[i].setText(options.get(i));

            int finalI = i;
            optionCards[i].setOnClickListener(v ->
                    checkAnswer(options.get(finalI), optionCards[finalI])
            );
        }

        mcCounter.setText((questionIndex + 1) + "/" + totalQuestions);
        resetBtn.setVisibility(View.GONE);
    }

    private void checkAnswer(String chosen, MaterialCardView pressedCard) {
        boolean correct = chosen.equals(correctPhrase.english);

        for (MaterialCardView card : optionCards) card.setEnabled(false);

        if (correct) {
            SharedFunctions.playSound(requireContext(), R.raw.right);
            SharedFunctions.animateMCCorrect(requireView(), pressedCard);
            correctCount++;
        } else {
            SharedFunctions.playSound(requireContext(), R.raw.wrong);
            SharedFunctions.animateMCWrong(requireView(), pressedCard, optionCards, optionTexts, correctPhrase.english);
        }

        pressedCard.postDelayed(() -> {
            questionIndex++;
            loadNextQuestion();
        }, 1500);
    }

    /** ---------------- UI Helpers ---------------- */

    private void resetOptionStyle(MaterialCardView card, TextView text) {
        card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
        card.setCardBackgroundColor(Color.WHITE);
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        card.setEnabled(true);
        card.setVisibility(View.VISIBLE);
    }

    /** ---------------- Summary + Reset ---------------- */

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                mcTagalog,
                mcCounter,
                optionTexts[0],
                "Score: " + correctCount + " / " + totalQuestions,
                resetBtn
        );

        resetOptionStyle(optionCards[0], optionTexts[0]);

        optionCards[1].setVisibility(View.GONE);
        optionCards[2].setVisibility(View.GONE);
        optionCards[3].setVisibility(View.GONE);

        SharedFunctions.saveBestScore(prefs,
                currentUnitKey + "_best_score_multiple_choice",
                correctCount,
                totalQuestions);

    }

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        for (MaterialCardView card : optionCards) {
            card.setVisibility(View.VISIBLE);
        }

        ViewGroup parent = (ViewGroup) mcTagalog.getParent();
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            if (child instanceof TextView && child != mcTagalog && child != mcCounter) {
                parent.removeView(child);
            }
        }
        mcCounter.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.GONE);
        loadNextQuestion();
    }
}
