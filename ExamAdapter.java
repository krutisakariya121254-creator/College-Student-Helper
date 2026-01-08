package com.example.studybuddy.ui.examcountdown;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studybuddy.R;
import com.example.studybuddy.data.entity.ExamEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.VH> {

    public interface OnItemLongClick {
        void onLongClick(ExamEntity exam);
    }

    private final List<ExamEntity> list;
    private final OnItemLongClick listener;

    public ExamAdapter(List<ExamEntity> list, OnItemLongClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {

        ExamEntity e = list.get(position);

        h.tvTitle.setText(e.title);

        SimpleDateFormat sdf =
                new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        h.tvDate.setText("Exam Date: " + sdf.format(new Date(e.examDate)));

        long daysLeft = getDaysLeft(e.examDate);

        if (daysLeft > 1) {
            h.tvDaysLeft.setText(daysLeft + " days remaining");
            h.tvDaysLeft.setTextColor(Color.parseColor("#1E88E5"));
        } else if (daysLeft == 1) {
            h.tvDaysLeft.setText("Tomorrow");
            h.tvDaysLeft.setTextColor(Color.parseColor("#FB8C00"));
        } else if (daysLeft == 0) {
            h.tvDaysLeft.setText("🔥 Exam Today!");
            h.tvDaysLeft.setTextColor(Color.parseColor("#E53935"));
        } else {
            h.tvDaysLeft.setText("❌ Exam Passed");
            h.tvDaysLeft.setTextColor(Color.GRAY);
        }

        h.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(e);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private long getDaysLeft(long examDateMillis) {

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar examDay = Calendar.getInstance();
        examDay.setTimeInMillis(examDateMillis);
        examDay.set(Calendar.HOUR_OF_DAY, 0);
        examDay.set(Calendar.MINUTE, 0);
        examDay.set(Calendar.SECOND, 0);
        examDay.set(Calendar.MILLISECOND, 0);

        long diff = examDay.getTimeInMillis() - today.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvDaysLeft;

        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvExamTitle);
            tvDate = v.findViewById(R.id.tvExamDate);
            tvDaysLeft = v.findViewById(R.id.tvDaysLeft);
        }
    }
}
