package com.example.passwords.fragment.container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private Button returnButton;
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
        initializeViews(view);
        setUpRecyclerView();

        findButton.setOnClickListener(v -> onFindButtonClick(view));
        returnButton.setOnClickListener(v -> onReturnButtonClick());
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.find_passwords);
        findEditText = view.findViewById(R.id.find_column_name);
        findButton = view.findViewById(R.id.find_button);
        returnButton = view.findViewById(R.id.find_return);

        passwordRepository = new PasswordRepository(getContext());

        view.findViewById(R.id.find_text).setVisibility(View.GONE);
    }

    private void setUpRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void onFindButtonClick(View view) {
        try {
            String query = findEditText.getText().toString().trim();
            List<Password> filteredPasswords = passwordRepository.findPasswordByName(query);
            adapter = new PasswordAdapter(getContext(), filteredPasswords, getParentFragmentManager());
            recyclerView.setAdapter(adapter);

            if (query.isEmpty()) {
                showEmptyQueryMessage(view);
            } else if (filteredPasswords.isEmpty()) {
                showNoResultsMessage(view);
            } else {
                hideMessages(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onReturnButtonClick() {
        PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
        switchFragment(getParentFragmentManager(), passwordListFragment, "find_to_list_fragment");
    }

    private void showEmptyQueryMessage(View view) {
        view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
        view.findViewById(R.id.find_text).setVisibility(View.GONE);
        view.findViewById(R.id.find_search_icon).setVisibility(View.VISIBLE);
    }

    private void showNoResultsMessage(View view) {
        view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
        view.findViewById(R.id.find_text).setVisibility(View.VISIBLE);
        view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
    }

    private void hideMessages(View view) {
        view.findViewById(R.id.find_message).setVisibility(View.GONE);
        view.findViewById(R.id.find_text).setVisibility(View.GONE);
        view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
    }
}
