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

public class FindPasswordFragment extends Fragment {

    private PasswordAdapter adapter;
    private RecyclerView recyclerView;
    private Button findButton;
    private TextView messageTextView;
    Button returnButton;
    private PasswordRepository passwordRepository;
    private EditText findEditText;
    private FragmentManager fragmentManager;

    public static FindPasswordFragment newInstance() {
        return new FindPasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_password, container, false);

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
                adapter = new PasswordAdapter(getContext(), filteredPasswords, fragmentManager);
                recyclerView.setAdapter(adapter);
                // 检查结果列表是否为空
                if(query.isEmpty()){
                    view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_text).setVisibility(View.GONE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.VISIBLE);
                }
                else if (filteredPasswords.isEmpty()) {
                    view.findViewById(R.id.find_message).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_text).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.find_message).setVisibility(View.GONE);
                    view.findViewById(R.id.find_text).setVisibility(View.GONE);
                    view.findViewById(R.id.find_search_icon).setVisibility(View.GONE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        returnButton.setOnClickListener(v -> {
            PasswordListFragment passwordListFragment = PasswordListFragment.newInstance();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            // 替换当前容器中的 Fragment 为 FindPasswordFragment
            transaction.replace(R.id.fragment_container, passwordListFragment);
            // 添加到返回栈（可选）
            transaction.addToBackStack(null);

            transaction.commit();
        });


        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取父Fragment或者Activity的FragmentManager
        fragmentManager = getParentFragmentManager();
    }
}
