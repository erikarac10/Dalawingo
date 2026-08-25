package com.example.dalawingo.ui.study;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dalawingo.R;
import com.example.dalawingo.ui.lessons.cards.CardItem;
import com.example.dalawingo.ui.lessons.cards.CardAdapter;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit1;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit2;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit3;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit4;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit5;
import com.example.dalawingo.ui.lessons.units.Lesson1Unit6;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit1;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit2;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit3;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit4;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit5;
import com.example.dalawingo.ui.lessons.units.Lesson2Unit6;

import java.util.List;
import java.util.Locale;

public class StudyFragment extends Fragment {

    private RecyclerView recycler;
    private CardAdapter adapter;
    private TextToSpeech tts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.lessonRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setItemAnimator(null); // optional: reduces flicker

        adapter = new CardAdapter(this::speakPhrase);
        recycler.setAdapter(adapter);

        getParentFragmentManager().setFragmentResultListener(
                "unitArgs",
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    String unitKey = bundle.getString("unitKey", "lesson1_unit1");
                    loadUnit(unitKey);

                    requireActivity()
                            .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("lastUnitKey", unitKey)
                            .apply();

                    recycler.scrollToPosition(0);
                }
        );

        // Initial unit
        String lastUnitKey = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("lastUnitKey", "lesson1_unit1");

        loadUnit(lastUnitKey);

        // TTS
        tts = new TextToSpeech(requireContext(), status -> {
            if (status == TextToSpeech.SUCCESS) checkTtsAvailability();
        });
    }

    private void loadUnit(@NonNull String unitKey) {
        List<CardItem> items = getItemsForUnit(unitKey);
        adapter.submitList(items);
    }

    private List<CardItem> getItemsForUnit(@NonNull String unitKey) {
        switch (unitKey) {
            case "lesson2_unit6":
                return Lesson2Unit6.build();

            case "lesson2_unit5":
                return Lesson2Unit5.build();

            case "lesson2_unit4":
                return Lesson2Unit4.build();

            case "lesson2_unit3":
                return Lesson2Unit3.build();

            case "lesson2_unit2":
                return Lesson2Unit2.build();

            case "lesson2_unit1":
                return Lesson2Unit1.build();

            case "lesson1_unit6":
                return Lesson1Unit6.build();

            case "lesson1_unit5":
                return Lesson1Unit5.build();

            case "lesson1_unit4":
                return Lesson1Unit4.build();

            case "lesson1_unit3":
                return Lesson1Unit3.build();

            case "lesson1_unit2":
                return Lesson1Unit2.build();

            case "lesson1_unit1":
            default:
                return Lesson1Unit1.build();
        }
    }

    /**
     * Keeps your exact speaking rules:
     * - if there's "=", speak only the right side
     * - split on underscores
     * - strip parentheses content and "+" signs
     * - add short pauses between blank segments
     */
    private void speakPhrase(@NonNull String rawPhrase) {
        if (tts == null) return;

        int eq = rawPhrase.indexOf('=');
        if (eq >= 0 && eq < rawPhrase.length() - 1) {
            rawPhrase = rawPhrase.substring(eq + 1);
        }

        String[] parts = rawPhrase.split("_+");

        boolean first = true;
        for (String part : parts) {
            String speakable = part
                    .replaceAll("__\\([^)]*\\)___", " ")
                    .replaceAll("\\([^)]*\\)", " ")
                    .replaceAll("\\+", " ")

                    // ✅ strip punctuation that makes it read weird
                    .replaceAll("[\\.,]+", " ")

                    // (optional but nice) strip other common punctuation too
                    .replaceAll("[!?;:]+", " ")

                    .replaceAll("\\s+", " ")
                    .trim();

            if (!speakable.isEmpty()) {
                tts.speak(
                        speakable,
                        first ? TextToSpeech.QUEUE_FLUSH : TextToSpeech.QUEUE_ADD,
                        null,
                        "speech_" + System.nanoTime()
                );
                first = false;
            }

            tts.playSilentUtterance(
                    400,
                    TextToSpeech.QUEUE_ADD,
                    "pause_" + System.nanoTime()
            );
        }
    }

    private void checkTtsAvailability() {
        if (tts == null) return;

        int result = tts.setLanguage(new Locale("fil", "PH"));
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts.setLanguage(Locale.ENGLISH);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Tagalog voice not installed")
                    .setMessage("To hear Tagalog phrases spoken out loud, please install the Filipino voice in your phone’s Text-to-speech settings.")
                    .setPositiveButton("Open settings", (dialog, which) -> {
                        try {
                            startActivity(new Intent(Settings.ACTION_VOICE_INPUT_SETTINGS));
                        } catch (Exception e) {
                            Toast.makeText(requireContext(),
                                    "Could not open TTS settings. Please check system settings manually.",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        recycler = null;
        adapter = null;
    }
}
