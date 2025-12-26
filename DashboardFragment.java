package com.example.studybuddy.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.studybuddy.LoginActivity;
import com.example.studybuddy.R;
import com.example.studybuddy.ui.assignments.AssignmentsFragment;
import com.example.studybuddy.ui.calculator.CalculatorFragment;
import com.example.studybuddy.ui.expense.ExpenseFragment;
import com.example.studybuddy.ui.notes.NotesFragment;
import com.example.studybuddy.ui.studytimer.StudyTimerFragment;
import com.example.studybuddy.ui.timetable.TimetableFragment;
import com.example.studybuddy.ui.examcountdown.ExamCountdownFragment;

public class DashboardFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // ✅ ENABLE TOOLBAR MENU
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        view.findViewById(R.id.cardTimetable)
                .setOnClickListener(v -> openFragment(new TimetableFragment()));

        view.findViewById(R.id.cardAssignments)
                .setOnClickListener(v -> openFragment(new AssignmentsFragment()));

        view.findViewById(R.id.cardNotes)
                .setOnClickListener(v -> openFragment(new NotesFragment()));

        view.findViewById(R.id.cardExpenses)
                .setOnClickListener(v -> openFragment(new ExpenseFragment()));

        view.findViewById(R.id.cardStudyTimer)
                .setOnClickListener(v -> openFragment(new StudyTimerFragment()));

        view.findViewById(R.id.cardCalculator)
                .setOnClickListener(v -> openFragment(new CalculatorFragment()));

        view.findViewById(R.id.cardTools)
                .setOnClickListener(v -> openFragment(new ExamCountdownFragment()));

        return view;
    }

    private void openFragment(Fragment fragment) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // ================= TOOLBAR MENU =================

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_logout) {
            showLogoutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ================= LOGOUT DIALOG =================

    private void showLogoutDialog() {

        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setPositiveButton("Yes", (d, w) -> {

                    requireContext()
                            .getSharedPreferences("StudyBuddyPrefs", requireContext().MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    Intent intent = new Intent(requireContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
