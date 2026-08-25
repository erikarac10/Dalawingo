package com.example.dalawingo.ui.lessons;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dalawingo.R;
import com.example.dalawingo.data.Unit;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.ScrollStateViewModel;
import com.example.dalawingo.ui.SnakePathView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LessonsFragment extends Fragment {

    private SharedPreferences prefs;
    private LinearLayout listLesson1, listLesson2;
    private ScrollStateViewModel scrollVm;

    // simple cache so we don't recount every time
    private final Map<Integer, Integer> unitCountCache = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        scrollVm = new ViewModelProvider(requireActivity()).get(ScrollStateViewModel.class);

        listLesson1 = view.findViewById(R.id.unitListLesson1);
        listLesson2 = view.findViewById(R.id.unitListLesson2);

        // Build once
        buildLessons();

        // Rebuild when a unit completes (unlock progression)
        getParentFragmentManager().setFragmentResultListener(
                "unitCompleted",
                getViewLifecycleOwner(),
                (requestKey, bundle) -> buildLessons()
        );

        // Scroll persistence
        ScrollView scrollView = view.findViewById(R.id.lessonsScroll);
        if (scrollView != null) {
            scrollView.post(() -> scrollView.scrollTo(0, scrollVm.lessonsScrollY));
            scrollView.getViewTreeObserver().addOnScrollChangedListener(
                    () -> scrollVm.lessonsScrollY = scrollView.getScrollY()
            );
        }
    }

    private void buildLessons() {
        if (listLesson1 != null) listLesson1.removeAllViews();
        if (listLesson2 != null) listLesson2.removeAllViews();

        // Lesson 1
        int lesson1Count = getUnitCountForLessonCached(1);
        addUnitRows(listLesson1, 1, lesson1Count);

        // Lesson 2 (keep the container behavior, only render if you want)
        // If your UI always shows Lesson 2 header/list, you can keep it:
        int lesson2Count = getUnitCountForLessonCached(2);
        addUnitRows(listLesson2, 2, lesson2Count);
    }

    private void addUnitRows(@Nullable LinearLayout parent, int lessonNumber, int unitCount) {
        if (parent == null) return;

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (int unitNum = 1; unitNum <= unitCount; unitNum++) {

            View row = inflater.inflate(R.layout.unit_row, parent, false);

            TextView tvTitle = row.findViewById(R.id.tvUnitTitle);
            TextView tvNumber = row.findViewById(R.id.tvUnitNumber);
            MaterialCardView circle = row.findViewById(R.id.unitCircle);

            final String unitKey = "lesson" + lessonNumber + "_unit" + unitNum;

            Unit unitObj = UnitData.getUnit(unitKey);

            tvTitle.setText(unitObj.getTitle());
            tvNumber.setText(String.valueOf(unitNum));

            boolean unlocked = isUnitUnlocked(lessonNumber, unitNum);

            if (unlocked) {
                styleUnlocked(circle, tvNumber);
                row.setOnClickListener(v -> openUnit(unitKey));
            } else {
                styleLocked(circle, tvNumber);
            }

            // === Snake Curve ===

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.gravity = android.view.Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 100;

            row.setLayoutParams(params);
            parent.addView(row);

            int finalUnitNum = unitNum;
            row.post(() -> {

                float maxShift = (parent.getWidth() - row.getWidth()) / 2f;

                float progress = (finalUnitNum - 1f) / (unitCount - 1f);

                // Offset sine so midpoint is NOT symmetric
                float sine = (float) Math.sin(progress * Math.PI * 1.5f - Math.PI / 2f);

                row.setTranslationX(sine * maxShift);
            });
        }parent.post(() -> {

            SnakePathView snake = null;

            if (parent.getId() == R.id.unitListLesson1) {
                snake = requireView().findViewById(R.id.snakePathLesson1);
            } else if (parent.getId() == R.id.unitListLesson2) {
                snake = requireView().findViewById(R.id.snakePathLesson2);
            }

            if (snake == null) return;

            snake.setUnitCount(unitCount);

            if (parent.getChildCount() > 0) {
                View firstRow = parent.getChildAt(0);
                float maxShift = (parent.getWidth() - firstRow.getWidth()) / 2f;
                snake.setMaxShiftPx(maxShift);
            }
        });
    }

    private void styleUnlocked(@NonNull MaterialCardView card, @NonNull TextView tv) {
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        card.setAlpha(1f);

        // keep your "no shadow" unlocked look
        card.setCardElevation(0f);
        card.setPreventCornerOverlap(false);
        card.setUseCompatPadding(false);
    }

    private void styleLocked(@NonNull MaterialCardView card, @NonNull TextView tv) {
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
        card.setAlpha(0.8f);
    }

    private boolean isUnitUnlocked(int lessonNumber, int unit) {
        // First lesson, first unit always free
        if(lessonNumber == 1 || lessonNumber == 2) return true; //Debug to unlock all units
        if (lessonNumber == 1 && unit == 1) return true;

        // Lesson 1: unit unlocked if previous unit complete
        if (lessonNumber == 1) {
            String prevUnitKey = "lesson" + lessonNumber + "_unit" + (unit - 1);
            return prefs.getBoolean("unit_complete_" + prevUnitKey, false);
        }

        // Lesson 2+: first unit locked until last unit of previous lesson complete
        if (lessonNumber == 2 && unit == 1) {
            return prefs.getBoolean("unit_complete_lesson1_unit5", false);
        }

        // Otherwise: unlocked if previous unit in same lesson complete
        String prevUnitKey = "lesson" + lessonNumber + "_unit" + (unit - 1);
        return prefs.getBoolean("unit_complete_" + prevUnitKey, false);
    }

    private void openUnit(@NonNull String unitKey) {
        Bundle args = new Bundle();
        args.putString("unitKey", unitKey);

        // Notify StudyFragment
        getParentFragmentManager().setFragmentResult("unitArgs", args);

        // Reset scroll positions (existing behavior)
        ScrollStateViewModel vm = new ViewModelProvider(requireActivity()).get(ScrollStateViewModel.class);
        vm.studyScrollY = 0;
        vm.practiceScrollY = 0;

        // Save last unit
        prefs.edit().putString("lastUnitKey", unitKey).apply();

        // Switch to Study tab
        View nav = requireActivity().findViewById(R.id.nav_view);
        if (nav != null) {
            nav.post(() -> ((BottomNavigationView) nav).setSelectedItemId(R.id.navigation_study));
        }
    }

    private int getUnitCountForLessonCached(int lessonNumber) {
        Integer cached = unitCountCache.get(lessonNumber);
        if (cached != null) return cached;

        int counted = countUnitsForLesson(lessonNumber);
        unitCountCache.put(lessonNumber, counted);
        return counted;
    }

    private int countUnitsForLesson(int lessonNumber) {
        String prefix = "lesson" + lessonNumber + "_unit";
        int count = 0;
        for (String key : UnitData.units.keySet()) {
            if (key != null && key.startsWith(prefix)) count++;
        }
        return count;
    }
}
