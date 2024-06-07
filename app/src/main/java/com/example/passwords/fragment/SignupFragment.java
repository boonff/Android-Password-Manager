package com.example.passwords.fragment;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.passwords.R;
import com.example.passwords.MainActivity;

public class SignupFragment  extends Fragment {
    EditText inputEditText;
    Button hideButton;
    EditText re_inputEditText;
    Button re_hideButton;
    Button confirmButton;
    private boolean[] isPasswordVisible = {false, false};

    public static SignupFragment newIntence() {
        return new SignupFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        inputEditText = view.findViewById(R.id.signup_input_password);
        hideButton = view.findViewById(R.id.signup_hide_password);
        re_inputEditText = view.findViewById(R.id.signup_re_input_password);
        re_hideButton = view.findViewById(R.id.signup_re_hide_password);
        confirmButton = view.findViewById(R.id.signup_confirm);

        hideButton.setOnClickListener(v -> togglePasswordVisibility(inputEditText, 0));
        re_hideButton.setOnClickListener(v -> togglePasswordVisibility(re_inputEditText, 1));

        confirmButton.setOnClickListener(v -> unlockPassword());


        return view;
    }

    private void togglePasswordVisibility(EditText editText, int index) {
        if (isPasswordVisible[index]) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideButton.setText("😎");
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideButton.setText("🤓");
        }
        isPasswordVisible[index] = !isPasswordVisible[index];
        editText.setSelection(editText.getText().length()); // Move cursor to end of text
    }

    private void unlockPassword() {
        String inputPassword = inputEditText.getText().toString();
        String re_inputPassword = re_inputEditText.getText().toString();

        if (inputPassword.isEmpty()) {
            Toast.makeText(getContext(), "请输入主密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (re_inputPassword.isEmpty()) {
            Toast.makeText(getContext(), "请再次输入主密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inputPassword.equals(re_inputPassword)) {
            Toast.makeText(getContext(), "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add your password registration logic here
        // For example, save the password to shared preferences or a database

        Toast.makeText(getContext(), "密码创建成功", Toast.LENGTH_SHORT).show();
    }

}
