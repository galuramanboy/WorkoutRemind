package com.example.workoutremind;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workoutremind.databinding.FragmentSignupBinding;
import com.example.workoutremind.viewmodels.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignupBinding binding = FragmentSignupBinding.inflate(inflater, container, false);
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null){
                binding.errorText.setText("Please provide an email and a password with 8 characters");
                //not logged in
            }

            else{
                //logged in
                binding.errorText.setText("We are logged in");
                NavHostFragment.findNavController(this).navigate(R.id.action_signupFragment_to_homeFragment);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            binding.errorText.setText(error);
        });

        binding.button.setOnClickListener(view -> {
            viewModel.signUp(binding.editTextTextEmailAddress2.getText().toString(), binding.editTextTextPassword2.getText().toString(), binding.editTextTextEmailAddress3.getText().toString(), binding.editTextTextPassword3.getText().toString());

        });

        return binding.getRoot();
    }
}