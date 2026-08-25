package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class TrueFalseFragment extends Fragment {

    private static final String GAME_KEY = "best_score_true_false";

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private TextView tfTagalog, tfEnglish, tfCounter;
    private ImageView btnTrue, btnFalse;
    private MaterialCardView cardTrue, cardFalse, englishCard;
    private MaterialCardView resetBtn;
    private LinearLayout tfBtnRow;

    private Random random;
    private boolean currentAnswerCorrect;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_true_false, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        englishCard = view.findViewById(R.id.tfEnglishCard);
        tfTagalog = view.findViewById(R.id.tfTagalog);
        tfEnglish = view.findViewById(R.id.tfEnglish);
        tfCounter = view.findViewById(R.id.tfCounter);
        resetBtn = view.findViewById(R.id.tfbtnResetGame);

        cardTrue = (MaterialCardView) view.findViewById(R.id.btnTrue).getParent();
        cardFalse = (MaterialCardView) view.findViewById(R.id.btnFalse).getParent();
        btnTrue = view.findViewById(R.id.btnTrue);
        btnFalse = view.findViewById(R.id.btnFalse);
        tfBtnRow = view.findViewById(R.id.tfBtnRow);

        resetBtn.setOnClickListener(v -> resetGame());

        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");
        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = new ArrayList<>(currentUnit.getPhrases());

        random = new Random();
        Collections.shuffle(phrases);

        loadNextQuestion();

        btnTrue.setOnClickListener(v -> checkAnswer(true, cardTrue));
        btnFalse.setOnClickListener(v -> checkAnswer(false, cardFalse));
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions) {
            showSummary();
            return;
        }

        Phrase correctPhrase = phrases.get(questionIndex % phrases.size());
        SharedFunctions.animateTFNewTerm(tfTagalog, correctPhrase.tagalog);

        boolean showCorrect = random.nextBoolean();
        Phrase chosenPhrase = showCorrect ? correctPhrase :
                phrases.get(random.nextInt(phrases.size()));

        SharedFunctions.animateTFNewTerm(tfEnglish, chosenPhrase.english);

        currentAnswerCorrect = showCorrect;
        tfCounter.setText((questionIndex + 1) + "/" + totalQuestions);

        resetBtn.setVisibility(View.GONE);
    }

    private void checkAnswer(boolean userSaidTrue, MaterialCardView pressedCard) {
        btnTrue.setEnabled(false);
        btnFalse.setEnabled(false);

        boolean userCorrect = (userSaidTrue == currentAnswerCorrect);

        MaterialCardView highlightCard = currentAnswerCorrect ? cardTrue : cardFalse;
        SharedFunctions.animateTFCardFeedback(highlightCard,
                currentAnswerCorrect ? R.color.pastel_green : R.color.pastel_pink,
                requireContext());

        if (userCorrect) {
            SharedFunctions.playSound(requireContext(), R.raw.right);
            correctCount++;
        } else {
            SharedFunctions.playSound(requireContext(), R.raw.wrong);
            Phrase correctPhrase = phrases.get(questionIndex % phrases.size());
            SharedFunctions.animateTFEnglishCorrection(englishCard, tfEnglish, correctPhrase.english,
                    currentAnswerCorrect ? R.color.pastel_green : R.color.pastel_pink,
                    requireContext());
        }

        questionIndex++;
        pressedCard.postDelayed(() -> {
            loadNextQuestion();
            btnTrue.setEnabled(true);
            btnFalse.setEnabled(true);
        }, 1500);
    }

    /** ---------------- Summary + Reset ---------------- */

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                tfTagalog,
                tfCounter,
                tfEnglish,
                "Score: " + correctCount + " / " + totalQuestions,
                resetBtn
        );

        tfBtnRow.setVisibility(View.GONE);

        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");
        SharedFunctions.saveBestScore(prefs, currentUnitKey + "_" + GAME_KEY, correctCount, totalQuestions);

    }

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        tfBtnRow.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.GONE);

        loadNextQuestion();
    }
}
