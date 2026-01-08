package com.example.studybuddy.ui.examcountdown;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.data.database.AppDatabase;
import com.example.studybuddy.data.entity.ExamEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExamCountdownFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rv;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_exam_countdown, container, false);

        // Header
        View header = v.findViewById(R.id.header);
        if (header != null) {
            TextView title = header.findViewById(R.id.tvHeaderTitle);
            if (title != null) title.setText("Exam Countdown");
        }

        // Logged-in user
        SharedPreferences prefs =
                requireContext().getSharedPreferences(
                        "StudyBuddyPrefs", Context.MODE_PRIVATE);

        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(requireContext(),
                    "Please login again",
                    Toast.LENGTH_SHORT).show();
            return v;
        }

        db = AppDatabase.getInstance(requireContext());

        rv = v.findViewById(R.id.rvExams);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        v.findViewById(R.id.btnAddExam)
                .setOnClickListener(b -> showAddDialog());

        loadExams();
        return v;
    }

    // ================= LOAD =================

    private void loadExams() {

        List<ExamEntity> list =
                db.examDao().getAll(userId);

        rv.setAdapter(new ExamAdapter(list, this::showOptionsDialog));
    }

    // ================= OPTIONS =================

    private void showOptionsDialog(ExamEntity exam) {

        String[] options = {"Update", "Delete"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Choose Action")
                .setItems(options, (d, which) -> {
                    if (which == 0) {
                        showEditDialog(exam);
                    } else {
                        showDeleteConfirmation(exam);
                    }
                })
                .show();
    }

    // ================= ADD =================

    private void showAddDialog() {

        View d = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_exam, null);

        EditText etTitle = d.findViewById(R.id.etExamTitle);
        TextView tvDate = d.findViewById(R.id.tvPickDate);
        TextView tvTime = d.findViewById(R.id.tvPickTime);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        tvDate.setOnClickListener(v ->
                new DatePickerDialog(
                        requireContext(),
                        (dp, y, m, day) -> {
                            cal.set(y, m, day);
                            tvDate.setText(day + "/" + (m + 1) + "/" + y);
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show()
        );

        tvTime.setOnClickListener(v ->
                new TimePickerDialog(
                        requireContext(),
                        (tp, h, m) -> {
                            cal.set(Calendar.HOUR_OF_DAY, h);
                            cal.set(Calendar.MINUTE, m);
                            tvTime.setText(String.format("%02d:%02d", h, m));
                        },
                        10,
                        0,
                        false
                ).show()
        );

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Exam")
                .setView(d)
                .setPositiveButton("Save", (x, y) -> {

                    String title = etTitle.getText().toString().trim();

                    if (title.isEmpty()
                            || tvDate.getText().toString().contains("Pick")
                            || tvTime.getText().toString().contains("Pick")) {

                        Toast.makeText(requireContext(),
                                "Fill all fields",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long examTime = cal.getTimeInMillis();

                    if (examTime <= System.currentTimeMillis()) {
                        Toast.makeText(requireContext(),
                                "Exam time must be in future",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ExamEntity exam = new ExamEntity();
                    exam.userId = userId;
                    exam.title = title;
                    exam.examDate = examTime;

                    db.examDao().insert(exam);
                    scheduleNotification(examTime, title);
                    loadExams();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= UPDATE =================

    private void showEditDialog(ExamEntity exam) {

        View d = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_exam, null);

        EditText etTitle = d.findViewById(R.id.etExamTitle);
        TextView tvDate = d.findViewById(R.id.tvPickDate);
        TextView tvTime = d.findViewById(R.id.tvPickTime);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(exam.examDate);

        etTitle.setText(exam.title);
        tvDate.setText(
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(cal.getTime())
        );
        tvTime.setText(
                new SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(cal.getTime())
        );

        tvDate.setOnClickListener(v ->
                new DatePickerDialog(
                        requireContext(),
                        (dp, y, m, d1) -> {
                            cal.set(y, m, d1);
                            tvDate.setText(d1 + "/" + (m + 1) + "/" + y);
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show()
        );

        tvTime.setOnClickListener(v ->
                new TimePickerDialog(
                        requireContext(),
                        (tp, h, m) -> {
                            cal.set(Calendar.HOUR_OF_DAY, h);
                            cal.set(Calendar.MINUTE, m);
                            tvTime.setText(String.format("%02d:%02d", h, m));
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false
                ).show()
        );

        new AlertDialog.Builder(requireContext())
                .setTitle("Update Exam")
                .setView(d)
                .setPositiveButton("Update", (x, y) -> {
                    exam.title = etTitle.getText().toString().trim();
                    exam.examDate = cal.getTimeInMillis();
                    db.examDao().insert(exam); // update via PK
                    loadExams();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= DELETE =================

    private void showDeleteConfirmation(ExamEntity exam) {

        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Exam")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.examDao().delete(exam);
                    loadExams();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= NOTIFICATION =================

    private void scheduleNotification(long examTime, String title) {

        AlarmManager alarmManager =
                (AlarmManager) requireContext()
                        .getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) return;

        long notifyTime = examTime - TimeUnit.HOURS.toMillis(1);

        if (notifyTime <= System.currentTimeMillis()) return;

        Intent intent =
                new Intent(requireContext(), NotificationReceiver.class);

        intent.putExtra("title", title);
        intent.putExtra("message", "Your exam starts in 1 hour ⏰");

        PendingIntent pi = PendingIntent.getBroadcast(
                requireContext(),
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                notifyTime,
                pi
        );
    }
}
