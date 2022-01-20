package com.example.workoutremind;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.example.workoutremind.databinding.FragmentSignupBinding;
import com.example.workoutremind.databinding.FragmentWorkoutItemBinding;
import com.example.workoutremind.models.Workout;
import com.example.workoutremind.viewmodels.UserViewModel;
import com.example.workoutremind.viewmodels.WorkoutViewModel;

import java.util.Calendar;

public class WorkoutItemFragment extends Fragment {
    private boolean isSaving = false;
    private boolean active;


    @SuppressLint("ShortAlarm")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentWorkoutItemBinding binding = FragmentWorkoutItemBinding.inflate(inflater, container, false);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        WorkoutViewModel workoutViewModel = new ViewModelProvider(getActivity()).get(WorkoutViewModel.class);

        Workout selectedWorkout = workoutViewModel.getSelectedWorkout().getValue();
        if (selectedWorkout != null) {
            binding.switch1.setChecked(selectedWorkout.isNotify());
            if (selectedWorkout.isNotify()){
                binding.time.setHour(selectedWorkout.getHour());
                binding.time.setMinute(selectedWorkout.getMinute());
                binding.time.setVisibility(View.VISIBLE);
            }
            binding.dayView.setText(selectedWorkout.getDay() + "");
            binding.plan.setText(selectedWorkout.getPlan() + "");

        }
        workoutViewModel.getSaving().observe(getViewLifecycleOwner(), saving -> {
            if (!isSaving && saving) {
                isSaving = true;
            }
            else if (isSaving && !saving) {
                //transition back
                NavHostFragment.findNavController(this).navigate(R.id.action_workoutItemFragment_to_homeFragment);
            }
        });

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.save.setOnClickListener(view -> {
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                if (binding.switch1.isChecked()){
                    int hour = binding.time.getHour();
                    int minute = binding.time.getMinute();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, getDay(selectedWorkout.getDay()));

                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

//                    //this should move any day/time before current time's alarm to 7 days in the future
//                    Calendar now = Calendar.getInstance();
//                    now.set(Calendar.SECOND, 0);
//                    now.set(Calendar.MILLISECOND, 0);
//                    if (calendar.before(now)) {    //this condition is used for future reminder that means your reminder not fire for past time
//                        System.out.println("This was set before");
//                        calendar.add(Calendar.DATE, 7);
//                    }


                    Intent intent = new Intent(getActivity(), ReminderService.class);
                    intent.putExtra("Day", selectedWorkout.getDay() + "");

                    //cancel previous alarm for the specified day
                    alarmManager.cancel(PendingIntent.getService(getActivity(), getDay(selectedWorkout.getDay()), intent, 0));

                    //set alarm with unique id being the day int value
                    PendingIntent pendingIntent = PendingIntent.getService(getActivity(), getDay(selectedWorkout.getDay()), intent, 0);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
                }
                else {
                    // if switch is turned off, then cancel any current alarm for that day
                    Intent intent = new Intent(getActivity(), ReminderService.class);
                    alarmManager.cancel(PendingIntent.getService(getActivity(), getDay(selectedWorkout.getDay()), intent, 0));
                }
                workoutViewModel.updateWorkout(selectedWorkout, binding.plan.getText().toString(), active, binding.time.getHour(), binding.time.getMinute());
            });
        });

//        binding.button2.setOnClickListener(view -> {
//            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
//            Intent intent = new Intent(getActivity(), ReminderService.class);
//            intent.putExtra("Day", selectedWorkout.getDay());
//            PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, 0);
//            alarmManager.set(
//                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                    SystemClock.elapsedRealtime() + 5000,
//                    pendingIntent
//            );
//        });


        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    active = true;
                    binding.time.setHour(selectedWorkout.getHour());
                    binding.time.setMinute(selectedWorkout.getMinute());
                    binding.time.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    active = false;
                    binding.time.setVisibility(View.INVISIBLE);
                }
            }
        });

        return binding.getRoot();
    }
    public int getDay(String day){
        int hi = 0;
        switch (day) {
            case "Monday":
                hi = 1;
                break;
            case "Tuesday":
                hi = 2;
                break;
            case "Wednesday":
                hi = 3;
                break;
            case "Thursday":
                hi = 4;
                break;
            case "Friday":
                hi = 5;
                break;
            case "Saturday":
                hi = 6;
                break;
            case "Sunday":
                hi = 7;
                break;
        }
        return hi;
    }
}
