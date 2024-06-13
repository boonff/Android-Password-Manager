package com.example.passwords.fragment.container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwords.R;
import com.example.passwords.database.Password;
import com.example.passwords.database.PasswordRepository;
import com.example.passwords.recycler_view.PasswordAdapter;

import java.util.List;

public class FindPasswordFragment extends MyContainerFragment {

    private PasswordAdapter adapter;
    private RecyclerView recyclerView;
    private Button findButton;
    Button returnButton;
    private PasswordRepository passwordRepository;
    private EditText findEditText;

    public static FindPasswordFragment newInstance() {
        return new FindPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.find_passwords);
        findEditText = view.findViewById(R.id.find_column_name);
        findButton = view.findViewById(R.id.find_button);
        returnButton = view.findViewById(R.id.find_return);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.find_text).setVisibility(View.GONE);

        // 初始化密码仓库
        passwordRepository = new PasswordRepository(getContext());

        findButton.setOnClickListener(v -> {
            try {
                String query = findEditText.getText().toString().trim();
                List<Password> filteredPasswords = passwordRepository.findPasswordByName(query);
                adapter = new PasswordAdapter(getContext(), filteredPasswords, getParentFragmentManager());
                recyclerView.setAdapter(adapter);
                // 检查结果列表是否为空
                if (query.isEmpty()) {
                    view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_text).setVisibility(View.GONE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.VISIBLE);
                } else if (filteredPasswords.isEmpty()) {
                    view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_text).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.find_message).setVisibility(View.GONE);
                    view.findViewById(R.id.find_text).setVisibility(View.GONE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        returnButton.setOnClickListener(v -> {
            PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
            switchFragment(getParentFragmentManager(), passwordListFragment, "find_to_list_fragment");
        });
    }
}
