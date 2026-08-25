package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dalawingo.R;
import com.example.dalawingo.data.Unit;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.data.UnitQuestion;
import com.example.dalawingo.ui.game.shared.Animations;
import com.example.dalawingo.ui.game.shared.SharedFunctions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

//import nl.dionsegijn.konfetti.xml.KonfettiView;

public class UnitQuestionsFragment extends Fragment {

    private boolean singleQuestionMode = false;
    private int questionIndex = 0;
    private List<UnitQuestion> currentQuestions;
    private String currentUnitKey = "lesson1_unit1";

    private SharedPreferences prefs;
    //private KonfettiView konfettiView;
    private List<Boolean> answeredCorrectly;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_unit_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        LinearLayout questionsContainer = view.findViewById(R.id.unitQuestionsContainer);
        TextView tvUnitInstruction = view.findViewById(R.id.tvUnitInstruction);
        Switch switchMode = view.findViewById(R.id.switchQuestionMode);
        ImageButton btnQuestionPrev = view.findViewById(R.id.btnBackQuestion);
        ImageButton btnQuestionNext = view.findViewById(R.id.btnNextQuestion);
        TextView tvQuestionCounter = view.findViewById(R.id.questionCounter);
        LinearLayout questionNavContainer = view.findViewById(R.id.questionNavContainer);
        //konfettiView = view.findViewById(R.id.konfettiAnim);

        // load current unit
        currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");
        Unit currentUnit = UnitData.getUnit(currentUnitKey);

        singleQuestionMode = prefs.getBoolean("practice_single_mode", false);
        currentQuestions = currentUnit.getQuestions();
        answeredCorrectly = new ArrayList<>();
        for (int i = 0; i < currentQuestions.size(); i++) {
            boolean savedCorrect = prefs.getBoolean("answer_correct_" + currentUnitKey + "_" + i, false);
            answeredCorrectly.add(savedCorrect);
        }

        questionIndex = prefs.getInt("practice_question_index_" + currentUnitKey, 0);
        questionIndex = clampQuestionIndex(questionIndex);

        switchMode.setChecked(singleQuestionMode);
        questionNavContainer.setVisibility(singleQuestionMode ? View.VISIBLE : View.GONE);
        tvUnitInstruction.setText(currentUnit.getInstructions());

