// AddPasswordFragment.java
package com.example.passwords.fragment.container;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;
import com.example.passwords.database.PasswordRepository;
import com.example.passwords.fragment.bar.TopBarGeneratePasswordFragment;
import com.example.passwords.fragment.navigation.FragmentNavigationListener;

public class AddPasswordFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextUrl;
    private Button buttonSave;
    private Button hideButton;
    private Button generateButton;
    private PasswordRepository passwordRepository;
    private boolean isPasswordVisible;
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String URL = "url";
    private FragmentNavigationListener navigationListener;

    public static AddPasswordFragment newInstance() {
        return new AddPasswordFragment();
    }

    public static AddPasswordFragment newInstance(String name, String username, String url, String password) {
        AddPasswordFragment addPasswordFragment = new AddPasswordFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(USERNAME, username);
        args.putString(URL, url);
        args.putString(PASSWORD, password);
        addPasswordFragment.setArguments(args);
        return addPasswordFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigationListener) {
            navigationListener = (FragmentNavigationListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentNavigationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        navigationListener = null;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_password, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextName = view.findViewById(R.id.add_name);
        editTextUsername = view.findViewById(R.id.add_username);
        editTextPassword = view.findViewById(R.id.add_password);
        editTextUrl = view.findViewById(R.id.add_url);
        buttonSave = view.findViewById(R.id.add_save);
        hideButton = view.findViewById(R.id.add_button_hide_password);
        generateButton = view.findViewById(R.id.bottom_bar_generate_button);



        buttonSave.setOnClickListener(v -> savePassword());
        hideButton.setOnClickListener(v -> togglePasswordVisibility());
        generateButton.setOnClickListener(v -> jump_generate());

        // 获取并设置传递过来的值
        if (getArguments() != null) {
            editTextName.setText(getArguments().getString(NAME));
            editTextUsername.setText(getArguments().getString(USERNAME));
            editTextPassword.setText(getArguments().getString(PASSWORD));
            editTextUrl.setText(getArguments().getString(URL));
        }
    }
    public void setPassword(String password){
        editTextPassword.setText(password);
    }
    private void jump_generate() {
        GeneratePasswordFragment generatePasswordFragment = GeneratePasswordFragment.newInstance();
        TopBarGeneratePasswordFragment topBarGeneratePasswordFragment = TopBarGeneratePasswordFragment.newInstance(
                editTextName.getText().toString(),
                editTextUsername.getText().toString(),
                editTextUrl.getText().toString()
        );
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.add_cover_frame, generatePasswordFragment);
        transaction.replace(R.id.top_bar_frame, topBarGeneratePasswordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hideButton.setText("😎");
        } else {
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hideButton.setText("🤓");
        }
        isPasswordVisible = !isPasswordVisible;
        editTextPassword.setSelection(editTextPassword.getText().length()); // Move cursor to end of text
    }

    private void savePassword() {
        passwordRepository = new PasswordRepository(requireContext());
        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(url)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        passwordRepository.insertPassword(name, username, password, url);
        Toast.makeText(requireContext(), "Password saved", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private void clearFields() {
        editTextName.setText("");
        editTextUsername.setText("");
        editTextPassword.setText("");
        editTextUrl.setText("");
    }
}