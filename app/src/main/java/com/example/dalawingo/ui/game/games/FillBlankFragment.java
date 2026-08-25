package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FillBlankFragment extends Fragment {

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private int totalQuestions;

    private MaterialCardView fbCard, btnReset, fbEnglishCard;
    private TextView fbCardText1, fbCardText2, fbCardText3, fbEnglish, fbCounter;
    private View fbUnderline;
    private TextInputEditText fbInput;
    private ScrollView practiceScroll;

    private Random random;
    private Phrase correctPhrase;
    private String missingWord;
    private String[] missingAlternatives;

    private String currentUnitKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fill_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        currentUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");
        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = filterPhrases(currentUnit.getPhrases());
        totalQuestions = Math.min(10, phrases.size());

        random = new Random();
        Collections.shuffle(phrases);

        fbInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (fbInput != null) fbInput.setSelection(s.length()); // <-- key line
                handleTyping(s.toString().trim());
            }
        });

        fbInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && practiceScroll != null) {
                fbInput.post(() -> practiceScroll.smoothScrollTo(0, fbInput.getBottom()));
            }
        });

        btnReset.setOnClickListener(v -> resetGame());

        loadNextQuestion();
    }

    /** ---------------- Setup ---------------- */

    private void initViews(View view) {
        fbCard = view.findViewById(R.id.fbCard);
        fbCardText1 = view.findViewById(R.id.fbCardText1);
        fbCardText2 = view.findViewById(R.id.fbCardText2);
        //fbCardText3 = view.findViewById(R.id.fbCardText3);
        fbEnglish = view.findViewById(R.id.fbEnglish);
        fbCounter = view.findViewById(R.id.fbCounter);
        fbInput = view.findViewById(R.id.fbInput);
        fbUnderline = view.findViewById(R.id.fbCardUnderline);
        practiceScroll = view.findViewById(R.id.practiceScroll);
        btnReset = view.findViewById(R.id.fbbtnResetGame);
        fbEnglishCard = view.findViewById(R.id.fbEnglishCard);
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions || questionIndex >= phrases.size()) {
            showSummary();
            return;
        }
        float d = getResources().getDisplayMetrics().density;
        fbEnglishCard.setStrokeWidth((int) (2 * d));
        fbEnglishCard.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));

        correctPhrase = phrases.get(questionIndex);
        String[] words = correctPhrase.tagalog.split("\\s+");
        int blankIndex = random.nextInt(words.length);

        missingWord = words[blankIndex];
        missingAlternatives = missingWord.split("\\s*/\\s*");

        resetCardVisuals();

        // dynamic sizing
        int minWidthPx = (int) (80 * getResources().getDisplayMetrics().density);
        int maxWidthPx = (int) (240 * getResources().getDisplayMetrics().density);
        int charWidth = (int) (fbInput.getTextSize() * 0.65f);

        int longestAlt = 0;
        for (String alt : missingAlternatives) {
            longestAlt = Math.max(longestAlt, alt.trim().length());
        }

        int targetWidth = Math.min(maxWidthPx, Math.max(minWidthPx, longestAlt * charWidth));

        fbInput.getLayoutParams().width = targetWidth;
        fbUnderline.getLayoutParams().width = targetWidth;
        fbInput.requestLayout();
        fbUnderline.requestLayout();

        // build context text
        StringBuilder left = new StringBuilder();
        for (int i = 0; i < blankIndex; i++) left.append(words[i]).append(" ");

        StringBuilder right = new StringBuilder();
        for (int i = blankIndex + 1; i < words.length; i++) right.append(" ").append(words[i]);

        fbCardText1.setVisibility(View.VISIBLE);
        fbCardText2.setVisibility(View.VISIBLE);

        fbCardText1.setText(left.toString().trim());
        fbCardText2.setText(right.toString().trim());

        // english + counter
        fbEnglish.setVisibility(View.VISIBLE);
        fbInput.setVisibility(View.VISIBLE);
        fbEnglish.setText(correctPhrase.english);
        fbCounter.setText((questionIndex + 1) + "/" + totalQuestions);

        // fade-in
        fbCard.setAlpha(0f);
        fbCard.animate().alpha(1f).setDuration(400).start();

        btnReset.setVisibility(View.GONE);
    }

    private void handleTyping(String userInput) {
        if (userInput.isEmpty()) return;

        String normalizedInput = userInput
                .replaceAll("[.!?]", "")
                .trim()
                .toLowerCase();

        for (String alt : missingAlternatives) {
            String normalizedAlt = alt
                    .replaceAll("[.!?]", "")
                    .trim()
                    .toLowerCase();

            if (normalizedInput.equals(normalizedAlt)) {
                SharedFunctions.playSound(requireContext(), R.raw.right);
                correctCount++;

                // ✅ Freeze typing but KEEP focus/cursor so it doesn't jump to Back button
                fbInput.setKeyListener(null);
                fbInput.setCursorVisible(true);
                fbInput.requestFocus();
                fbInput.setSelection(fbInput.getText() != null ? fbInput.getText().length() : 0);

                // underline + animation
                fbUnderline.setVisibility(View.INVISIBLE);
                SharedFunctions.animateFillBlankCorrect(requireView(), fbCard, fbUnderline);

                nextAfterDelay();
                return;
            }
        }
    }


    /** ---------------- UI Helpers ---------------- */

    private void resetCardVisuals() {
        float d = getResources().getDisplayMetrics().density;

        fbCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pastel_red));

        // ✅ restore stroke so correct “green pop” can be visible again
        fbCard.setStrokeWidth((int) (4 * d));
        fbCard.setStrokeColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));

        fbCardText1.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        fbCardText2.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));

        fbInput.setText("");
        fbUnderline.setVisibility(View.VISIBLE);
        fbInput.setEnabled(true);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(fbInput.getWindowToken(), 0);
        }
    }

    private List<Phrase> filterPhrases(List<Phrase> inputPhrases) {
        List<Phrase> filtered = new ArrayList<>();
        for (Phrase p : inputPhrases) {
            String tagalog = p.tagalog.trim();
            if (tagalog.split("\\s+").length < 2) continue;
            if (tagalog.contains("_")) continue;
            filtered.add(p);
        }
        return filtered;
    }

    private void nextAfterDelay() {
        fbCard.postDelayed(() -> {
            questionIndex++;
            loadNextQuestion();
        }, 1500);
    }

    /** ---------------- Summary ---------------- */

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        // remove border
        fbCard.setStrokeWidth(0);
        fbCard.setStrokeColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));

        fbEnglishCard.setStrokeWidth(0);
        fbEnglishCard.setStrokeColor(
                ContextCompat.getColor(requireContext(), R.color.pastel_grey)
        );

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        prefs.edit()
                .putInt(currentUnitKey + "_best_score_fill_blank_total", totalQuestions)
                .apply();

        SharedFunctions.saveBestScore(
                requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE),
                currentUnitKey + "_best_score_fill_blank",
                correctCount,
                totalQuestions
        );

        fbCardText1.setText("  ⭐ Magaling ⭐");
        fbCardText2.setVisibility(View.GONE);
        //if (fbCardText3 != null) fbCardText3.setVisibility(View.GONE);

        fbInput.setVisibility(View.GONE);

        // 🎆 Fireworks (Lottie)
        LottieAnimationView celebrateAnim = requireActivity().findViewById(R.id.fireworkAnim);
        Animations.playFireworks(celebrateAnim);

        fbInput.setVisibility(View.GONE);
        fbUnderline.setVisibility(View.GONE);

        fbEnglish.setText("Completed");
        fbCounter.setVisibility(View.GONE);

        hideKeyboard();

        fbCard.setScaleX(0.8f);
        fbCard.setScaleY(0.8f);
        fbCard.setAlpha(0f);
        fbCard.animate()
                .scaleX(1f).scaleY(1f)
                .alpha(1f)
                .setDuration(500)
                .start();

        btnReset.setVisibility(View.VISIBLE);
    }

    /** ---------------- Reset ---------------- */

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        fbCardText2.setVisibility(View.VISIBLE);
        //fbCardText3.setVisibility(View.VISIBLE);
        fbInput.setVisibility(View.VISIBLE);
        fbCounter.setVisibility(View.GONE);
        fbEnglish.setVisibility(View.VISIBLE);
        fbInput.setVisibility(View.VISIBLE);
        fbUnderline.setVisibility(View.VISIBLE);

        btnReset.setVisibility(View.GONE);

        loadNextQuestion();
    }
}
