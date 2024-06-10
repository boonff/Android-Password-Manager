package com.example.passwords.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;
import com.example.passwords.fragment.bar.BottomBarFragment;
import com.example.passwords.fragment.container.PasswordListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadHome();
        }
    }

    private void loadHome() {
        // 加载密码列表页面和底部导航栏
        PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
        replaceContainerFragment(passwordListFragment, false);

        BottomBarFragment bottomBarFragment = BottomBarFragment.newInstance();
        replaceBottomFragment(bottomBarFragment, false);
    }

    public void replaceContainerFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_main_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceBottomFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_bottom_bar, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }


}
