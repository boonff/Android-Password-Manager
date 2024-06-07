package com.example.passwords.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passwords.database.Password;
import com.example.passwords.R;

public class PasswordDetailFragment extends Fragment {
    public static PasswordDetailFragment newInstance() {
        return new PasswordDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_detail, container, false);

        TextView textViewName = view.findViewById(R.id.detail_name);
        TextView textViewUsername = view.findViewById(R.id.detail_username);
        TextView textViewPassword = view.findViewById(R.id.detail_password);
        TextView textViewUrl = view.findViewById(R.id.detail_url);

        // 从参数中获取密码对象
        Bundle args = getArguments();
        if (args != null) {
            Password password = args.getParcelable("password");
            if (password != null) {
                // 设置密码详细信息
                textViewName.setText(password.getName());
                textViewUsername.setText(password.getUsername());
                textViewPassword.setText(password.getPassword());
                textViewUrl.setText(password.getUrl());
            }
        }

        return view;
    }
}
