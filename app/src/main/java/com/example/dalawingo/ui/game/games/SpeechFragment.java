package com.example.dalawingo.ui.game.games;

import static android.view.View.GONE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SpeechFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO = 101;

    private List<Phrase> phrases;
    private int phraseIndex = 0;
    private int correctCount = 0;
    private final int totalQuestions = 10;

    private TextView speechText, speechCounter, speechEnglish;
    private ImageView microphoneBtn;
    private MaterialCardView resetBtn, speechCard;

    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private boolean isListening = false;
    private TextToSpeech tts;
    private Random random;

    private String currentUnitKey;
    private AlphaAnimation pulseAnim;

    private View micRow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_speech, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        speechText = view.findViewById(R.id.speechText);
        speechCounter = view.findViewById(R.id.speechCounter);
        speechEnglish = view.findViewById(R.id.speechEnglish);
        micRow = view.findViewById(R.id.micRow);
        microphoneBtn = view.findViewById(R.id.microphoneBtn);
        resetBtn = view.findViewById(R.id.speechbtnResetGame);
        speechCard = view.findViewById(R.id.speechCard);

        pulseAnim = new AlphaAnimation(0.4f, 1f);
        pulseAnim.setDuration(600);
        pulseAnim.setRepeatMode(AlphaAnimation.REVERSE);
        pulseAnim.setRepeatCount(AlphaAnimation.INFINITE);

        // load phrases
        currentUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");
        Unit currentUnit = UnitData.getUnit(currentUnitKey);
        phrases = new ArrayList<>(currentUnit.getPhrases());

        random = new Random();
        Collections.shuffle(phrases);

        loadNextPhrase();

        // ---- SpeechRecognizer ----
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());

        speechRecognizer.setRecognitionListener(new SimpleRecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches =
                        results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if (matches != null && !matches.isEmpty()) {

                    // what the user said (try all hypotheses, not just first)
                    boolean isCorrect = false;

                    // raw target phrase from your data
                    String targetRaw = phrases.get(phraseIndex).tagalog.trim();

                    // Split target into alternatives if you use "/" as “either/or”
                    // Example: "kumusta / kamusta" -> two acceptable targets
                    String[] targetAlternatives = targetRaw.split("\\s*/\\s*");

                    // Build normalized "required" targets by removing placeholders/underscores
                    java.util.List<String> requiredTargets = new java.util.ArrayList<>();
                    for (String alt : targetAlternatives) {
                        String required = alt
                                .replaceAll("__\\([^)]*\\)___", " ")  // remove __(job title)___
                                .replaceAll("_+", " ")                // remove _____
                                .replaceAll("\\s+", " ")
                                .trim();

                        String reqNorm = normalize(required);
                        if (!reqNorm.isEmpty()) requiredTargets.add(reqNorm);
                    }

                    // If stripping removed everything, don't get stuck—skip this phrase
                    if (requiredTargets.isEmpty()) {
                        phraseIndex++;
                        loadNextPhrase();
                        return;
                    }

                    // Compare each recognized hypothesis against each required target
                    for (String match : matches) {
                        String spokenNorm = normalize(match);

                        for (String targetNorm : requiredTargets) {

                            // If target is very short (1–2 words), use whole-word containment
                            // This avoids Levenshtein being too harsh/noisy on short strings.
                            int wc = targetNorm.split("\\s+").length;
                            if (wc <= 2) {
                                String paddedSpoken = " " + spokenNorm + " ";
                                String paddedTarget = " " + targetNorm + " ";
                                if (paddedSpoken.contains(paddedTarget)) {
                                    isCorrect = true;
                                    break;
                                }
                            } else {
                                int distance = levenshtein(spokenNorm, targetNorm);
                                float similarity =
                                        1f - (float) distance /
                                                Math.max(spokenNorm.length(), targetNorm.length());

                                if (similarity >= 0.60f) {
                                    isCorrect = true;
                                    break;
                                }
                            }
                        }

                        if (isCorrect) break;
                    }

                    if (isCorrect) {
                        SharedFunctions.playSound(requireContext(), R.raw.right);
                        correctCount++;
                        SharedFunctions.animateUnitQuestionCorrect(speechCard, requireContext());
                        nextPhrase();
                    } else {
                        SharedFunctions.playSound(requireContext(), R.raw.wrong);
                        SharedFunctions.animateUnitQuestionIncorrect(speechCard, requireContext());
                        Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                }

                stopMicAnimation();
                isListening = false;
            }


            @Override
            public void onError(int error) {
                // 🔥 removed timeout / no-speech handling
                stopMicAnimation();
                isListening = false;
            }
        });

        microphoneBtn.setOnClickListener(v -> {
            if (!isListening) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            REQUEST_RECORD_AUDIO);
                } else {
                    startListening();
                }
            } else {
                // ⏹ Stop pressed → finalize recognition
                speechRecognizer.stopListening();
                stopMicAnimation();
                isListening = false;

            }
        });


        // ---- TextToSpeech ----
        tts = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(new Locale("fil", "PH"));
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(requireContext(),
                            "Tagalog voice not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetBtn.setOnClickListener(v -> resetGame());
    }
    private int levenshtein(String a, String b) {
        int[][] dp = new int[a.length()+1][b.length()+1];
        for (int i=0; i<=a.length(); i++) dp[i][0] = i;
        for (int j=0; j<=b.length(); j++) dp[0][j] = j;
        for (int i=1; i<=a.length(); i++) {
            for (int j=1; j<=b.length(); j++) {
                int cost = (a.charAt(i-1) == b.charAt(j-1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i-1][j]+1, dp[i][j-1]+1), dp[i-1][j-1]+cost);
            }
        }
        return dp[a.length()][b.length()];
    }
    String normalize(String text) {
        String noAccents = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // strip accents/diacritics
        return noAccents.toLowerCase()
                .replaceAll("[^a-z0-9 ]", "") // keep only basic letters/numbers/spaces
                .trim();
    }

    private void loadNextPhrase() {
        if (phraseIndex >= totalQuestions || phraseIndex >= phrases.size()) {
            showSummary();
            return;
        }

        Phrase phrase = phrases.get(phraseIndex);

        // Tagalog text
        SharedFunctions.animateNewWord(speechText, phrase.tagalog);

        // English meaning
        speechEnglish.setText(phrase.english);

        // Counter
        speechCounter.setText((phraseIndex + 1) + "/" + totalQuestions);

        resetBtn.setVisibility(GONE);
    }


    private void nextPhrase() {
        phraseIndex++;
        loadNextPhrase();
    }

    private void showSummary() {
        SharedFunctions.playSound(requireContext(), R.raw.congrats);

        SharedFunctions.saveBestScore(
                requireActivity().getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE),
                currentUnitKey + "_best_score_speech",
                correctCount,
                totalQuestions
        );

        stopMicAnimation();
        isListening = false;
        if (micRow != null) micRow.setVisibility(View.GONE);

        // 🎉 Animate summary
        SharedFunctions.animateSummary(
                requireContext(),
                requireView(),
                speechText,
                speechEnglish,
                speechCounter,
                "Completed",
                resetBtn
        );

    }

    private void startListening() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext());
        }

        // fresh intent each time
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fil-PH");

        speechRecognizer.startListening(speechIntent);
        isListening = true;

        microphoneBtn.setImageResource(R.drawable.ic_stop);
        microphoneBtn.startAnimation(pulseAnim);

        Toast.makeText(requireContext(), "Listening…", Toast.LENGTH_SHORT).show();
    }

    private void stopMicAnimation() {
        microphoneBtn.clearAnimation();
        microphoneBtn.setImageResource(R.drawable.ic_microphone);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            } else {
                Toast.makeText(requireContext(),
                        "Microphone permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetGame() {
        phraseIndex = 0;
        correctCount = 0;
        Collections.shuffle(phrases);
        loadNextPhrase();
        stopMicAnimation();
        speechCounter.setVisibility(View.VISIBLE);
        isListening = false;
        if (micRow != null) micRow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) speechRecognizer.destroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
