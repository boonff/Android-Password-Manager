package com.example.passwords.activity;

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

import com.example.passwords.R;
import com.example.passwords.fragment.account.SignupFragment;
import com.example.passwords.fragment.bar.BottomBarFragment;
import com.example.passwords.fragment.container.PasswordListFragment;
import com.example.passwords.key.PasswordUtil;

public class LoginFragment extends Fragment {

    private EditText inputEditText;
    private Button hideButton;
    private Button unlockButton;
    private Button signupButton;
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
        signupButton = view.findViewById(R.id.login_signup);


        hideButton.setOnClickListener(v -> togglePasswordVisibility());

        unlockButton.setOnClickListener(v -> unlockPassword());

        signupButton.setOnClickListener(v -> goToSignup());

        return view;
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

        private void loadMainActivity(){
        // 登录成功后跳转到密码列表页面
        PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
        ((MainActivity)getActivity()).replaceContainerFragment(passwordListFragment, false);

        // 添加底部导航栏
        BottomBarFragment bottomBarFragment = BottomBarFragment.newInstance();
        ((MainActivity)getActivity()).replaceBottomFragment(bottomBarFragment,false);

    }

    private void goToSignup() {
        SignupFragment signupFragment = SignupFragment.newInstance();
        ((MainActivity)getActivity()).replaceContainerFragment(signupFragment,true);
    }
}
