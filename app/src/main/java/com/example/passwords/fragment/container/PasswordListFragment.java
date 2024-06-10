package com.example.passwords.fragment.container;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwords.R;
import com.example.passwords.database.Password;
import com.example.passwords.database.PasswordRepository;
import com.example.passwords.recycler_view.PasswordAdapter;

import java.util.List;

public class PasswordListFragment extends Fragment {

    private PasswordAdapter adapter;

    RecyclerView recyclerView;
    TextView textView;
    Button findButton;
    private PasswordRepository passwordRepository;

    private FragmentManager fragmentManager;

    public static PasswordListFragment newInstance(){
        return new PasswordListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_password_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.list_passwords);
        textView = view.findViewById(R.id.list_password_count);
        findButton = view.findViewById(R.id.list_go_find);
        // 初始化RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 初始化密码仓库
        passwordRepository = new PasswordRepository(getContext());
        //passwordRepository.clearAllPasswords();

        // 获取密码列表并设置适配器
        List<Password> passwords = passwordRepository.getAllPasswords();
        adapter = new PasswordAdapter(getContext(), passwords, fragmentManager);
        recyclerView.setAdapter(adapter);
        //项目数量
        textView.setText(Integer.toString(passwords.size()));

        findButton.setOnClickListener(v -> {
            FindPasswordFragment findPasswordFragment = FindPasswordFragment.newInstance();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // 替换当前容器中的 Fragment 为 FindPasswordFragment
            transaction.replace(R.id.fragment_main_container, findPasswordFragment);
            transaction.commit();

        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取父Fragment或者Activity的FragmentManager
        fragmentManager = getParentFragmentManager();
    }


}
