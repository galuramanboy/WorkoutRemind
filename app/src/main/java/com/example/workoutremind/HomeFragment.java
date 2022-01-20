package com.example.workoutremind;

import android.os.Bundle;

import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutremind.databinding.FragmentHomeBinding;
import com.example.workoutremind.models.Workout;
import com.example.workoutremind.viewmodels.UserViewModel;
import com.example.workoutremind.viewmodels.WorkoutViewModel;
import com.google.android.gms.ads.AdRequest;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        UserViewModel userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        WorkoutViewModel workoutViewModel = new ViewModelProvider(getActivity()).get(WorkoutViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) return;
            binding.workouts.setAdapter(new WorkoutsAdapter(workoutViewModel.getWorkouts(user.uid), workout -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_workoutItemFragment);
                workoutViewModel.setSelectedWorkout(workout);
            }));
            binding.workouts.setLayoutManager(new LinearLayoutManager(getContext()));
        });
        binding.logout.setOnClickListener(view -> {
            userViewModel.logout();
            workoutViewModel.clearWorkouts();
            NavHostFragment.findNavController(this).navigate(R.id.action_homeFragment_to_loginFragment);
        });
        binding.bannerad.loadAd(new AdRequest.Builder().build());
        return binding.getRoot();
    }
}