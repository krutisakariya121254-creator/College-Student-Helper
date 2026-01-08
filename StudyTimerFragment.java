package com.example.studybuddy.ui.studytimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybuddy.R;

public class StudyTimerFragment extends Fragment {

    TextView tvTime;
    Button btnStart, btnPause, btnReset;

    Handler handler = new Handler();
    boolean isRunning = false;
    long seconds = 0;

    SharedPreferences prefs;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seconds++;
            tvTime.setText(formatTime(seconds));
            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_study_timer, container, false);

        View header = view.findViewById(R.id.header);
        if (header != null) {
            TextView title = header.findViewById(R.id.tvHeaderTitle);
            title.setText("Studt Timer");
        }

        tvTime = view.findViewById(R.id.tvTimer);
        btnStart = view.findViewById(R.id.btnStart);
        btnPause = view.findViewById(R.id.btnPause);
        btnReset = view.findViewById(R.id.btnReset);

        prefs = requireContext().getSharedPreferences("StudyPrefs", 0);

        // Load saved time
        seconds = prefs.getLong("total_seconds", 0);
        tvTime.setText(formatTime(seconds));

        TextView tvStreak = view.findViewById(R.id.tvStreak);

        int current = prefs.getInt("current_streak", 0);
        int best = prefs.getInt("best_streak", 0);

        tvStreak.setText("🔥 " + current + " days | 🏆 Best: " + best);


        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());

        return view;
    }

    private void startTimer() {
        if (!isRunning) {
            handler.post(runnable);
            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
            saveTime();
            updateStreak();
        }
    }

    private void updateStreak() {

        long todayMinutes = seconds / 60;

        long lastDate = prefs.getLong("last_study_date", 0);
        int currentStreak = prefs.getInt("current_streak", 0);
        int bestStreak = prefs.getInt("best_streak", 0);

        long today = System.currentTimeMillis() / (1000 * 60 * 60 * 24);

        if (todayMinutes >= 20) {

            if (lastDate == today - 1) {
                currentStreak++;
            } else if (lastDate != today) {
                currentStreak = 1;
            }

            if (currentStreak > bestStreak) {
                bestStreak = currentStreak;
            }

            prefs.edit()
                    .putInt("current_streak", currentStreak)
                    .putInt("best_streak", bestStreak)
                    .putLong("last_study_date", today)
                    .apply();
        }
    }

    private void resetTimer() {
        handler.removeCallbacks(runnable);
        isRunning = false;
        seconds = 0;
        tvTime.setText("00:00:00");
        saveTime();
    }

    private void saveTime() {
        prefs.edit()
                .putLong("total_seconds", seconds)
                .apply();
    }

    private String formatTime(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = sec % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}