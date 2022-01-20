package com.example.workoutremind.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.workoutremind.models.User;
import com.example.workoutremind.repository.UserRepository;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserViewModel extends ViewModel {
    UserRepository repository;
    MutableLiveData<User> user = new MutableLiveData<>();
    MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public UserViewModel(){
        repository = new UserRepository();
        errorMessage.setValue("");
        repository.setAuthStateChangedListener(user -> {
            this.user.postValue(user);
        });
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void signIn(String username, String password){
        errorMessage.setValue("");
        if(username == null || username.isEmpty()){
            errorMessage.setValue("Email cannot be blank");
            return;
        }
        if(password == null || password.isEmpty()){
            errorMessage.setValue("Password cannot be blank");
            return;
        }
        repository.signIn(username, password,
                e -> errorMessage.postValue(e.getMessage()));
    }

    public void signUp(String username, String password, String userConfirm, String passwordConfirm){
        errorMessage.setValue("");
        if(username == null || username.isEmpty()){
            errorMessage.setValue("Email cannot be blank");
            return;
        }
        if(!username.equals(userConfirm)){
            errorMessage.setValue("Email fields must match");
            return;
        }
        if(password == null || password.isEmpty()){
            errorMessage.setValue("Password cannot be blank");
            return;
        }
        if(!password.equals(passwordConfirm)){
            errorMessage.setValue("Passwords must match");
            return;
        }
        if(password.length() != 8){
            errorMessage.setValue("Password must be at least 8 characters");
            return;
        }
        repository.signUp(username, password,
                e -> errorMessage.postValue(e.getMessage()));
    }

    public void logout(){
        repository.logout();
    }
}
