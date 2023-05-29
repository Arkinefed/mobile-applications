package com.x.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox cbWork = findViewById(R.id.work_checkbox);
        CheckBox cbHobby = findViewById(R.id.hobby_checkbox);
        CheckBox cbFamily = findViewById(R.id.family_checkbox);

        EditText notificationTime = findViewById(R.id.notification_time);

        SharedPreferences sharedPref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        cbWork.setChecked(sharedPref.getBoolean("cat.work", true));
        cbWork.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("cat.work", b);
            editor.apply();

            TaskListActivity.getWeakReference().updateTaskList();
        });

        cbHobby.setChecked(sharedPref.getBoolean("cat.hobby", true));
        cbHobby.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("cat.hobby", b);
            editor.apply();

            TaskListActivity.getWeakReference().updateTaskList();
        });

        cbFamily.setChecked(sharedPref.getBoolean("cat.family", true));
        cbFamily.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("cat.family", b);
            editor.apply();

            TaskListActivity.getWeakReference().updateTaskList();
        });

        notificationTime.setText(String.valueOf(sharedPref.getInt("nt", 1)));
        notificationTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    editor.putInt("nt", Integer.parseInt(editable.toString()));
                    editor.apply();

                    TaskListActivity.getWeakReference().updateTaskList();
                }
            }
        });
    }
}