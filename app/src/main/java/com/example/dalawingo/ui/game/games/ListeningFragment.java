package com.example.dalawingo.ui.game.games;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dalawingo.R;
//import com.example.dalawingo.SoundPlayer;
import com.example.dalawingo.data.Phrase;
import com.example.dalawingo.data.Unit;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.game.shared.SharedFunctions;
import com.example.dalawingo.ui.game.shared.SoundPlayer;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ListeningFragment extends Fragment {

    private List<Phrase> phrases;
    private int questionIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private MaterialCardView[] optionCards;
    private TextView[] optionTexts;
    private TextView counter;
    private MaterialCardView resetBtn, speakerCard;
    private ImageView speakerBtn;

    private Phrase correctPhrase;
    private Random random;

    private SharedPreferences prefs;
    private String currentUnitKey;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_listening, container, false); // 👈 your XML
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        counter = view.findViewById(R.id.speechCounter);
        resetBtn = view.findViewById(R.id.speechbtnResetGame);
        speakerBtn = view.findViewById(R.id.speakerBtn);
        speakerCard = view.findViewById(R.id.speakerCard);

        optionCards = new MaterialCardView[]{
                view.findViewById(R.id.speechOption1),
                view.findViewById(R.id.speechOption2),
                view.findViewById(R.id.speechOption3),
                view.findViewById(R.id.speechOption4)
        };

        optionTexts = new TextView[]{
                view.findViewById(R.id.speechOption1Text),
                view.findViewById(R.id.speechOption2Text),
                view.findViewById(R.id.speechOption3Text),
                view.findViewById(R.id.speechOption4Text)
        };

        prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        currentUnitKey = prefs.getString("lastUnitKey", "lesson1_unit1");

        resetBtn.setOnClickListener(v -> resetGame());

        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = new ArrayList<>(currentUnit.getPhrases());

        // ✅ SKIP phrases with blanks
        phrases.removeIf(p -> p.tagalog.contains("_____"));

        Collections.shuffle(phrases);
        random = new Random();

        // init TTS
        tts = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("fil", "PH"));
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.setLanguage(Locale.ENGLISH); // fallback
                }

                // 👇 add listener
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        requireActivity().runOnUiThread(() -> startSpeakerAnimation());
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        requireActivity().runOnUiThread(() -> stopSpeakerAnimation());
                    }

                    @Override
                    public void onError(String utteranceId) {
                        requireActivity().runOnUiThread(() -> stopSpeakerAnimation());
                    }
                });
            }
        });

        loadNextQuestion();
    }
    private void startSpeakerAnimation() {
        // Pulse effect repeating while speaking, applied to the card
        speakerCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(500)
                .withEndAction(() -> {
                    if (tts != null) {
                        // chain a reverse animation to loop while speaking
                        speakerCard.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(500)
                                .withEndAction(() -> {
                                    // if still speaking, restart
                                    if (tts.isSpeaking()) {
                                        startSpeakerAnimation();
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

    private void stopSpeakerAnimation() {
        speakerCard.animate().cancel();
        speakerCard.setScaleX(1f);
        speakerCard.setScaleY(1f);
    }


    /** ---------------- Game Flow ---------------- */
    private void loadNextQuestion() {
        if (questionIndex >= totalQuestions) {
            showSummary();
            return;
        }

        correctPhrase = phrases.get(questionIndex % phrases.size());

        // Hook up speaker button to speak Tagalog
        speakerBtn.setOnClickListener(v -> {
            if (tts != null) {
                // pass a unique ID so listener works
                tts.speak(correctPhrase.tagalog,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "utteranceId_" + System.currentTimeMillis());
            }
        });

        // Generate options (English meanings)
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
                    checkAnswer(options.get(finalI), optionCards[finalI]));
        }

        counter.setText((questionIndex + 1) + "/" + totalQuestions);
        resetBtn.setVisibility(View.GONE);
    }

    private void checkAnswer(String chosen, MaterialCardView pressedCard) {
        boolean correct = chosen.equals(correctPhrase.english);
        for (MaterialCardView card : optionCards) card.setEnabled(false);

        if (correct) {
            // play right sound quieter
            SoundPlayer.play(requireContext(), R.raw.right, 0.4f);
            //SharedFunctions.playSound(requireContext(), R.raw.right);
            SharedFunctions.animateMCCorrect(requireView(), pressedCard);
            correctCount++;
        } else {
            // play wrong sound quieter
            SoundPlayer.play(requireContext(), R.raw.wrong, 0.4f);
            //SharedFunctions.playSound(requireContext(), R.raw.wrong);
            SharedFunctions.animateMCWrong(requireView(), pressedCard, optionCards, optionTexts, correctPhrase.english);
        }

        pressedCard.postDelayed(() -> {
            questionIndex++;
            loadNextQuestion();
        }, 1500);
    }

    /** ---------------- Helpers ---------------- */
    private void resetOptionStyle(MaterialCardView card, TextView text) {
        card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
        card.setCardBackgroundColor(Color.WHITE);
        text.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        card.setEnabled(true);
        card.setVisibility(View.VISIBLE);
    }

    /** ---------------- Summary + Reset ---------------- */
    private void showSummary() {
        if (tts != null) tts.stop();   // ✅ THIS LINE
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                counter, // we’ll animate over this
                counter,
                optionTexts[0],
                "Score: " + correctCount + " / " + totalQuestions,
                resetBtn
        );

        resetOptionStyle(optionCards[0], optionTexts[0]);
        optionCards[1].setVisibility(View.GONE);
        optionCards[2].setVisibility(View.GONE);
        optionCards[3].setVisibility(View.GONE);

        SharedFunctions.saveBestScore(prefs,
                currentUnitKey + "_best_score_listening",
                correctCount,
                totalQuestions);
    }

    private void resetGame() {
        questionIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);

        for (MaterialCardView card : optionCards) card.setVisibility(View.VISIBLE);
        counter.setVisibility(View.VISIBLE);
        resetBtn.setVisibility(View.GONE);

        loadNextQuestion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}