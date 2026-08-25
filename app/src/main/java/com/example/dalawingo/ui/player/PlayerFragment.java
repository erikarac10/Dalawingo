package com.example.dalawingo.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dalawingo.R;

import java.util.Calendar;

public class PlayerFragment extends Fragment {

    private TextView streakNumber;

    private ImageView daySun, dayMon, dayTue, dayWed, dayThu, dayFri, daySat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player, container, false);

        // 🔥 Streak
        streakNumber = view.findViewById(R.id.streakNumber);

        // 📊 Weekly circles
        daySun = view.findViewById(R.id.daySun);
        dayMon = view.findViewById(R.id.dayMon);
        dayTue = view.findViewById(R.id.dayTue);
        dayWed = view.findViewById(R.id.dayWed);
        dayThu = view.findViewById(R.id.dayThu);
        dayFri = view.findViewById(R.id.dayFri);
        daySat = view.findViewById(R.id.daySat);

        // Example streak value (replace with real data later)
        int currentStreak = 3;
        streakNumber.setText(String.valueOf(currentStreak));

        updateWeekUI(currentStreak);

        // 🎨 Customize button
        View customize = view.findViewById(R.id.customize);
        customize.setOnClickListener(v -> {
            // TODO: open customization screen
        });

        // ▶ Daily Review button
        View playReview = view.findViewById(R.id.btnPlayReview);
        playReview.setOnClickListener(v -> {
            // TODO: navigate to review
        });

        return view;
    }

    private void updateWeekUI(int streak) {

        ImageView[] days = {
                daySun, dayMon, dayTue, dayWed,
                dayThu, dayFri, daySat
        };

        // Reset all
        for (ImageView day : days) {
            day.setImageResource(R.drawable.ic_circle);
        }

        // Fill based on streak (simple logic)
        for (int i = 0; i < streak && i < 7; i++) {
            days[i].setImageResource(R.drawable.ic_circle_filled);
        }

        // Optional: highlight today
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        int index = today - 1; // Sunday = 1

        if (index >= 0 && index < days.length) {
            days[index].setScaleX(1.2f);
            days[index].setScaleY(1.2f);
        }
    }
}