package com.example.passwords.fragment.bar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passwords.activity.MainActivity;
import com.example.passwords.R;
import com.example.passwords.fragment.container.AddPasswordFragment;
import com.example.passwords.fragment.container.GeneratePasswordFragment;
import com.example.passwords.fragment.container.PasswordListFragment;

public class BottomBarFragment extends Fragment {
    public static BottomBarFragment newInstance() {
        return new BottomBarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_bar, container, false);

        Button addButton = view.findViewById(R.id.bottom_bar_add_button);
        Button listButton = view.findViewById(R.id.bottom_bar_password_list);
        Button generateButton = view.findViewById(R.id.bottom_bar_generate_button);


        listButton.setOnClickListener(v -> {
                PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
                ((MainActivity) getActivity()).replaceContainerFragment(passwordListFragment, true);

        });

        generateButton.setOnClickListener(v -> {
            GeneratePasswordFragment generatePasswrodFragment = GeneratePasswordFragment.newInstance();
            ((MainActivity) getActivity()).replaceContainerFragment(generatePasswrodFragment, true);
        });

        addButton.setOnClickListener(v -> {
                AddPasswordFragment addPasswordFragment = AddPasswordFragment.newInstance();
                ((MainActivity) getActivity()).replaceContainerFragment(addPasswordFragment,true);

        });

        return view;
    }
}
