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

        // Inflate the layout using the transparent MaterialCardView design
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvUsername = view.findViewById(R.id.tvUsername);

        // ---------- GREETING LOGIC ----------
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            tvGreeting.setText("Good Morning \uD83C\uDF3C");
        } else if (hour < 17) {
            tvGreeting.setText("Good Afternoon 🌤️");
        } else if (hour < 21) {
            tvGreeting.setText("Good Evening 🌆");
        } else {
            tvGreeting.setText("Good Night 🌙");
        }

        // ---------- FETCH USERNAME ----------
        SharedPreferences prefs =
                requireContext().getSharedPreferences("StudyBuddyPrefs", 0);

        // Standardized to "kruti" based on your screenshot or default to Student
        String username = prefs.getString("username", "kruti");
        tvUsername.setText(username);

        // ---------- ICON CLICK LISTENERS ----------
        // These IDs match the MaterialCardView containers in the XML

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

        view.findViewById(R.id.cardExamCountdown)
                .setOnClickListener(v -> open(new ExamCountdownFragment()));

        return view;
    }

    /**
     * Opens a fragment with a smooth slide-and-fade animation
     * to match the premium transparent icon look.
     */
    private void open(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.anim.slide_in_left,  // Enter
                        android.R.anim.fade_out,       // Exit
                        android.R.anim.fade_in,        // Pop Enter
                        android.R.anim.slide_out_right // Pop Exit
                )
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}