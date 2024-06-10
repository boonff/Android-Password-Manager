package com.example.passwords.fragment.container;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MyFragment extends Fragment {
    protected FragmentManager fragmentManager;

    protected static final String BUTTON_TYPE = "button_type";
    protected static final String NAME = "name";
    protected static final String USERNAME = "username";
    protected static final String PASSWORD = "password";
    protected static final String URL = "url";

    protected static final  String ID = "id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取父Fragment或者Activity的FragmentManager
        fragmentManager = getParentFragmentManager();
    }

}
