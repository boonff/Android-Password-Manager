// AddPasswordFragment.java
package com.example.passwords.fragment.container;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passwords.R;
import com.example.passwords.database.PasswordRepository;

public class AddPasswordFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextUrl;
    private Button buttonSave;

    private PasswordRepository passwordRepository;

    public static AddPasswordFragment newInstance() {
        return new AddPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_password, container, false);

        editTextName = view.findViewById(R.id.add_name);
        editTextUsername = view.findViewById(R.id.add_username);
        editTextPassword = view.findViewById(R.id.add_password);
        editTextUrl = view.findViewById(R.id.add_url);
        buttonSave = view.findViewById(R.id.add_save);

        passwordRepository = new PasswordRepository(requireContext());

        buttonSave.setOnClickListener(v -> savePassword());

        return view;
    }


    private void savePassword() {
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