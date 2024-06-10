package com.example.passwords.fragment.container;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.passwords.R;
import com.example.passwords.activity.MainActivity;
import com.example.passwords.database.PasswordRepository;

public class EditPasswordFragment extends AddPasswordFragment {
    TextView textViewID;
    public static EditPasswordFragment newInstance() {
        return new EditPasswordFragment();
    }

    public static EditPasswordFragment newInstance(int id, String name, String username, String url, String password) {
        EditPasswordFragment editPasswordFragment = new EditPasswordFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putString(NAME, name);
        args.putString(USERNAME, username);
        args.putString(URL, url);
        args.putString(PASSWORD, password);
        editPasswordFragment.setArguments(args);
        return editPasswordFragment;
    }

    public static EditPasswordFragment newInstance(String name, String username, String url, String password) {
        EditPasswordFragment editPasswordFragment = new EditPasswordFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(USERNAME, username);
        args.putString(URL, url);
        args.putString(PASSWORD, password);
        editPasswordFragment.setArguments(args);
        return editPasswordFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textViewID = view.findViewById(R.id.edit_id);
        editTextName = view.findViewById(R.id.edit_name);
        editTextUsername = view.findViewById(R.id.edit_username);
        editTextPassword = view.findViewById(R.id.edit_password);
        editTextUrl = view.findViewById(R.id.edit_url);
        buttonSave = view.findViewById(R.id.edit_save);
        hideButton = view.findViewById(R.id.edit_button_hide_password);
        generateButton = view.findViewById(R.id.edit_generate_button);



        buttonSave.setOnClickListener(v -> savePassword());
        hideButton.setOnClickListener(v -> togglePasswordVisibility());
        generateButton.setOnClickListener(v -> goto_generate());

        // 获取并设置传递过来的值
        getPasswordFromArgs();
    }

    private void getPasswordFromArgs() {
        if (getArguments() != null) {
            editTextName.setText(getArguments().getString(NAME));
            editTextUsername.setText(getArguments().getString(USERNAME));
            editTextPassword.setText(getArguments().getString(PASSWORD));
            editTextUrl.setText(getArguments().getString(URL));
        }
    }

    private void goto_generate() {
        GeneratePasswordFragment generatePasswordFragment = GeneratePasswordFragment.newInstance(
                "edit",
                getBundleID(),
                editTextName.getText().toString(),
                editTextUsername.getText().toString(),
                editTextPassword.getText().toString(),
                editTextUrl.getText().toString()
        );
        switchFragment(fragmentManager, generatePasswordFragment, "edit_fragment");
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
        int id = getBundleID();

        String name = editTextName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String url = editTextUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(url)) {
            Toast.makeText(requireContext(), "需要填入所有参数", Toast.LENGTH_SHORT).show();
            return;
        }

        passwordRepository.updatePasswordById(id, name, username, password, url);
        PasswordDetailFragment passwordDetailFragment = PasswordDetailFragment.newInstance(
                name,
                username,
                password,
                url
        );
        ((MainActivity) getActivity()).replaceContainerFragment(passwordDetailFragment, true);
        Toast.makeText(requireContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
    }

    private int getBundleID(){
        Bundle args = getArguments();
        if (args != null)
            return args.getInt(ID);
        else return -1;
    }
}
