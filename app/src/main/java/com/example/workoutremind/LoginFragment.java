package com.example.workoutremind;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutremind.databinding.FragmentLoginBinding;
import com.example.workoutremind.viewmodels.UserViewModel;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        UserViewModel viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null){
                binding.errorMessage.setText("User not logged in");
                //not logged in
            }

            else{
                //logged in
                binding.errorMessage.setText("We are logged in");
                NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });


        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            binding.errorMessage.setText(error);
        });

        binding.signup.setOnClickListener(view -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signupFragment);

        });
        binding.login.setOnClickListener(view -> {
            viewModel.signIn(binding.editTextTextEmailAddress.getText().toString(), binding.editTextTextPassword.getText().toString());
        });

        return binding.getRoot();

    }
}