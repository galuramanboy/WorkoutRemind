package com.example.workoutremind.repository;

import com.example.workoutremind.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserRepository {
    FirebaseAuth auth;

    public static interface  OnFailureListener{
        public void onFailure(Exception e);
    }
    public static interface OnAuthStateChangedListener {
        public void onAuthStateChanged(User user);
    }


    public UserRepository(){
        auth = FirebaseAuth.getInstance();
    }

    public void setAuthStateChangedListener(OnAuthStateChangedListener listener) {
        auth.addAuthStateListener(auth -> {
            listener.onAuthStateChanged(getCurrentUser());
        });
    }

    public User getCurrentUser(){
        User user = new User();
        FirebaseUser fbUser = auth.getCurrentUser();
        if(fbUser == null) return null;
        user.uid = fbUser.getUid();
        user.email = fbUser.getEmail();
        return user;
    }

    public void signIn(String username, String password, OnFailureListener onFailureListener ) {
        auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println(task.getException().getMessage());
                onFailureListener.onFailure(task.getException());
            }
        });
    }

    public void signUp(String username, String password, OnFailureListener onFailureListener){
        auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(resultTask -> {
            System.out.println(resultTask.isSuccessful());
            if (!resultTask.isSuccessful()) {
                System.out.println(resultTask.getException().getMessage());
                onFailureListener.onFailure(resultTask.getException());
            }
        });
    }

    public void logout(){
        auth.signOut();
    }
}
