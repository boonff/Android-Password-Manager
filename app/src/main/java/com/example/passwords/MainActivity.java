package com.example.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.passwords.fragment.LoginFragment;

public class MainActivity extends AppCompatActivity {

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
    public void replaceContainerFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // 将Fragment添加到回退栈中，以便后退时能返回上一个Fragment
        transaction.commit();
    }

    public void replaceBottomFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_bottom_bar, fragment);
        transaction.addToBackStack(null); // 将Fragment添加到回退栈中，以便后退时能返回上一个Fragment
        transaction.commit();
    }

}
