package com.example.passwords.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwords.R;
import com.example.passwords.database.Password;
import com.example.passwords.database.PasswordRepository;
import com.example.passwords.recycler_view.PasswordAdapter;

import java.util.List;

public class PasswordListFragment extends Fragment {

    private PasswordAdapter adapter;
    private PasswordRepository passwordRepository;

    private FragmentManager fragmentManager;

    public static PasswordListFragment newInstance(){
        return new PasswordListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_list, container, false);

        // 初始化RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化密码仓库
        passwordRepository = new PasswordRepository(getContext());

        // 获取密码列表并设置适配器
        List<Password> passwords = passwordRepository.getAllPasswords();
        adapter = new PasswordAdapter(getContext(), passwords, fragmentManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取父Fragment或者Activity的FragmentManager
        fragmentManager = getParentFragmentManager();
    }
}
