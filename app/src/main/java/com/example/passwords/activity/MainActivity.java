package com.example.passwords.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.passwords.R;
import com.example.passwords.fragment.navigation.FragmentNavigationListener;

public class MainActivity extends AppCompatActivity implements FragmentNavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        LoginFragment loginFragment = new LoginFragment();
        transaction.add(R.id.fragment_container, loginFragment);

        // 提交事务
        transaction.commit();
    }

    // 用于替换Fragment的方法
    public void replaceContainerFragment(Fragment fragment, boolean unback) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (unback) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceBottomFragment(Fragment fragment, boolean unback) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_bottom_bar, fragment);
        if (unback) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


}
