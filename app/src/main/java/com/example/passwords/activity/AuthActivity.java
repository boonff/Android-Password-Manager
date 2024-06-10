package com.example.passwords.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;
import com.example.passwords.fragment.auth.ChangePasswordFragment;
import com.example.passwords.fragment.auth.LoginFragment;
import com.example.passwords.fragment.auth.SignupFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (savedInstanceState == null) {
            switchToLogin(false);
        }
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_auth_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void switchToSignup(boolean addToBackStack) {
        loadFragment(SignupFragment.newInstance(), addToBackStack);
    }

    public void switchToLogin(boolean addToBackStack) {
        loadFragment(LoginFragment.newInstance(), addToBackStack);
    }

    public void switchToChange() {
        loadFragment(ChangePasswordFragment.newInstance(), true);
    }
}
