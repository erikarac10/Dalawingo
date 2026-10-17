package com.example.dalawingo.ui.lessons;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import com.example.dalawingo.R;
import com.example.dalawingo.data.Unit;
import com.example.dalawingo.data.UnitData;
import com.example.dalawingo.ui.ScrollStateViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class LessonsFragment extends Fragment {
    private LinearLayout listLesson1;
    private LinearLayout listLesson2;
    private SharedPreferences prefs;
    private ScrollStateViewModel scrollVm;
    private final Map<Integer, Integer> unitCountCache = new HashMap();

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lessons, container, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.prefs = requireActivity().getSharedPreferences("app_prefs", 0);
        this.scrollVm = (ScrollStateViewModel) new ViewModelProvider(requireActivity()).get(ScrollStateViewModel.class);
        this.listLesson1 = (LinearLayout) view.findViewById(R.id.unitListLesson1);
        this.listLesson2 = (LinearLayout) view.findViewById(R.id.unitListLesson2);
        buildLessons();
        getParentFragmentManager().setFragmentResultListener("unitCompleted", getViewLifecycleOwner(), new FragmentResultListener() { // from class: com.example.dalawingo.ui.lessons.LessonsFragment$$ExternalSyntheticLambda1
            @Override // androidx.fragment.app.FragmentResultListener
            public final void onFragmentResult(String str, Bundle bundle) {
                this.f$0.m2618x5c00b01b(str, bundle);
            }
        });
        final ScrollView scrollView = (ScrollView) view.findViewById(R.id.lessonsScroll);
        if (scrollView != null) {
            scrollView.post(new Runnable() { // from class: com.example.dalawingo.ui.lessons.LessonsFragment$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m2619x4daa563a(scrollView);
                }
            });
            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() { // from class: com.example.dalawingo.ui.lessons.LessonsFragment$$ExternalSyntheticLambda3
                @Override // android.view.ViewTreeObserver.OnScrollChangedListener
                public final void onScrollChanged() {
                    this.f$0.m2620x3f53fc59(scrollView);
                }
            });
        }
    }

    /* JADX INFO: renamed from: lambda$onViewCreated$0$com-example-dalawingo-ui-lessons-LessonsFragment, reason: not valid java name */
    /* synthetic */ void m2618x5c00b01b(String requestKey, Bundle bundle) {
        buildLessons();
    }

    /* JADX INFO: renamed from: lambda$onViewCreated$1$com-example-dalawingo-ui-lessons-LessonsFragment, reason: not valid java name */
    /* synthetic */ void m2619x4daa563a(ScrollView scrollView) {
        scrollView.scrollTo(0, this.scrollVm.lessonsScrollY);
    }

    /* JADX INFO: renamed from: lambda$onViewCreated$2$com-example-dalawingo-ui-lessons-LessonsFragment, reason: not valid java name */
    /* synthetic */ void m2620x3f53fc59(ScrollView scrollView) {
        this.scrollVm.lessonsScrollY = scrollView.getScrollY();
    }

    private void buildLessons() {
        if (this.listLesson1 != null) {
            this.listLesson1.removeAllViews();
        }
        if (this.listLesson2 != null) {
            this.listLesson2.removeAllViews();
        }
        int lesson1Count = getUnitCountForLessonCached(1);
        addUnitRows(this.listLesson1, 1, lesson1Count);
        int lesson2Count = getUnitCountForLessonCached(2);
        addUnitRows(this.listLesson2, 2, lesson2Count);
    }

    private void addUnitRows(LinearLayout parent, int lessonNumber, int unitCount) {
        if (parent == null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int unitNum = 1; unitNum <= unitCount; unitNum++) {
            View row = inflater.inflate(R.layout.unit_row, (ViewGroup) parent, false);
            MaterialCardView card = (MaterialCardView) row;
            TextView tv = (TextView) row.findViewById(R.id.tvUnit);
            final String unitKey = "lesson" + lessonNumber + "_unit" + unitNum;
            Unit unitObj = UnitData.getUnit(unitKey);
            String unitTitle = "Unit " + unitNum + ": " + unitObj.getTitle();
            tv.setText(unitTitle);
            tv.setTypeface(tv.getTypeface(), 1);
            boolean unlocked = isUnitUnlocked(lessonNumber, unitNum);
            if (unlocked) {
                styleUnlocked(card, tv);
                row.setOnClickListener(new View.OnClickListener() { // from class: com.example.dalawingo.ui.lessons.LessonsFragment$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        this.f$0.m2617x1a38735e(unitKey, view);
                    }
                });
            } else {
                styleLocked(card, tv);
                row.setClickable(false);
            }
            parent.addView(row);
        }
    }

    /* JADX INFO: renamed from: lambda$addUnitRows$3$com-example-dalawingo-ui-lessons-LessonsFragment, reason: not valid java name */
    /* synthetic */ void m2617x1a38735e(String unitKey, View v) {
        openUnit(unitKey);
    }

    private void styleUnlocked(MaterialCardView card, TextView tv) {
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.white));
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown));
        card.setAlpha(1.0f);
        card.setCardElevation(0.0f);
        card.setPreventCornerOverlap(false);
        card.setUseCompatPadding(false);
    }

    private void styleLocked(MaterialCardView card, TextView tv) {
        card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey));
        tv.setTextColor(ContextCompat.getColor(requireContext(), R.color.pastel_grey));
        card.setAlpha(0.8f);
    }

    private boolean isUnitUnlocked(int lessonNumber, int unit) {
        if (lessonNumber == 1 || lessonNumber == 2) {
            return true;
        }
        if (lessonNumber != 1 || unit != 1) {
            if (lessonNumber == 1) {
                String prevUnitKey = "lesson" + lessonNumber + "_unit" + (unit - 1);
                return this.prefs.getBoolean("unit_complete_" + prevUnitKey, false);
            }
            if (lessonNumber == 2 && unit == 1) {
                return this.prefs.getBoolean("unit_complete_lesson1_unit5", false);
            }
            String prevUnitKey2 = "lesson" + lessonNumber + "_unit" + (unit - 1);
            return this.prefs.getBoolean("unit_complete_" + prevUnitKey2, false);
        }
        return true;
    }

    private void openUnit(String unitKey) {
        Bundle args = new Bundle();
        args.putString("unitKey", unitKey);
        getParentFragmentManager().setFragmentResult("unitArgs", args);
        ScrollStateViewModel vm = (ScrollStateViewModel) new ViewModelProvider(requireActivity()).get(ScrollStateViewModel.class);
        vm.studyScrollY = 0;
        vm.practiceScrollY = 0;
        this.prefs.edit().putString("lastUnitKey", unitKey).apply();
        final View nav = requireActivity().findViewById(R.id.nav_view);
        if (nav != null) {
            nav.post(new Runnable() { // from class: com.example.dalawingo.ui.lessons.LessonsFragment$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ((BottomNavigationView) nav).setSelectedItemId(R.id.navigation_study);
                }
            });
        }
    }

    private int getUnitCountForLessonCached(int lessonNumber) {
        Integer cached = this.unitCountCache.get(Integer.valueOf(lessonNumber));
        if (cached != null) {
            return cached.intValue();
        }
        int counted = countUnitsForLesson(lessonNumber);
        this.unitCountCache.put(Integer.valueOf(lessonNumber), Integer.valueOf(counted));
        return counted;
    }

    private int countUnitsForLesson(int lessonNumber) {
        String prefix = "lesson" + lessonNumber + "_unit";
        int count = 0;
        for (String key : UnitData.units.keySet()) {
            if (key != null && key.startsWith(prefix)) {
                count++;
            }
        }
        return count;
    }
}
