package com.example.passwords.fragment.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.example.passwords.key.PasswordUtil;

public class ChangePasswordFragment extends MyAuthFragment {

    private TextView top_bar_text;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText re_newPasswordEditText;
    private Button hideOldPasswordButton;
    private Button hideNewPasswordButton;
    private Button re_hideNewPasswordButton;
    private Button changePasswordButton;
    private boolean isOldPasswordVisible = false;
    private boolean[] isNewPasswordVisibles = {false, false};

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        oldPasswordEditText = view.findViewById(R.id.change_old_password);
        newPasswordEditText = view.findViewById(R.id.change_new_password);
        re_newPasswordEditText = view.findViewById(R.id.change_re_new_password);
        hideOldPasswordButton = view.findViewById(R.id.change_hide_old_password);
        hideNewPasswordButton = view.findViewById(R.id.change_hide_new_password);
        re_hideNewPasswordButton = view.findViewById(R.id.change_re_hide_new_password);
        changePasswordButton = view.findViewById(R.id.change_password_button);
        top_bar_text = view.findViewById(R.id.top_bar_text);

        top_bar_text.setText("修改密码");

        hideOldPasswordButton.setOnClickListener(v -> toggleOldPasswordVisibility());
        hideNewPasswordButton.setOnClickListener(v -> toggleNewPasswordVisibility(hideNewPasswordButton, newPasswordEditText, 0));
        re_hideNewPasswordButton.setOnClickListener(v -> toggleNewPasswordVisibility(re_hideNewPasswordButton, re_newPasswordEditText, 1));
        changePasswordButton.setOnClickListener(v -> changePassword());
    }

    private void toggleOldPasswordVisibility() {
        if (isOldPasswordVisible) {
            oldPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideOldPasswordButton.setText("😎");
        } else {
            oldPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideOldPasswordButton.setText("🤓");
        }
        isOldPasswordVisible = !isOldPasswordVisible;
        oldPasswordEditText.setSelection(oldPasswordEditText.getText().length());
    }

    private void toggleNewPasswordVisibility(Button hideButton, EditText editText, int index) {
        if (isNewPasswordVisibles[index]) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideButton.setText("😎");
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideButton.setText("🤓");
        }
        isNewPasswordVisibles[index] = !isNewPasswordVisibles[index];
        editText.setSelection(editText.getText().length());
    }

    private boolean isOldPasswordCorrect(String inputPassword) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPref.getString("main_password_hash", null);
        return storedPassword != null && PasswordUtil.checkPassword(inputPassword, storedPassword);
    }

    private void changePassword() {
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String re_newPassword = re_newPasswordEditText.getText().toString();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getContext(), "请输入所有必要信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(re_newPassword)) {
            Toast.makeText(getContext(), "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOldPasswordCorrect(oldPassword)) {
            String newHashedPassword = PasswordUtil.hashPassword(newPassword);
            SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("main_password_hash", newHashedPassword);
            editor.apply();

            Toast.makeText(getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
            goToLogin();
        } else {
            Toast.makeText(getContext(), "旧密码不正确，请重试", Toast.LENGTH_SHORT).show();
        }
    }
    private void goToLogin() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        switchFragment(fragmentManager, loginFragment, "login_fragment");
    }
}
