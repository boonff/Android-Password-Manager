package com.example.passwords.fragment.bar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;
import com.example.passwords.fragment.container.AddPasswordFragment;
import com.example.passwords.fragment.container.GeneratePasswordFragment;

public class TopBarGeneratePasswordFragment extends Fragment {

    private Button selectButton;
    private TextView passwordTextView;

    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";


    public static TopBarGeneratePasswordFragment newInstance(String name, String username, String url) {
        TopBarGeneratePasswordFragment topBarGeneratePasswordFragment = new TopBarGeneratePasswordFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(PASSWORD, username);
        args.putString(URL, url);
        topBarGeneratePasswordFragment.setArguments(args);
        return topBarGeneratePasswordFragment;
    }



    public static TopBarGeneratePasswordFragment newInstance() {
        return new TopBarGeneratePasswordFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_bar_generate_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectButton = view.findViewById(R.id.top_bar_generate_password_select_button);

        selectButton.setOnClickListener(v -> selectPassword());

    }

    private String getPassword(){
        // 获取AddPasswordFragment的实例
        FragmentManager fragmentManager = getParentFragmentManager();
        GeneratePasswordFragment generatePasswordFragment = (GeneratePasswordFragment) fragmentManager.findFragmentById(R.id.add_cover_frame);

        return generatePasswordFragment.getGeneratePassword();
    }

    //选择密码并返回添加页面
    private void selectPassword() {
        try {
            String password = getPassword();
            AddPasswordFragment addPasswordFragment = AddPasswordFragment.newInstance(
                    getArguments().getString(NAME),
                    getArguments().getString(USERNAME),
                    getArguments().getString(URL),
                    password
            );

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main_container, addPasswordFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
