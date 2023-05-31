package com.x.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {
    private int id;
    private Task task;

    private EditText title;
    private EditText description;
    private TextView whenCreated;
    private TextView date;
    private Button chooseDate;
    private TextView time;
    private Button chooseTime;
    private CheckBox status;
    private CheckBox notification;
    private Spinner category;
    private TextView attachmentCount;
    private LinearLayout attachments;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        toolbar = findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            id = getIntent().getIntExtra("id", 0);
        } else {
            id = savedInstanceState.getInt("id");
        }

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        whenCreated = findViewById(R.id.when_created);
        date = findViewById(R.id.date);
        chooseDate = findViewById(R.id.choose_date);
        time = findViewById(R.id.time);
        chooseTime = findViewById(R.id.choose_time);
        status = findViewById(R.id.status);
        notification = findViewById(R.id.notification);
        category = findViewById(R.id.category);
        attachmentCount = findViewById(R.id.attachment_count);
        attachments = findViewById(R.id.attachments);

        Thread dbThread = new Thread(() -> {
            task = TaskDatabase.getInstance(this).taskDao().findTaskById(id);
        });

        dbThread.start();

        try {
            dbThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(task.getTitle());

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        whenCreated.setText(task.getWhenCreated().toString());

        LocalDateTime deadline = task.getDeadline();

        int year = deadline.getYear();
        int month = deadline.getMonthValue() - 1;
        int day = deadline.getDayOfMonth();
        int hour = deadline.getHour();
        int minute = deadline.getMinute();

        String formattedDateString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, (month + 1), day);
        date.setText(formattedDateString);

        final int[] selYear = {year};
        final int[] selMonth = {month};
        final int[] selDay = {day};
        final int[] selHour = {hour};
        final int[] selMinute = {minute};

        chooseDate.setOnClickListener(view -> {
            new DatePickerDialog(
                    this,
                    (v, selectedYear, selectedMonth, selectedDay) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay, selHour[0], selMinute[0]);

                        if (selectedCalendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis()) {
                            selYear[0] = selectedYear;
                            selMonth[0] = selectedMonth;
                            selDay[0] = selectedDay;

                            String formattedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, (selectedMonth + 1), selectedDay);
                            date.setText(formattedDate);
                        } else {
                            Toast.makeText(this, R.string.not_valid_date, Toast.LENGTH_SHORT).show();
                        }
                    },
                    year,
                    month,
                    day
            ).show();
        });

        String currentTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        time.setText(currentTime);

        chooseTime.setOnClickListener(view -> {
            new TimePickerDialog(
                    this,
                    (v, hourOfDay, m) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selYear[0], selMonth[0], selDay[0], hourOfDay, m);

                        if (selectedCalendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis()) {
                            selHour[0] = hourOfDay;
                            selMinute[0] = m;

                            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, m);

                            time.setText(selectedTime);
                        } else {
                            Toast.makeText(this, R.string.not_valid_time, Toast.LENGTH_SHORT).show();
                        }
                    },
                    hour,
                    minute,
                    true
            ).show();
        });

        status.setChecked(task.isFinished());
        notification.setChecked(task.isNotification());

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(spinnerAdapter);

        category.setSelection(spinnerAdapter.getPosition(task.getCategory()));
        attachmentCount.setText(String.valueOf(task.getAttachments().size()));

        for (String attachment : task.getAttachments()) {
            ImageView iv = new ImageView(this);

            iv.setImageURI(Uri.parse(attachment));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int dp = 7;
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

            layoutParams.setMargins(px, px, px, px);

            iv.setLayoutParams(layoutParams);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            iv.setAdjustViewBounds(true);

            attachments.addView(iv);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("id", id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            task.setTitle(title.getText().toString());
            task.setDescription(description.getText().toString());

            task.setDeadline(LocalDateTime.parse(date.getText().toString() + time.getText().toString(), DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")));

            task.setFinished(status.isChecked());
            task.setNotification(notification.isChecked());

            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

            Intent alarmIntent = new Intent(this, NotificationReceiver.class);
            alarmIntent.putExtra("id", task.getId());

            if (!task.isNotification() || task.isFinished()) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getId(), alarmIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT);

                alarmManager.cancel(pendingIntent);
            } else {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                SharedPreferences sharedPref = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
                long notificationTime = (long) sharedPref.getInt("nt", 1) * 60 * 1000;

                long whenFire = task.getDeadline().toInstant(ZoneOffset.UTC).toEpochMilli() - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - notificationTime;

                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + whenFire, pendingIntent);
            }

            task.setCategory(category.getSelectedItem().toString());

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(task.getTitle());

            Thread dbThread = new Thread(() -> {
                TaskDatabase.getInstance(this).taskDao().update(task);
            });

            dbThread.start();

            try {
                dbThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            TaskListActivity.getWeakReference().updateTaskList();

            Toast.makeText(this, R.string.task_updated, Toast.LENGTH_SHORT).show();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}