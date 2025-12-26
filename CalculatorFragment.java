package com.example.studybuddy.ui.calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studybuddy.R;

public class CalculatorFragment extends Fragment {

    private TextView tvResult;

    private double firstNumber = 0;
    private String operator = "";
    private boolean isNewInput = true;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        View header = view.findViewById(R.id.header);
        if (header != null) {
            TextView title = header.findViewById(R.id.tvHeaderTitle);
            title.setText("Calculator");
        }

        tvResult = view.findViewById(R.id.tvResult);

        // Number buttons
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9
        };

        for (int id : numberIds) {
            view.findViewById(id).setOnClickListener(this::onNumberClick);
        }

        // Dot
        view.findViewById(R.id.btnDot).setOnClickListener(v -> addDot());

        // Operators
        view.findViewById(R.id.btnPlus).setOnClickListener(v -> setOperator("+"));
        view.findViewById(R.id.btnMinus).setOnClickListener(v -> setOperator("-"));
        view.findViewById(R.id.btnMultiply).setOnClickListener(v -> setOperator("*"));
        view.findViewById(R.id.btnDivide).setOnClickListener(v -> setOperator("/"));

        // Equal
        view.findViewById(R.id.btnEqual).setOnClickListener(v -> calculate());

        // Clear
        view.findViewById(R.id.btnClear).setOnClickListener(v -> clear());

        return view;
    }

    // ---------------- Logic ----------------

    private void onNumberClick(View view) {
        Button btn = (Button) view;

        if (isNewInput) {
            tvResult.setText(btn.getText().toString());
            isNewInput = false;
        } else {
            tvResult.append(btn.getText().toString());
        }
    }

    private void addDot() {
        if (isNewInput) {
            tvResult.setText("0.");
            isNewInput = false;
        } else if (!tvResult.getText().toString().contains(".")) {
            tvResult.append(".");
        }
    }

    private void setOperator(String op) {
        firstNumber = Double.parseDouble(tvResult.getText().toString());
        operator = op;
        isNewInput = true;
    }

    private void calculate() {
        double secondNumber = Double.parseDouble(tvResult.getText().toString());
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber == 0) {
                    tvResult.setText("Error");
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        tvResult.setText(removeDecimalZero(result));
        isNewInput = true;
    }

    private void clear() {
        tvResult.setText("0");
        firstNumber = 0;
        operator = "";
        isNewInput = true;
    }

    private String removeDecimalZero(double value) {
        if (value == (long) value)
            return String.valueOf((long) value);
        else
            return String.valueOf(value);
    }
}
