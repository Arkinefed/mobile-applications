package com.x.todo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTaskDialogFragment extends DialogFragment {
    private final TasksAdapter tasksAdapter;
    private final TaskListActivity activity;

    private EditText title;
    private EditText description;
    private TextView date;
    private TextView time;
    private CheckBox notification;
    private Spinner categorySpinner;
    private TextView chosenAttachmentsCount;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;

    private List<Uri> attachments = new ArrayList<>();

    public AddTaskDialogFragment(TasksAdapter tasksAdapter, TaskListActivity activity) {
        this.tasksAdapter = tasksAdapter;
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(), uris -> {
            attachments = uris;

            chosenAttachmentsCount.setText(String.valueOf(uris.size()));
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.dialog_add_task, null);

        title = ll.findViewById(R.id.title);
        description = ll.findViewById(R.id.description);
        date = ll.findViewById(R.id.date);
        Button chooseDate = ll.findViewById(R.id.choose_date);
        time = ll.findViewById(R.id.time);
        Button chooseTime = ll.findViewById(R.id.choose_time);
        notification = ll.findViewById(R.id.notification);
        categorySpinner = ll.findViewById(R.id.category_spinner);
        chosenAttachmentsCount = ll.findViewById(R.id.chosen_attachments_count);
        Button chooseAttachments = ll.findViewById(R.id.choose_attachments);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String formattedDateString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, (month + 1), day);
        date.setText(formattedDateString);

        final int[] selYear = {year};
        final int[] selMonth = {month};
        final int[] selDay = {day};
        final int[] selHour = {hour};
        final int[] selMinute = {minute};

        chooseDate.setOnClickListener(view -> {
            new DatePickerDialog(
                    activity,
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
                            Toast.makeText(activity, R.string.not_valid_date, Toast.LENGTH_SHORT).show();
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
                    activity,
                    (v, hourOfDay, m) -> {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selYear[0], selMonth[0], selDay[0], hourOfDay, m);

                        if (selectedCalendar.getTimeInMillis() >= Calendar.getInstance().getTimeInMillis()) {
                            selHour[0] = hourOfDay;
                            selMinute[0] = m;

                            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, m);

                            time.setText(selectedTime);
                        } else {
                            Toast.makeText(activity, R.string.not_valid_time, Toast.LENGTH_SHORT).show();
                        }
                    },
                    hour,
                    minute,
                    true
            ).show();
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.categories, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        chooseAttachments.setOnClickListener(view -> {
            pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType((VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });

        builder.setView(ll)
                .setPositiveButton(R.string.add, (dialog, id) -> {
                    LocalDateTime dateNow = LocalDateTime.now();

                    Task task = new Task(
                            title.getText().toString(),
                            description.getText().toString(),
                            dateNow,
                            LocalDateTime.parse(date.getText().toString() + time.getText().toString(), DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm")),
                            notification.isChecked(),
                            categorySpinner.getSelectedItem().toString(),
                            dateNow.toString());

                    for (Uri u : attachments) {
                        DocumentFile df = DocumentFile.fromSingleUri(activity, u);

                        String outputFolder = activity.getFilesDir() + "/" + task.getFolderName() + "/";
                        String output = outputFolder + df.getName();

                        try {
                            Files.createDirectories(Paths.get(outputFolder));
                            Files.copy(activity.getContentResolver().openInputStream(u), Paths.get(output));

                            task.addAttachment(output);
                        } catch (IOException e) {
                            Toast.makeText(activity, R.string.cannot_copy_file, Toast.LENGTH_SHORT).show();
                        }
                    }

                    Thread dbThread = new Thread(() -> {
                        TaskDatabase.getInstance(activity).taskDao().insert(task);
                    });

                    dbThread.start();

                    try {
                        dbThread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    final Task[] lastInserted = {null};

                    Thread dbThread2 = new Thread(() -> {
                        lastInserted[0] = TaskDatabase.getInstance(activity).taskDao().findLastInserted();
                    });

                    dbThread2.start();

                    try {
                        dbThread2.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (notification.isChecked()) {
                        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

                        Intent alarmIntent = new Intent(activity, NotificationReceiver.class);
                        alarmIntent.putExtra("id", lastInserted[0].getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, lastInserted[0].getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                        SharedPreferences sharedPref = activity.getSharedPreferences("pref", Context.MODE_PRIVATE);
                        long notificationTime = (long) sharedPref.getInt("nt", 1) * 60 * 1000;

                        long whenFire = lastInserted[0].getDeadline().toInstant(ZoneOffset.UTC).toEpochMilli() - LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - notificationTime;

                        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + whenFire, pendingIntent);
                    }

                    activity.updateTaskList();
                    tasksAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> AddTaskDialogFragment.this.getDialog().cancel());
        return builder.create();
    }
}
