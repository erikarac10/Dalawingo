package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OrderingFragment extends Fragment {

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int totalQuestions;

    private FrameLayout wordSpawnArea;
    private TextView sentenceEnglish, sentenceCounter;
    private MaterialCardView resetBtn;

    private Random random;
    private String[] tokens;

    private String currentUnitKey;
    private final List<TextView> draggableWords = new ArrayList<>();
    private final List<TextView> tappedWords = new ArrayList<>();
    private boolean isAnimating = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_sentence_ordering, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wordSpawnArea = view.findViewById(R.id.wordSpawnArea);
        sentenceEnglish = view.findViewById(R.id.orderingEnglish);
        resetBtn = view.findViewById(R.id.orderingbtnResetGame);
        sentenceCounter = view.findViewById(R.id.orderingCounter);

        resetBtn.setOnClickListener(v -> resetGame());

        currentUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");

        Unit currentUnit = UnitData.getUnit(currentUnitKey);

        phrases = new ArrayList<>();
        for (Phrase p : currentUnit.getPhrases()) {
            String raw = p.tagalog.trim();
            if (raw.split(" ").length >= 2 && !raw.contains("_")) {
                phrases.add(p);
            }
        }

        Collections.shuffle(phrases);
        if (phrases.size() > 10) {
            phrases = phrases.subList(0, 10);
        }
        totalQuestions = phrases.size();

        random = new Random();
        loadNextQuestion();
    }

    /** ---------------- Game Flow ---------------- */

    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions) {
            showSummary();
            return;
        }

        Phrase phrase = phrases.get(questionIndex % phrases.size());
        String rawPhrase = phrase.tagalog.trim();

        // ❌ skip phrases with underscores
        if (rawPhrase.contains("_") || rawPhrase.contains("/")) {
            questionIndex++;
            loadNextQuestion();
            return;
        }

        wordSpawnArea.removeAllViews();
        draggableWords.clear();
        tappedWords.clear();

        SharedFunctions.animateOrderingNewWord(sentenceEnglish, phrase.english);
        sentenceCounter.setText((questionIndex + 1) + "/" + totalQuestions);

        // ✅ clean + lowercase tokens
        tokens = rawPhrase.split("\\s+");
        List<String> shuffled = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (String t : tokens) {
            // skip words with underscores
            if (t.contains("_")) continue;

            // clean: remove punctuation and lowercase
            String cleaned = t.replaceAll("[.!?,]", "").toLowerCase();

            // add only once
            if (seen.add(cleaned)) {
                shuffled.add(cleaned);
            }
        }

        Collections.shuffle(shuffled);
        spawnDraggableWords(shuffled);

        resetBtn.setVisibility(View.GONE);
    }


    /** ---------------- UI Helpers ---------------- */

    private boolean isWordDraggable(String token) {
        if (token.contains("_")) return false;
        if (token.startsWith("__(") && token.endsWith(")__")) return false;
        if (token.equals("/")) return false;
        return !token.matches("^[.,!?;:]+$");
    }

    private void spawnDraggableWords(List<String> words) {
        wordSpawnArea.post(() -> {
            int areaWidth = wordSpawnArea.getWidth();
            int areaHeight = wordSpawnArea.getHeight();
            int padding = 32;

            for (String word : words) {
                TextView tv = createDraggableWord(word);

                int maxX = areaWidth - 200;
                int maxY = areaHeight - 120;
                float x = random.nextInt(Math.max(1, maxX - padding)) + padding;
                float y = random.nextInt(Math.max(1, maxY - padding)) + padding;

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                tv.setLayoutParams(lp);
                tv.setX(x);
                tv.setY(y);

                makeDraggable(tv);
                wordSpawnArea.addView(tv);
                draggableWords.add(tv);

                SharedFunctions.animateOrderingSpawn(tv);
            }
        });
    }

    private TextView createDraggableWord(String word) {
        // Strip punctuation from the word
        String cleaned = word.replaceAll("[.,!?;:]", "").toLowerCase();

        TextView tv = new TextView(requireContext());
        tv.setText(cleaned); // ✅ lowercase + punctuation removed
        tv.setTextSize(22);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(20, 12, 20, 12);
        tv.setSingleLine(true);

        tv.setOnClickListener(v -> {
            if (isAnimating) return;
            if (!tappedWords.contains(tv)) tappedWords.add(tv);
            checkCompletion();
        });

        return tv;
    }

    /** ---------------- Drag Handling ---------------- */

    private void makeDraggable(TextView tv) {
        tv.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isAnimating) return false;

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        if (!tappedWords.contains(tv)) tappedWords.add(tv);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float newX = event.getRawX() + dX;
                        float newY = event.getRawY() + dY;

                        int maxX = wordSpawnArea.getWidth() - v.getWidth();
                        int maxY = wordSpawnArea.getHeight() - v.getHeight();

                        newX = Math.max(0, Math.min(newX, maxX));
                        newY = Math.max(0, Math.min(newY, maxY));

                        v.setX(newX);
                        v.setY(newY);
                        return true;

                    case MotionEvent.ACTION_UP:
                        checkCompletion();
                        return true;
                }
                return false;
            }
        });
    }

    /** ---------------- Completion ---------------- */

    private void checkCompletion() {
        List<TextView> ordered = new ArrayList<>(draggableWords);
        ordered.sort(Comparator.comparingDouble(tv -> tv.getX())); // sort by left edge

        // thresholds
        final float MAX_GAP = 80f;   // max allowed gap between words
        final float MAX_DY = 60f;    // max vertical offset allowed

        float lastRight = -1;
        float lastY = -1;

        for (TextView tv : ordered) {
            float leftX = tv.getX();
            float rightX = tv.getX() + tv.getWidth();
            float centerY = tv.getY() + tv.getHeight() / 2f;

            if (lastRight >= 0) {
                float gap = leftX - lastRight;
                float dy = Math.abs(centerY - lastY);

                // 🔴 Require words to connect like a chain
                if (gap > MAX_GAP || dy > MAX_DY) {
                    return; // chain broken → not correct
                }
            }

            lastRight = rightX;
            lastY = centerY;
        }

        // build attempt
        StringBuilder attempt = new StringBuilder();
        for (TextView tv : ordered) {
            attempt.append(tv.getText().toString()).append(" ");
        }

        // build correct
        StringBuilder correct = new StringBuilder();
        for (String token : tokens) {
            if (isWordDraggable(token)) {
                String cleaned = token.replaceAll("[.,!?;:]", "").toLowerCase();
                correct.append(cleaned).append(" ");
            }
        }

        if (attempt.toString().trim().equalsIgnoreCase(correct.toString().trim())) {
            isAnimating = true;
            SharedFunctions.playSound(requireContext(), R.raw.right);
            SharedFunctions.animateOrderingCorrect(ordered);

            questionIndex++;
            wordSpawnArea.postDelayed(() -> {
                isAnimating = false;
                loadNextQuestion();
            }, 1200);
        }
    }


    /** ---------------- Summary + Reset ---------------- */

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.saveBestScore(
                requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE),
                currentUnitKey + "_best_score_ordering",
                questionIndex,
                totalQuestions
        );

        wordSpawnArea.removeAllViews();

        TextView title = new TextView(requireContext());

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                title,
                sentenceCounter,
                sentenceEnglish,
                "Completed",
                resetBtn
        );
        // ⬅️ Center in the middle of the FrameLayout
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        title.setLayoutParams(lp);
        wordSpawnArea.addView(title);

    }

    private void resetGame() {
        questionIndex = 0;
        sentenceCounter.setVisibility(View.VISIBLE);
        Collections.shuffle(phrases);
        wordSpawnArea.removeAllViews();
        resetBtn.setVisibility(View.GONE);
        loadNextQuestion();
    }
}
