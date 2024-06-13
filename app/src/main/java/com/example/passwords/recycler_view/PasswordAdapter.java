package com.example.passwords.recycler_view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.passwords.database.PasswordRepository;
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
                        .replace(R.id.fragment_main_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // 设置列表项的长按监听器
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // 显示删除对话框
                new AlertDialog.Builder(context)
                        .setTitle("删除密码")
                        .setMessage("你确定要删除这个密码吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 删除密码
                                PasswordRepository passwordRepository = new PasswordRepository(context);
                                passwordRepository.deletePassword(password);
                                passwords.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, passwords.size());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
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
