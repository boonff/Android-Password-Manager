package com.example.passwords.fragment.container;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentTransaction;

import com.example.passwords.R;
import com.example.passwords.fragment.bar.TopBarGeneratePasswordFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneratePasswordFragment extends Fragment {


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

    public static GeneratePasswordFragment newInstance(){
        return new GeneratePasswordFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_generate_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    public String getGeneratePassword(){
        return generatedPasswordTextView.getText().toString();
    }

    private void copy(){
        String password = generatedPasswordTextView.getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Generated Password", password);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "密码已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }
    private void switchingLimits(){
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
    private void generatePassword(){
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
