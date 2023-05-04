package com.x.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BasicCalculator extends AppCompatActivity {
    protected Calculator calc = Calculator.INSTANCE;

    protected Button bNum0;
    protected Button bNum1;
    protected Button bNum2;
    protected Button bNum3;
    protected Button bNum4;
    protected Button bNum5;
    protected Button bNum6;
    protected Button bNum7;
    protected Button bNum8;
    protected Button bNum9;

    protected Button bPoint;

    protected Button bAdd;
    protected Button bSubtract;
    protected Button bMultiply;
    protected Button bDivide;
    protected Button bEquals;
    protected Button bPlusMinus;

    protected Button bClear;
    protected Button bAllClear;

    protected TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_calculator);

        initBasicCalculator();
    }

    protected void initBasicCalculator() {
        bNum0 = findViewById(R.id.num0);
        bNum1 = findViewById(R.id.num1);
        bNum2 = findViewById(R.id.num2);
        bNum3 = findViewById(R.id.num3);
        bNum4 = findViewById(R.id.num4);
        bNum5 = findViewById(R.id.num5);
        bNum6 = findViewById(R.id.num6);
        bNum7 = findViewById(R.id.num7);
        bNum8 = findViewById(R.id.num8);
        bNum9 = findViewById(R.id.num9);
        bPoint = findViewById(R.id.point);

        bAdd = findViewById(R.id.add);
        bSubtract = findViewById(R.id.subtract);
        bMultiply = findViewById(R.id.multiply);
        bDivide = findViewById(R.id.divide);
        bEquals = findViewById(R.id.equals);
        bPlusMinus = findViewById(R.id.plusMinus);

        bClear = findViewById(R.id.clear);
        bAllClear = findViewById(R.id.allClear);

        result = findViewById(R.id.result);

        bNum0.setOnClickListener((view) -> {
            numberButtonClick(bNum0.getText().toString());
        });
        bNum1.setOnClickListener((view) -> {
            numberButtonClick(bNum1.getText().toString());
        });
        bNum2.setOnClickListener((view) -> {
            numberButtonClick(bNum2.getText().toString());
        });
        bNum3.setOnClickListener((view) -> {
            numberButtonClick(bNum3.getText().toString());
        });
        bNum4.setOnClickListener((view) -> {
            numberButtonClick(bNum4.getText().toString());
        });
        bNum5.setOnClickListener((view) -> {
            numberButtonClick(bNum5.getText().toString());
        });
        bNum6.setOnClickListener((view) -> {
            numberButtonClick(bNum6.getText().toString());
        });
        bNum7.setOnClickListener((view) -> {
            numberButtonClick(bNum7.getText().toString());
        });
        bNum8.setOnClickListener((view) -> {
            numberButtonClick(bNum8.getText().toString());
        });
        bNum9.setOnClickListener((view) -> {
            numberButtonClick(bNum9.getText().toString());
        });

        bPoint.setOnClickListener((view) -> {
            if (calc.inputPoint()) {
                updateResult();
            }
        });

        bAdd.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.ADD);
        });
        bSubtract.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.SUBTRACT);
        });
        bMultiply.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.MULTIPLY);
        });
        bDivide.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.DIVIDE);
        });
        bEquals.setOnClickListener((view) -> {
            if (!calc.equals()) {
                Toast.makeText(this, R.string.error_operation, Toast.LENGTH_SHORT).show();
            } else {
                updateResult();
            }
        });
        bPlusMinus.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.PLUS_MINUS);
        });

        bClear.setOnClickListener((view) -> {
            calc.clear();
            updateResult();
        });
        bAllClear.setOnClickListener((view) -> {
            calc.allClear();
            updateResult();
        });

        result.setText(String.format("%s", calc.getNumber()));
    }

    private void numberButtonClick(String num) {
        calc.inputNumber(num);
        updateResult();
    }

    protected void operation1ArgButtonClick(Calculator.Operation1Arg op) {
        if (!calc.inputOperation1Arg(op)) {
            Toast.makeText(this, R.string.error_operation, Toast.LENGTH_SHORT).show();
        } else {
            updateResult();
        }
    }

    protected void operation2ArgButtonClick(Calculator.Operation2Arg op) {
        if (!calc.inputOperation2Arg(op)) {
            Toast.makeText(this, R.string.error_operation, Toast.LENGTH_SHORT).show();
        } else {
            updateResult();
        }
    }

    protected void updateResult() {
        result.setText(calc.getNumber());
    }
}