package com.example.studybuddy.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybuddy.R;
import com.example.studybuddy.ui.assignments.AssignmentsFragment;
import com.example.studybuddy.ui.calculator.CalculatorFragment;
import com.example.studybuddy.ui.expense.ExpenseFragment;
import com.example.studybuddy.ui.notes.NotesFragment;
import com.example.studybuddy.ui.studytimer.StudyTimerFragment;
import com.example.studybuddy.ui.timetable.TimetableFragment;
import com.example.studybuddy.ui.examcountdown.ExamCountdownFragment;

import java.util.Calendar;

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvUsername = view.findViewById(R.id.tvUsername);

        // ---------- GREETING ----------
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            tvGreeting.setText("Good Morning ");
        } else if (hour < 17) {
            tvGreeting.setText("Good Afternoon 🌤️");
        } else if (hour < 21) {
            tvGreeting.setText("Good Evening 🌆");
        } else {
            tvGreeting.setText("Good Night 🌙");
        }

        // ---------- USERNAME ----------
        SharedPreferences prefs =
                requireContext().getSharedPreferences("StudyBuddyPrefs", 0);

        String username = prefs.getString("username", "Student");
        tvUsername.setText(username);

        // ---------- CARD CLICKS ----------
        view.findViewById(R.id.cardTimetable)
                .setOnClickListener(v -> open(new TimetableFragment()));

        view.findViewById(R.id.cardAssignments)
                .setOnClickListener(v -> open(new AssignmentsFragment()));

        view.findViewById(R.id.cardNotes)
                .setOnClickListener(v -> open(new NotesFragment()));

        view.findViewById(R.id.cardExpenses)
                .setOnClickListener(v -> open(new ExpenseFragment()));

        view.findViewById(R.id.cardStudyTimer)
                .setOnClickListener(v -> open(new StudyTimerFragment()));

        view.findViewById(R.id.cardCalculator)
                .setOnClickListener(v -> open(new CalculatorFragment()));

        view.findViewById(R.id.cardTools)
                .setOnClickListener(v -> open(new ExamCountdownFragment()));

        return view;
    }

    private void open(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}