package com.example.passwords.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwords.MainActivity;
import com.example.passwords.R;

public class LoginFragment extends Fragment {

    private EditText inputEditText;
    private Button hideButton;
    private Button unlockButton;
    private boolean isPasswordVisible = false;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        inputEditText = view.findViewById(R.id.login_input_password);
        hideButton = view.findViewById(R.id.login_hide_password);
        unlockButton = view.findViewById(R.id.login_unlock);

        // Set hide/show password functionality
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Set unlock button functionality
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlockPassword();
            }
        });

        return view;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideButton.setText("🤓");
        } else {
            inputEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideButton.setText("😎");
        }
        isPasswordVisible = !isPasswordVisible;
        inputEditText.setSelection(inputEditText.getText().length()); // Move cursor to end of text
    }

    private void unlockPassword() {
        String inputPassword = inputEditText.getText().toString();

        if (inputPassword.isEmpty()) {
            Toast.makeText(getContext(), "请输入主密码", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPref = getContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE);
        String storedPassword = sharedPref.getString("main_password", null);

        if (storedPassword != null && storedPassword.equals(inputPassword)) {
            Toast.makeText(getContext(), "解锁成功", Toast.LENGTH_SHORT).show();

            PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
            ((MainActivity)getActivity()).replaceFragment(passwordListFragment);

            BottomBarFragment bottomBarFragment = BottomBarFragment.newInstance();
            ((MainActivity)getActivity()).replaceFragment(bottomBarFragment);

        } else {
            Toast.makeText(getContext(), "密码错误，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}
