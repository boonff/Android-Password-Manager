package com.example.passwords.fragment.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;

public class MyAuthFragment extends Fragment {
    FragmentManager fragmentManager;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取父Fragment或者Activity的FragmentManager
        fragmentManager = getParentFragmentManager();
    }

    public void switchFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        //弹出同tag的条目
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        //检查返回栈深度，弹出最旧的条目
        if (fragmentManager.getBackStackEntryCount() >= 3)
            fragmentManager.popBackStack(
                    fragmentManager.getBackStackEntryAt(0).getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            );


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_auth_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
