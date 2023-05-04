package com.x.calculator;

import android.os.Bundle;
import android.widget.Button;

public class AdvancedCalculator extends BasicCalculator {
    protected Button bSin;
    protected Button bCos;
    protected Button bTg;

    protected Button bSqrt;
    protected Button bPercent;

    protected Button bPow2;
    protected Button bPowY;

    protected Button bLn;
    protected Button bLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calculator);

        initAdvancedCalculator();
        initBasicCalculator();
    }

    protected void initAdvancedCalculator() {
        bSin = findViewById(R.id.sin);
        bCos = findViewById(R.id.cos);
        bTg = findViewById(R.id.tg);

        bSqrt = findViewById(R.id.sqrt);
        bPercent = findViewById(R.id.percent);

        bPow2 = findViewById(R.id.pow_2);
        bPowY = findViewById(R.id.pow_y);

        bLn = findViewById(R.id.ln);
        bLog = findViewById(R.id.log);

        bSin.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.SIN);
        });
        bCos.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.COS);
        });
        bTg.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.TG);
        });

        bSqrt.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.SQRT);
        });
        bPercent.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.PERCENT);
        });

        bPow2.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.POW_2);
        });
        bPowY.setOnClickListener((view) -> {
            operation2ArgButtonClick(Calculator.Operation2Arg.POW_Y);
        });

        bLn.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.LN);
        });
        bLog.setOnClickListener((view) -> {
            operation1ArgButtonClick(Calculator.Operation1Arg.LOG);
        });
    }
}