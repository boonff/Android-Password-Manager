package com.example.passwords.fragment.container;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.StatFs;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.passwords.R;
import com.example.passwords.activity.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneratePasswordFragment extends Fragment {


    private static final String BUTTON_TYPE = "button_type";
    private static final String NAME = "name";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";

    TextView topBarText;
    TextView generatedPasswordTextView;
    Button copyButton;
    Button generateButton;
    SeekBar lengthSeekBar;
    SwitchCompat switchUppercase;
    SwitchCompat switchLowercase;
    SwitchCompat switchNumbers;
    SwitchCompat switchSpecialChars;
    SeekBar minNumbersSeekBar;
    SeekBar minSymbolsSeekBar;

    public static GeneratePasswordFragment newInstance() {
        return new GeneratePasswordFragment();
    }


    public static GeneratePasswordFragment newInstance(String buttonType, String name, String username, String password, String url) {
        GeneratePasswordFragment fragment = new GeneratePasswordFragment();
        Bundle args = new Bundle();
        args.putString(BUTTON_TYPE, buttonType);
        args.putString(NAME, name);
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate_password, container, false);
        createAddButtonView(view);
        createEditButtonView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topBarText = view.findViewById(R.id.top_bar_text);
        generatedPasswordTextView = view.findViewById(R.id.generated_password_text);
        copyButton = view.findViewById(R.id.generate_copy_button);
        generateButton = view.findViewById(R.id.generate_button);
        lengthSeekBar = view.findViewById(R.id.generate_length_seekbar);
        switchUppercase = view.findViewById(R.id.generate_switch_uppercase);
        switchLowercase = view.findViewById(R.id.generate_switch_lowercase);
        switchNumbers = view.findViewById(R.id.generate_switch_numbers);
        switchSpecialChars = view.findViewById(R.id.generate_switch_special_chars);
        minNumbersSeekBar = view.findViewById(R.id.generate_min_numbers_seekbar);
        minSymbolsSeekBar = view.findViewById(R.id.generate_min_symbols_seekbar);

        topBarText.setText("生成器");

        // 设置开关默认为打开状态
        switchUppercase.setChecked(true);
        switchLowercase.setChecked(true);
        switchNumbers.setChecked(true);
        switchSpecialChars.setChecked(true);
        //初始化时生成密码
        generatePassword();

        generateButton.setOnClickListener(v -> generatePassword());
        copyButton.setOnClickListener(v -> copy());


        switchingLimits();

    }

    private void selectPasswordToAdd() {
        String password = getGeneratePassword();
        if (getArguments() == null)
            return;
        AddPasswordFragment addPasswordFragment = AddPasswordFragment.newInstance(
                getArguments().getString(NAME),
                getArguments().getString(USERNAME),
                getArguments().getString(URL),
                password
        );


        ((MainActivity) getActivity()).replaceContainerFragment(addPasswordFragment, true);
    }

    private void selectPasswordToEdit() {
        String password = getGeneratePassword();
        if (getArguments() == null)
            return;
        EditPasswordFragment editPasswordFragment = EditPasswordFragment.newInstance(
                getArguments().getString(NAME),
                getArguments().getString(USERNAME),
                getArguments().getString(URL),
                password
        );

        ((MainActivity) getActivity()).replaceContainerFragment(editPasswordFragment, true);
    }

    private void createAddButtonView(View view) {

        if (getArguments() == null)
            return;
        if (getArguments().getString(BUTTON_TYPE).equals("add")) {
            // 找到包含公共布局文件的FrameLayout
            FrameLayout topBarFrame = view.findViewById(R.id.top_bar_frame);

            // 创建按钮
            Button button = new Button(getContext());
            button.setText("🖊");
            button.setBackgroundColor(0x00000000);
            button.setTextSize(32);

            // 设置按钮的布局参数
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            button.setLayoutParams(params);

            // 添加按钮到FrameLayout
            topBarFrame.addView(button);

            button.setOnClickListener(v -> selectPasswordToAdd());
        }
    }

    private void createEditButtonView(View view) {
        if(getArguments() == null)
            return;
        if (getArguments().getString(BUTTON_TYPE).equals("edit")) {
            // 找到包含公共布局文件的FrameLayout
            FrameLayout topBarFrame = view.findViewById(R.id.top_bar_frame);

            // 创建按钮
            Button button = new Button(getContext());
            button.setText("🖊");
            button.setBackgroundColor(0x00000000);
            button.setTextSize(32);

            // 设置按钮的布局参数
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            button.setLayoutParams(params);

            // 添加按钮到FrameLayout
            topBarFrame.addView(button);

            button.setOnClickListener(v -> selectPasswordToEdit());
        }
    }

    public String getGeneratePassword() {
        return generatedPasswordTextView.getText().toString();
    }

    private void copy() {
        String password = generatedPasswordTextView.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Generated Password", password);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "密码已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    private void switchingLimits() {
        CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked && !switchUppercase.isChecked() && !switchLowercase.isChecked() && !switchNumbers.isChecked() && !switchSpecialChars.isChecked()) {
                    // 如果用户尝试关闭所有开关，保持开关的开启状态，并显示提示消息
                    buttonView.setChecked(true);
                    Toast.makeText(getActivity(), "至少需要选择一个字符类型", Toast.LENGTH_SHORT).show();
                }
            }
        };

        switchUppercase.setOnCheckedChangeListener(switchListener);
        switchLowercase.setOnCheckedChangeListener(switchListener);
        switchNumbers.setOnCheckedChangeListener(switchListener);
        switchSpecialChars.setOnCheckedChangeListener(switchListener);
    }

    private void generatePassword() {
        int length = lengthSeekBar.getProgress();
        boolean includeUppercase = switchUppercase.isChecked();
        boolean includeLowercase = switchLowercase.isChecked();
        boolean includeNumbers = switchNumbers.isChecked();
        boolean includeSpecialChars = switchSpecialChars.isChecked();
        int minNumbers = minNumbersSeekBar.getProgress();
        int minSymbols = minSymbolsSeekBar.getProgress();

        String generatedPassword = generatePassword(length, includeUppercase, includeLowercase, includeNumbers, includeSpecialChars, minNumbers, minSymbols);
        generatedPasswordTextView.setText(generatedPassword);
    }

    //生成随机密码
    private String generatePassword(int length, boolean includeUppercase, boolean includeLowercase, boolean includeNumbers, boolean includeSpecialChars, int minNumbers, int minSymbols) {
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*";

        // 计算每个字符类型需要的数量
        int requiredNumbers = includeNumbers ? minNumbers : 0;
        int requiredSymbols = includeSpecialChars ? minSymbols : 0;
        int requiredChars = length - requiredNumbers - requiredSymbols;

        // 生成密码中包含的字符类型列表
        List<String> charTypes = new ArrayList<>();
        if (includeUppercase) charTypes.add(uppercaseChars);
        if (includeLowercase) charTypes.add(lowercaseChars);
        if (includeNumbers) charTypes.add(numberChars);
        if (includeSpecialChars) charTypes.add(specialChars);

        // 随机生成密码
        for (int i = 0; i < requiredChars; i++) {
            String charSet = charTypes.get(random.nextInt(charTypes.size()));
            password.append(charSet.charAt(random.nextInt(charSet.length())));
        }

        // 补充数字和特殊字符
        for (int i = 0; i < requiredNumbers; i++) {
            password.append(numberChars.charAt(random.nextInt(numberChars.length())));
        }
        for (int i = 0; i < requiredSymbols; i++) {
            password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }

        // 将密码打乱顺序
        List<Character> charList = password.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(charList);
        StringBuilder shuffledPassword = new StringBuilder(charList.size());
        for (Character character : charList) {
            shuffledPassword.append(character);
        }

        return shuffledPassword.toString();
    }


}
