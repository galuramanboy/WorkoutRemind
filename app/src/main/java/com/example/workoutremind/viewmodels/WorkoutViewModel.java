package com.example.workoutremind.viewmodels;

import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutremind.models.Workout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorkoutViewModel extends ViewModel {
    FirebaseFirestore db;
    ObservableArrayList<Workout> workouts;
    MutableLiveData<Boolean> saving = new MutableLiveData<>();
    MutableLiveData<Workout> selectedWorkout = new MutableLiveData<>();

    public WorkoutViewModel() {
        db = FirebaseFirestore.getInstance();
        saving.setValue(false);
    }

    public void createWorkout(String day, String plan, String userId, int hour, int min){
        Workout wk = new Workout(day, plan, false, userId, hour, min);
        db.collection("workouts").document(wk.getUserId() + "_" + wk.getDay()).set(wk).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                workouts.add(0, wk);
            }
            else {
                //handle error
            }
        });
    }

    public ObservableArrayList<Workout> getWorkouts(String userId) {
        if (workouts == null) {
            workouts = new ObservableArrayList<>();
            loadWorkouts(userId);
        }
        return workouts;
    }

    private void loadWorkouts(String userId) {
        db.collection("workouts").whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Workout> temp = task.getResult().toObjects(Workout.class);
                        Collections.sort(temp);
                        workouts.addAll(temp);
                        if (workouts.isEmpty()){
                            // create initial workout weekday set here
                            createWorkout("Sunday", "", userId, -1, -1);
                            createWorkout("Saturday", "", userId, -1, -1);
                            createWorkout("Friday", "", userId, -1, -1);
                            createWorkout("Thursday", "", userId, -1, -1);
                            createWorkout("Wednesday", "", userId, -1, -1);
                            createWorkout("Tuesday", "", userId, -1, -1);
                            createWorkout("Monday", "", userId, -1, -1);
                        }
                    }
                });
    }

    public void updateWorkout(Workout workout, String plan, boolean notify, int hour, int minute){
        saving.setValue(true);
        workout.setPlan(plan);
        workout.setNotify(notify);
        workout.setHour(hour);
        workout.setMinute(minute);

        db.collection("workouts").document(workout.getUserId() + "_" + workout.getDay()).set(workout).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int i = workouts.indexOf(workout);
                workouts.set(i, workout);
            }
            else {
                //handle error
            }
            saving.setValue(false);
        });
    }

    public void clearWorkouts() {
        workouts = null;
    }

    public MutableLiveData<Boolean> getSaving() {
        return saving;
    }

    public MutableLiveData<Workout> getSelectedWorkout() {
        return selectedWorkout;
    }

    public void setSelectedWorkout(Workout selectedWorkout) {
        this.selectedWorkout.setValue(selectedWorkout);
    }


}
