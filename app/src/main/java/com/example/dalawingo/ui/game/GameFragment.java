package com.example.dalawingo.ui.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dalawingo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPendingGame();
    }

    /**
     * Loads the pending game fragment saved from PracticeFragment.
     */
    private void loadPendingGame() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String className = prefs.getString("pending_game_class", null);
        int colorRes = prefs.getInt("pending_game_color", R.color.pastel_red);

        // ✅ NO title handling anymore — only back/exit button styling
        setupExitButton(colorRes);

        if (className != null) {
            try {
                Fragment gameFragment =
                        (Fragment) Class.forName(className).newInstance();

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.practiceGameContainer, gameFragment)
                        .commit();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Clear after using
            prefs.edit()
                    .remove("pending_game_class")
                    .remove("pending_game_color")
                    .commit();
        }
    }

    /**
     * Setup Exit button to return to Practice tab.
     */
    private void setupExitButton(int colorRes) {
        View root = getView();
        if (root == null) return;

        FloatingActionButton btnExit = root.findViewById(R.id.btnExitGame);
        if (btnExit != null) {
            btnExit.setBackgroundTintList(requireContext().getColorStateList(colorRes));
            btnExit.setOnClickListener(v -> {
                BottomNavigationView navView = requireActivity().findViewById(R.id.nav_view);
                navView.setSelectedItemId(R.id.navigation_practice);
            });
        }
    }
}
