package com.example.dalawingo.ui.practice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dalawingo.R;
import com.example.dalawingo.ui.game.games.*;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PracticeFragment extends Fragment {

    private String currentUnitKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_practice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        currentUnitKey = prefs.getString("lastUnitKey", "lesson0_unit0");

        // Unit Questions card setup
        setupUnitQuestionsCard(view, prefs);

        // Individual game cards
        setupGameCard(view.findViewById(R.id.gameTitle1), "Self-paced Flashcards", "Mga Card", R.color.pastel_yellow, FlashcardsFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle2), "Say It Out Loud", "Magsalita", R.color.orange, SpeechFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle3), "True or False", "Tama o Mali", R.color.pastel_green, TrueFalseFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle4), "Multiple Choice", "Maraming Sagot", R.color.pastel_blue, MultipleChoiceFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle5), "Spot the Vowel Error", "Hanapin ang Mali", R.color.pastel_purple, SpotErrorFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle6), "Order the Sentence", "Ayos ng Salita", R.color.pastel_pink, OrderingFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle7), "Fill in the Blank", "Sagutin Mo", R.color.pastel_red, FillBlankFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle8), "Listen and Match", "Makinig Kayo", R.color.orange, ListeningFragment.class);
        setupGameCard(view.findViewById(R.id.gameTitle9), "Type in the Answer", "Isulat Mo", R.color.pastel_yellow, TypeInFragment.class);
        refreshScores();
    }

    private void setupUnitQuestionsCard(View view, SharedPreferences prefs) {
        MaterialCardView card = view.findViewById(R.id.cardUnitQuestions);
        if (card == null) return;

        if (areAllGamesPerfect(prefs, currentUnitKey)) {
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white));
            card.setOnClickListener(v -> openGame(UnitQuestionsFragment.class, R.color.pastel_red));
        } else {
            card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
            card.setOnClickListener(null);
        }
        updateUnitQuestionsLock(prefs);
    }

    private void setupGameCard(View container, String description, String title,
                               int colorRes, Class<? extends Fragment> fragmentClass) {
        if (container == null) return;

        TextView titleView = container.findViewById(R.id.gameCardTitleText);
        TextView descriptionView = container.findViewById(R.id.gameDescription);
        View playButton = container.findViewById(R.id.btnPlay);

        if (titleView != null) titleView.setText(title);
        if (descriptionView != null) descriptionView.setText(description);

        if (container instanceof com.google.android.material.card.MaterialCardView) {
            MaterialCardView card = (MaterialCardView) container;

            int defaultColor = ContextCompat.getColor(requireContext(), R.color.card_purple);
            int currentColor = card.getCardBackgroundColor().getDefaultColor();

            // Only replace if still default
            if (currentColor == defaultColor) {
                card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), colorRes));
            }
        }

        if (playButton != null) {
            playButton.setBackgroundTintList(requireContext().getColorStateList(colorRes));
            playButton.setOnClickListener(v ->
                    openGame(fragmentClass, colorRes)
            );
        }

        updateGameScore(container, title);
    }

    private void openGame(Class<? extends Fragment> gameClass, int colorRes) {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        prefs.edit()
                .putString("pending_game_class", gameClass.getName())
                .putInt("pending_game_color", colorRes)
                .apply();

        BottomNavigationView navView =
                requireActivity().findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_game);
    }

    public void refreshScores() {
        updateGameScore(requireView().findViewById(R.id.gameTitle1), "Flashcards");
        updateGameScore(requireView().findViewById(R.id.gameTitle2), "Say It Out Loud");
        updateGameScore(requireView().findViewById(R.id.gameTitle3), "True or False");
        updateGameScore(requireView().findViewById(R.id.gameTitle4), "Multiple Choice");
        updateGameScore(requireView().findViewById(R.id.gameTitle5), "Spot the Error");
        updateGameScore(requireView().findViewById(R.id.gameTitle6), "Sentence Ordering");
        updateGameScore(requireView().findViewById(R.id.gameTitle7), "Fill in the Blank");
        updateGameScore(requireView().findViewById(R.id.gameTitle8), "Listen and Match");
        updateGameScore(requireView().findViewById(R.id.gameTitle9), "Type In");

        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        updateUnitQuestionsLock(prefs);
    }

    private void updateGameScore(View container, String title) {
        if (container == null) return;

        TextView scoreView = container.findViewById(R.id.tvCounterScore);
        TextView scoreLabel = container.findViewById(R.id.scoreLabel);
        TextView completedStatus = container.findViewById(R.id.completedStatus);
        ImageView badgeView = container.findViewById(R.id.gameBadge);
        if (scoreView == null || scoreLabel == null || completedStatus == null || badgeView == null) return;

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String keyBase = getScoreKeyForTitle(title);
        int best = prefs.getInt(currentUnitKey + "_" + keyBase, 0);
        int total;

        if ("Flashcards".equals(title)) {
            total = com.example.dalawingo.data.UnitData
                    .getUnit(currentUnitKey)
                    .getPhrases()
                    .size();
            prefs.edit()
                    .putInt(currentUnitKey + "_" + keyBase + "_total", total)
                    .apply();
        } else {
            total = prefs.getInt(currentUnitKey + "_" + keyBase + "_total", 10);
        }

        // ✅ Type in / Fill in / Ordering → show Completed/Incomplete
        if ("Type In".equals(title) ||
                "Fill in the Blank".equals(title) ||
                "Say It Out Loud".equals(title) ||
                "Sentence Ordering".equals(title)) {

            scoreView.setVisibility(View.GONE);
            scoreLabel.setVisibility(View.GONE);
            completedStatus.setVisibility(View.VISIBLE);

            if (best >= total && total > 0) {
                completedStatus.setText("Completed");
                badgeView.setImageResource(R.drawable.ic_box_checked);
                badgeView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_green));
            } else {
                completedStatus.setText("Incomplete");
                badgeView.setImageResource(R.drawable.ic_box);
                badgeView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_red));
            }

        } else {
            // ✅ Other games → show Best Score + numeric
            scoreView.setVisibility(View.VISIBLE);
            scoreLabel.setVisibility(View.VISIBLE);
            completedStatus.setVisibility(View.GONE);

            scoreLabel.setText("Best Score");
            scoreView.setText(best + " / " + total);

            if (best >= total && total > 0) {
                badgeView.setImageResource(R.drawable.ic_box_checked);
                badgeView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_green));
            } else {
                badgeView.setImageResource(R.drawable.ic_box);
                badgeView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_red));
            }
        }
    }

    private String getScoreKeyForTitle(String title) {
        switch (title) {
            case "True or False": return "best_score_true_false";
            case "Flashcards": return "best_score_flashcards";
            case "Multiple Choice": return "best_score_multiple_choice";
            case "Sentence Ordering": return "best_score_ordering";
            case "Spot the Error": return "best_score_spot_error";
            case "Fill in the Blank": return "best_score_fill_blank";
            case "Type In": return "best_score_type_in";
            case "Listen and Match": return "best_score_listening";
            case "Say It Out Loud": return "best_score_speech";
            default: return "best_score_generic";
        }
    }

    private boolean areAllGamesPerfect(SharedPreferences prefs, String unitKey) {
        String[] gameKeys = {
                "best_score_true_false",
                "best_score_flashcards",
                "best_score_multiple_choice",
                "best_score_ordering",
                "best_score_spot_error",
                "best_score_fill_blank",
                "best_score_type_in",
                "best_score_speech",
                "best_score_listening"
        };
        //return true;
        for (String key : gameKeys) {
            int best = prefs.getInt(unitKey + "_" + key, 0);
            int total = prefs.getInt(unitKey + "_" + key + "_total", 10);
            if (total == 0 || best < total) return false;
        }
        return true;
    }

    private void updateUnitQuestionsLock(SharedPreferences prefs) {
        MaterialCardView cardUnitQuestions = requireView().findViewById(R.id.cardUnitQuestions);
        TextView unitTitle = requireView().findViewById(R.id.unitTitle);
        TextView unitScore = requireView().findViewById(R.id.unitScore);
        TextView unitScoreLabel = requireView().findViewById(R.id.unitScoreLabel);
        ImageView unitLockIcon = requireView().findViewById(R.id.unitLockIcon);
        ProgressBar gameProgress = requireView().findViewById(R.id.gameProgress);

        if (cardUnitQuestions == null) return;

        int totalQuestions = com.example.dalawingo.data.UnitData
                .getUnit(currentUnitKey).getQuestions().size();

        int correctCount = 0;
        for (int i = 0; i < totalQuestions; i++) {
            boolean correct = prefs.getBoolean("answer_correct_" + currentUnitKey + "_" + i, false);
            if (correct) correctCount++;
        }
        unitScore.setText(correctCount + " / " + totalQuestions);

        String[] gameKeys = {
                "best_score_true_false",
                "best_score_flashcards",
                "best_score_multiple_choice",
                "best_score_ordering",
                "best_score_spot_error",
                "best_score_fill_blank",
                "best_score_type_in",
                "best_score_speech",
                "best_score_listening"
        };

        int completed = 0;
        for (String key : gameKeys) {
            int best = prefs.getInt(currentUnitKey + "_" + key, 0);
            int total = prefs.getInt(currentUnitKey + "_" + key + "_total", 0);
            if (total > 0 && best > 0) completed++;
        }
        int progressPercent = (int) ((completed / (float) gameKeys.length) * 100f);
        gameProgress.setProgress(progressPercent);

        if (areAllGamesPerfect(prefs, currentUnitKey)) {
            cardUnitQuestions.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.pastel_red));
            unitTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
            unitScore.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            unitScoreLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
            unitLockIcon.setImageResource(R.drawable.ic_unlock);
            unitLockIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_blue));
            unitScore.setVisibility(View.VISIBLE);
            unitScoreLabel.setVisibility(View.VISIBLE);
            gameProgress.setVisibility(View.GONE);
            cardUnitQuestions.setOnClickListener(v ->
                    openGame(UnitQuestionsFragment.class, R.color.pastel_red));
        } else {
            cardUnitQuestions.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.grey));
            unitTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
            unitScore.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            unitScoreLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
            unitLockIcon.setImageResource(R.drawable.ic_lock);
            unitLockIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
            unitScore.setVisibility(View.GONE);
            unitScoreLabel.setVisibility(View.GONE);
            gameProgress.setVisibility(View.VISIBLE);
            cardUnitQuestions.setOnClickListener(null);
        }
    }
}
