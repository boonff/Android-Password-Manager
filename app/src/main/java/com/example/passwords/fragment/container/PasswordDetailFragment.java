package com.example.passwords.fragment.container;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.activity.MainActivity;
import com.example.passwords.database.Password;
import com.example.passwords.R;

public class PasswordDetailFragment extends MyFragment {
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";
    private TextView textViewID;
    private TextView textViewName;
    private TextView textViewUsername;
    private TextView textViewPassword;
    private TextView textViewUrl;
    private Button buttonCopyUsername;
    private Button buttonCopyPassword;
    private Button buttonCopyUrl;
    private Button buttonHidePassword;
    private Button editButton;
    private boolean isPasswordVisible = false;

    public static PasswordDetailFragment newInstance(Password password) {
        PasswordDetailFragment fragment = new PasswordDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("password", password);
        fragment.setArguments(args);
        return fragment;
    }

    public static PasswordDetailFragment newInstance(String name, String username, String password, String url) {
        PasswordDetailFragment fragment = new PasswordDetailFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_password_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewID = view.findViewById(R.id.detail_id);
        textViewName = view.findViewById(R.id.detail_name);
        textViewUsername = view.findViewById(R.id.detail_username);
        textViewPassword = view.findViewById(R.id.detail_password);
        textViewUrl = view.findViewById(R.id.detail_url);
        buttonCopyUsername = view.findViewById(R.id.detail_button_copy_username);
        buttonCopyPassword = view.findViewById(R.id.detail_button_copy_password);
        buttonCopyUrl = view.findViewById(R.id.detail_button_copy_url);
        buttonHidePassword = view.findViewById(R.id.detail_button_hide_password);
        editButton = view.findViewById(R.id.detail_edit);
        // 从参数中获取密码对象
        getPasswordFromArgs();
        // 设置复制按钮
        buttonCopyUsername.setOnClickListener(v -> copyToClipboard(textViewUsername.getText().toString()));
        buttonCopyPassword.setOnClickListener(v -> copyToClipboard(textViewPassword.getText().toString()));
        buttonCopyUrl.setOnClickListener(v -> copyToClipboard(textViewUrl.getText().toString()));
        // 设置显示/隐藏密码
        buttonHidePassword.setOnClickListener(v -> togglePasswordVisibility());
        //跳转编辑界面
        editButton.setOnClickListener(v -> goto_Edit());
    }



    private void goto_Edit() {
        int id = Integer.parseInt(textViewID.getText().toString());

        EditPasswordFragment editPasswordFragment = EditPasswordFragment.newInstance(
                id,
                textViewName.getText().toString(),
                textViewUsername.getText().toString(),
                textViewPassword.getText().toString(),
                textViewUrl.getText().toString()
        );
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_main_container, editPasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    private void getPasswordFromArgs() {

        Bundle args = getArguments();
        if (args != null) {
            Password password = args.getParcelable("password");
            textViewName.setText(args.getString(NAME));
            textViewUsername.setText(args.getString(USERNAME));
            textViewPassword.setText(args.getString(PASSWORD));
            textViewUrl.setText(args.getString(URL));
            if (password != null) {
                // 设置密码详细信息
                textViewID.setText(Integer.toString(password.getId()));
                textViewName.setText(password.getName());
                textViewUsername.setText(password.getUsername());
                textViewPassword.setText(password.getPassword());
                textViewUrl.setText(password.getUrl());
            }
        }
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("password", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireContext(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // 隐藏密码
            textViewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            buttonHidePassword.setText("😎");
        } else {
            // 显示密码
            textViewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            buttonHidePassword.setText("🤓");
        }
        isPasswordVisible = !isPasswordVisible;
    }
}
