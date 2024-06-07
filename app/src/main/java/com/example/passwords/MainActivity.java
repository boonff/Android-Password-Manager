package com.example.passwords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.passwords.database.PasswordRepository;
import com.example.passwords.fragment.AddPasswordFragment;
import com.example.passwords.fragment.BottomBarFragment;
import com.example.passwords.fragment.LoginFragment;
import com.example.passwords.fragment.PasswordListFragment;

public class MainActivity extends AppCompatActivity {
    private PasswordRepository passwordRepository;

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
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // 将Fragment添加到回退栈中，以便后退时能返回上一个Fragment
        transaction.commit();
    }

}
