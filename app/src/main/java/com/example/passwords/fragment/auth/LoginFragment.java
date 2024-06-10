package com.example.passwords.fragment.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwords.R;
import com.example.passwords.activity.AuthActivity;
import com.example.passwords.activity.MainActivity;
import com.example.passwords.key.PasswordUtil;

public class LoginFragment extends Fragment {

    private EditText inputEditText;
    private Button hideButton;
    private Button unlockButton;
    private Button signupButton;
    private Button changeButton;
    private TextView topBarTextView;
    private boolean isPasswordVisible = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inputEditText = view.findViewById(R.id.login_input_password);
        hideButton = view.findViewById(R.id.login_hide_password);
        unlockButton = view.findViewById(R.id.login_unlock);
        signupButton = view.findViewById(R.id.login_signup);
        topBarTextView = view.findViewById(R.id.top_bar_text);
        changeButton = view.findViewById(R.id.login_change_password);

        topBarTextView.setText("登录");

        hideButton.setOnClickListener(v -> togglePasswordVisibility());
        unlockButton.setOnClickListener(v -> unlockPassword());
        signupButton.setOnClickListener(v -> goToSignup());
        changeButton.setOnClickListener(v -> goToChange());
        // 如果密码不存在，跳转注册页面
        checkPasswordHashExists();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideButton.setText("😎");
        } else {
            inputEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideButton.setText("🤓");
        }
        isPasswordVisible = !isPasswordVisible;
        inputEditText.setSelection(inputEditText.getText().length()); // Move cursor to end of text
    }

    private boolean isPasswordCorrect(String inputPassword) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPref.getString("main_password_hash", null);

        return storedPassword != null && PasswordUtil.checkPassword(inputPassword, storedPassword);
    }

    private void unlockPassword() {
        String inputPassword = inputEditText.getText().toString();

        if (inputPassword.isEmpty()) {
            Toast.makeText(getContext(), "请输入主密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isPasswordCorrect(inputPassword)) {
            Toast.makeText(getContext(), "解锁成功", Toast.LENGTH_SHORT).show();
            loadMainActivity();
        } else {
            Toast.makeText(getContext(), "密码错误，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Finish AuthActivity so the user cannot go back to it.
    }

    private void goToSignup() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPref.getString("main_password_hash", null);

        if (storedPassword != null) {
            Toast.makeText(getContext(), "密码已存在，不能重复注册", Toast.LENGTH_SHORT).show();
            return;
        }
        ((AuthActivity) getActivity()).switchToSignup();
    }
    private void goToChange(){
        ((AuthActivity) getActivity()).switchToChange();
    }

    private void checkPasswordHashExists() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPref.getString("main_password_hash", null);
        if (storedPassword == null) {
            goToSignup();
        }
    }
}