        loadQuestions(questionsContainer, tvQuestionCounter);

        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedFunctions.playSound(requireContext(), R.raw.button_press);
            singleQuestionMode = isChecked;
            prefs.edit().putBoolean("practice_single_mode", isChecked).apply();
            questionNavContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (isChecked) {
                questionIndex = clampQuestionIndex(
                        prefs.getInt("practice_question_index_" + currentUnitKey, 0));
            }
            loadQuestions(questionsContainer, tvQuestionCounter);
        });

        btnQuestionNext.setOnClickListener(v -> {
            SharedFunctions.playSound(requireContext(), R.raw.button_press);
            if (!currentQuestions.isEmpty()) {
                questionIndex = (questionIndex + 1) % currentQuestions.size();
                showSingleQuestion(questionsContainer, tvQuestionCounter);
                saveQuestionIndexForUnit(currentUnitKey, questionIndex);
            }
        });

        btnQuestionPrev.setOnClickListener(v -> {
            SharedFunctions.playSound(requireContext(), R.raw.button_press);
            if (!currentQuestions.isEmpty()) {
                questionIndex = (questionIndex - 1 + currentQuestions.size()) % currentQuestions.size();
                showSingleQuestion(questionsContainer, tvQuestionCounter);
                saveQuestionIndexForUnit(currentUnitKey, questionIndex);
            }
        });
    }

    private void loadQuestions(ViewGroup container, @Nullable TextView tvQuestionCounter) {
        container.removeAllViews();
        if (currentQuestions == null || currentQuestions.isEmpty()) return;

        if (singleQuestionMode) {
            showSingleQuestion(container, tvQuestionCounter);
        } else {
            int number = 1;
            for (UnitQuestion q : currentQuestions) {
                addQuestionCard(container, q, number, number - 1);
                number++;
            }
        }
    }

    private void showSingleQuestion(ViewGroup container, @Nullable TextView tvQuestionCounter) {
        container.removeAllViews();
        if (currentQuestions == null || currentQuestions.isEmpty()) return;

        UnitQuestion q = currentQuestions.get(questionIndex);
        addQuestionCard(container, q, questionIndex + 1, questionIndex);

        if (tvQuestionCounter != null) {
            tvQuestionCounter.setText((questionIndex + 1) + "/" + currentQuestions.size());
        }
    }

    private void addQuestionCard(ViewGroup container, UnitQuestion q, int number, int questionIdx) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View qaCard = inflater.inflate(R.layout.card_unit_questions_answers, container, false);

        TextView tvQuestion = qaCard.findViewById(R.id.questionText);
        TextInputEditText etAnswer = qaCard.findViewById(R.id.UnitQuestionInput);
        Button btnSubmit = qaCard.findViewById(R.id.btnSubmitAnswer);
        MaterialCardView card = qaCard.findViewById(R.id.tiInputCard);

        tvQuestion.setText(number + ". " + q.question);

        // Load saved answer and correctness
        String savedAnswer = prefs.getString("answer_" + currentUnitKey + "_" + questionIdx, "");
        boolean savedCorrect = prefs.getBoolean("answer_correct_" + currentUnitKey + "_" + questionIdx, false);
        boolean savedAttempted = prefs.getBoolean("answer_attempted_" + currentUnitKey + "_" + questionIdx, false);

        etAnswer.setText(savedAnswer);

        if (savedAttempted) {
            if (savedCorrect) {
                card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_green));
            } else {
                card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_red));
            }
        }

        btnSubmit.setOnClickListener(v -> {
            String userInput = etAnswer.getText() != null ? etAnswer.getText().toString() : "";

            prefs.edit().putString("answer_" + currentUnitKey + "_" + questionIdx, userInput).apply();

            if (checkAnswer(userInput, q.answer)) {
                answeredCorrectly.set(questionIdx, true);
                prefs.edit()
                        .putBoolean("answer_correct_" + currentUnitKey + "_" + questionIdx, true)
                        .putBoolean("answer_attempted_" + currentUnitKey + "_" + questionIdx, true)
                        .apply();
                SharedFunctions.playSound(requireContext(), R.raw.right);
                SharedFunctions.animateUnitQuestionCorrect(card, requireContext());
                checkAllAnswered();
            } else {
                answeredCorrectly.set(questionIdx, false);
                prefs.edit()
                        .putBoolean("answer_correct_" + currentUnitKey + "_" + questionIdx, false)
                        .putBoolean("answer_attempted_" + currentUnitKey + "_" + questionIdx, true)
                        .apply();
                SharedFunctions.playSound(requireContext(), R.raw.wrong);
                SharedFunctions.animateUnitQuestionIncorrect(card, requireContext());
            }
        });

        container.addView(qaCard);
    }

    /** Celebration when all correct */
    private void checkAllAnswered() {
        if (answeredCorrectly.contains(false)) return;

        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        prefs.edit().putBoolean("unit_complete_" + currentUnitKey, true).apply();

        //Animations.playKonfetti(konfettiView, requireContext());

        Toast.makeText(requireContext(), "New Unit Unlocked ✨", Toast.LENGTH_LONG).show();

        // 🎆 Fireworks (Lottie)
        LottieAnimationView celebrateAnim = requireActivity().findViewById(R.id.confettiAnim);
        Animations.playFireworks(celebrateAnim);
    }

    private boolean checkAnswer(String input, String expected) {
        if (TextUtils.isEmpty(expected)) return false;

        String cleanInput = normalize(input);

        // Split expected answers by "/" → multiple acceptable sets
        String[] possibleAnswers = expected.split("/");

        for (String possible : possibleAnswers) {
            String cleanExpected = normalize(possible);

            // Each expected set is broken into words
            String[] expectedWords = cleanExpected.split("\\s+");

            boolean allMatch = true;
            for (String word : expectedWords) {
                if (!cleanInput.contains(word)) {
                    allMatch = false;
                    break;
                }
            }

            // ✅ If any set matches fully, answer is correct
            if (allMatch) return true;
        }

        return false;
    }


    private String normalize(String s) {
        if (s == null) return "";
        return s.replaceAll("\\(.*?\\)", "")   // remove hints like (job)
                .replaceAll("[\\p{Punct}_]", "") // remove punctuation/underscores
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase();
    }

    private void saveQuestionIndexForUnit(String unitKey, int index) {
        prefs.edit().putInt("practice_question_index_" + unitKey, index).apply();
    }

    private int clampQuestionIndex(int idx) {
        if (currentQuestions == null || currentQuestions.isEmpty()) return 0;
        return Math.max(0, Math.min(idx, currentQuestions.size() - 1));
    }
}
