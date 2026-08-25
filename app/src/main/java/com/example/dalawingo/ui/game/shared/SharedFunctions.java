package com.example.dalawingo.ui.game.shared;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dalawingo.R;
import com.example.dalawingo.SoundPlayer;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SharedFunctions {

    /** Play a sound effect */
    public static void playSound(Context context, int soundResId) {
        SoundPlayer.play(context, soundResId);
    }

    /** Animate stroke color change for a MaterialCardView */
    public static void animateStrokeColor(com.google.android.material.card.MaterialCardView card, int targetColor) {
        int currentColor = card.getStrokeColor();
        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, targetColor);
        colorAnim.setDuration(300);
        colorAnim.addUpdateListener(anim -> card.setStrokeColor((int) anim.getAnimatedValue()));
        colorAnim.start();
    }

    /** Save best score in SharedPreferences */
    public static void saveBestScore(SharedPreferences prefs, String keyBase, int score, int total) {
        int prevBest = prefs.getInt(keyBase, 0);

        SharedPreferences.Editor editor = prefs.edit();
        if (score > prevBest) {
            editor.putInt(keyBase, score);
        }
        editor.putInt(keyBase + "_total", total);
        editor.apply();
    }

    /** ✅ Correct answer animation for Fill-in-the-Blank */
    public static void animateFillBlankCorrect(View root, MaterialCardView card, View underline) {
        int green = ContextCompat.getColor(root.getContext(), R.color.pastel_green);
        card.setStrokeColor(green);

        card.animate()
                .scaleX(1.04f).scaleY(1.04f)
                .setDuration(150)
                .withEndAction(() -> card.animate().scaleX(1f).scaleY(1f).setDuration(150))
                .start();

        underline.animate()
                .alpha(0f).setDuration(300)
                .withEndAction(() -> underline.setVisibility(View.INVISIBLE))
                .start();
    }

    /** ✨ Multiple Choice: animate new word appearing */
    public static void animateNewWord(TextView target, String text) {
        target.setAlpha(0f);
        target.setScaleX(0.8f);
        target.setScaleY(0.8f);
        target.setText(text);

        target.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(400)
                .start();
    }

    /** ✅ Multiple Choice: correct option pulse */
    public static void animateMCCorrect(View root, MaterialCardView card) {
        int green = ContextCompat.getColor(root.getContext(), R.color.pastel_green);
        int grey = ContextCompat.getColor(root.getContext(), R.color.pastel_grey);

        card.setCardBackgroundColor(green);
        card.setStrokeColor(green);
        card.setStrokeWidth(4);

        card.setScaleX(0.9f);
        card.setScaleY(0.9f);
        card.animate()
                .scaleX(1.01f)
                .scaleY(1.01f)
                .setDuration(150)
                .withEndAction(() -> card.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction(() -> card.postDelayed(() -> {
                            card.setStrokeColor(grey);
                            card.setCardBackgroundColor(ContextCompat.getColor(root.getContext(), R.color.white));
                        }, 1000)))
                .start();
    }

    /** ❌ Multiple Choice: wrong answer shake + highlight correct */
    public static void animateMCWrong(View root, MaterialCardView wrongCard,
                                      MaterialCardView[] optionCards, TextView[] optionTexts,
                                      String correctAnswer) {
        int red = ContextCompat.getColor(root.getContext(), R.color.pastel_pink);
        wrongCard.setStrokeColor(red);

        for (int i = 0; i < optionTexts.length; i++) {
            if (optionTexts[i].getText().toString().equals(correctAnswer)) {
                MaterialCardView correctCard = optionCards[i];
                correctCard.animate()
                        .translationXBy(15f).setDuration(50)
                        .withEndAction(() -> correctCard.animate()
                                .translationXBy(-30f).setDuration(50)
                                .withEndAction(() -> correctCard.animate()
                                        .translationXBy(15f).setDuration(50)))
                        .start();
                break;
            }
        }
    }

    /** ⭐ Multiple Choice: summary views fade + scale */
    public static void animateMCSummary(TextView title, TextView counter, TextView summary) {
        title.setAlpha(0f);
        title.setScaleX(0.8f);
        title.setScaleY(0.8f);
        title.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).start();

        counter.setTranslationY(50f);
        counter.setAlpha(0f);
        counter.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(700)
                .setStartDelay(300)
                .start();

        summary.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setStartDelay(600)
                .withEndAction(() -> summary.animate()
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(150)
                        .withEndAction(() -> summary.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)))
                .start();
    }
    /** ✨ Ordering: fade+scale new word (English prompt) */
    public static void animateOrderingNewWord(TextView target, String text) {
        target.setAlpha(0f);
        target.setScaleX(0.8f);
        target.setScaleY(0.8f);
        target.setText(text);
        target.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(400).start();
    }

    /** 🪄 Ordering: spawn draggable word bubble */
    public static void animateOrderingSpawn(View word) {
        word.setScaleX(0f);
        word.setScaleY(0f);
        word.animate().scaleX(1f).scaleY(1f).setDuration(300).start();
    }

    /** ✅ Ordering: correct sequence pulse */
    public static void animateOrderingCorrect(List<TextView> words) {
        for (TextView tv : words) {
            tv.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150)
                    .withEndAction(() -> tv.animate().scaleX(1f).scaleY(1f).setDuration(150))
                    .start();
        }
    }

    /** 🎯 SpotError: pop animation (correct guess) */
    public static void animateSpotErrorPop(View v) {
        v.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(150))
                .start();
    }

    /** 👀 SpotError: fade in the word */
    public static void animateSpotErrorFadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(400).start();
    }

    /** ❌ SpotError: shake wrong guess */
    public static void animateSpotErrorShake(View v) {
        Animation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(5));
        v.startAnimation(shake);
    }

    /** ✍️ True/False: animate new term fade-slide in */
    public static void animateTFNewTerm(TextView textView, String newText) {
        textView.animate().cancel();
        textView.animate()
                .alpha(0f)
                .translationY(20f)
                .setDuration(150)
                .withEndAction(() -> {
                    textView.setText(newText);
                    textView.setTranslationY(-20f);
                    textView.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /** ✅ True/False: animate card feedback pulse */
    public static void animateTFCardFeedback(MaterialCardView card, int colorResId, Context context) {
        int strokeColor = ContextCompat.getColor(context, colorResId);
        int originalStroke = card.getStrokeColor();

        card.animate()
                .scaleX(1.02f).scaleY(1.02f).setDuration(180)
                .withEndAction(() -> card.animate().scaleX(1f).scaleY(1f).setDuration(180))
                .start();

        card.setStrokeColor(strokeColor);
        card.setAlpha(0.9f);
        card.animate()
                .alpha(1f).setDuration(400).setStartDelay(100)
                .withEndAction(() -> card.setStrokeColor(originalStroke))
                .start();
    }

    /** ❌ True/False: animate English correction shake */
    public static void animateTFEnglishCorrection(MaterialCardView englishCard, TextView tvEnglish,
                                                  String correctEnglish, int colorResId, Context context) {
        int strokeColor = ContextCompat.getColor(context, colorResId);
        int originalStroke = englishCard.getStrokeColor();

        tvEnglish.setText(correctEnglish);
        englishCard.setStrokeColor(strokeColor);

        englishCard.animate()
                .translationXBy(15f).setDuration(50)
                .withEndAction(() -> englishCard.animate()
                        .translationXBy(-30f).setDuration(50)
                        .withEndAction(() -> englishCard.animate()
                                .translationXBy(15f).setDuration(50)))
                .start();

        englishCard.postDelayed(() -> englishCard.setStrokeColor(originalStroke), 1000);
    }

    /** ✍️ Type-In: animate new English term fade-slide in */
    public static void animateTypeInNewTerm(TextView textView, String newText) {
        textView.animate().cancel();
        textView.animate()
                .alpha(0f)
                .translationY(20f)
                .setDuration(150)
                .withEndAction(() -> {
                    textView.setText(newText);
                    textView.setTranslationY(-20f);
                    textView.animate()
                            .alpha(1f)
                            .translationY(0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /** ✅ Type-In: animate correct input pulse and move to next */
    public static void animateTypeInCorrect(MaterialCardView inputCard,
                                            View inputField,
                                            Runnable onNext,
                                            Context context) {
        int green = ContextCompat.getColor(context, R.color.pastel_green);
        inputCard.setStrokeColor(green);

        inputField.animate()
                .scaleX(1.15f).scaleY(1.15f)
                .setDuration(150)
                .withEndAction(() -> inputField.animate().scaleX(1f).scaleY(1f).setDuration(150))
                .start();

        inputField.postDelayed(onNext, 1000);
    }

    /** ✅ Correct answer pulse (Unit Questions) */
    public static void animateUnitQuestionCorrect(MaterialCardView card, Context context) {
        int green = ContextCompat.getColor(context, R.color.pastel_green);
        card.setStrokeColor(green);

        ScaleAnimation pop = new ScaleAnimation(
                1f, 1.05f,
                1f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        pop.setDuration(200);
        pop.setRepeatCount(1);
        pop.setRepeatMode(Animation.REVERSE);
        card.startAnimation(pop);
    }

    /** ❌ Incorrect shake (Unit Questions) */
    public static void animateUnitQuestionIncorrect(MaterialCardView card, Context context) {
        int red = ContextCompat.getColor(context, R.color.pastel_red);
        card.setStrokeColor(red);

        TranslateAnimation shake = new TranslateAnimation(0, 15, 0, 0);
        shake.setDuration(300);
        shake.setInterpolator(new CycleInterpolator(3));
        card.startAnimation(shake);
    }

    /** 🎉 Generic summary animation: big title + fading details */
    public static void animateSummary(Context context,
                                      View rootView,
                                      TextView title,
                                      TextView counter,
                                      TextView english,
                                      String completedText,
                                      MaterialCardView resetBtn) {
        //Standard Title
        title.setText("⭐ Magaling ⭐");
        title.setTextSize(22);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setTextColor(ContextCompat.getColor(context, R.color.brown));

        counter.setVisibility(View.GONE);
        english.setText(completedText);

        resetBtn.setVisibility(View.VISIBLE);

        // Title: fade + scale in
        title.setAlpha(0f);
        title.setScaleX(0.7f);
        title.setScaleY(0.7f);
        title.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .start();

        // English meaning / status: slide + fade
        english.setTranslationY(40f);
        english.setAlpha(0f);
        english.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(700)
                .setStartDelay(300)
                .start();

        // 🎆 Celebration (Lottie fireworks)
        LottieAnimationView celebrateAnim =
                ((Activity) context).findViewById(R.id.fireworkAnim);
        if (celebrateAnim != null) {
            Animations.playFireworks(celebrateAnim);
        }
    }

}
