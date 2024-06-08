package com.example.passwords.recycler_view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwords.R;
import com.example.passwords.database.Password;
import com.example.passwords.fragment.container.PasswordDetailFragment;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<Password> passwords;
    private Context context;
    private FragmentManager fragmentManager;

    // 构造函数，接受上下文、密码列表和FragmentManager作为参数
    public PasswordAdapter(Context context, List<Password> passwords, FragmentManager fragmentManager) {
        this.context = context;
        this.passwords = passwords;
        this.fragmentManager = fragmentManager;
    }

    // 创建ViewHolder实例
    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用布局填充器从布局文件中创建视图
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_password, parent, false);
        // 返回新的ViewHolder实例
        return new PasswordViewHolder(view);
    }

    // 绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        // 获取当前位置的密码对象
        Password password = passwords.get(position);
        // 将密码名称设置到ViewHolder中的textViewName上
        holder.textViewName.setText(password.getName());
        // 将密码URL设置到ViewHolder中的textViewUrl上
        holder.textViewUrl.setText(password.getUrl());
        // 设置列表项的点击监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个 Bundle 对象，用于传递数据给新的 Fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("password", password);

                // 创建一个新的 Fragment 实例
                PasswordDetailFragment fragment = new PasswordDetailFragment();
                fragment.setArguments(bundle);

                // 启动新的 Fragment 来显示详细信息
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    // 获取密码列表的大小
    @Override
    public int getItemCount() {
        return passwords.size();
    }

    // ViewHolder类，用于持有每个密码列表项的视图控件
    public class PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewUrl;

        // ViewHolder的构造函数，接受一个视图参数
        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            // 在构造函数中找到并保存密码名称和URL的TextView
            textViewName = itemView.findViewById(R.id.item_name);
            textViewUrl = itemView.findViewById(R.id.item_url);
        }
    }
}
