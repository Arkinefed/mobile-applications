package com.x.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonSwitchToBasicCalculator = findViewById(R.id.buttonBasicCalculator);
        mButtonSwitchToBasicCalculator.setOnClickListener(view -> {
            Intent intent = new Intent(this, BasicCalculator.class);
            startActivity(intent);
        });

        Button mButtonSwitchToAdvancedCalculator = findViewById(R.id.buttonAdvancedCalculator);
        mButtonSwitchToAdvancedCalculator.setOnClickListener(view -> {
            Intent intent = new Intent(this, AdvancedCalculator.class);
            startActivity(intent);
        });

        Button mButtonSwitchToAbout = findViewById(R.id.buttonAbout);
        mButtonSwitchToAbout.setOnClickListener(view -> {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        });

        Button mButtonClose = findViewById(R.id.buttonClose);
        mButtonClose.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });
    }
}