package com.example.passwords.fragment.bar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.passwords.MainActivity;
import com.example.passwords.R;
import com.example.passwords.fragment.container.AddPasswordFragment;
import com.example.passwords.fragment.container.PasswordListFragment;

public class BottomBarFragment extends Fragment {
    public static BottomBarFragment newInstance() {
        return new BottomBarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_bar, container, false);

        Button addButton = view.findViewById(R.id.button_add_password);
        Button listButton = view.findViewById(R.id.button_password_list);


        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
                ((MainActivity) getActivity()).replaceContainerFragment(passwordListFragment);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AddPasswordFragment addPasswordFragment = AddPasswordFragment.newInstance();
                ((MainActivity) getActivity()).replaceContainerFragment(addPasswordFragment);
            }
        });
        return view;
    }
}
