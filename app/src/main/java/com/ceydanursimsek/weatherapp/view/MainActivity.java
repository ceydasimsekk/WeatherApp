package com.ceydanursimsek.weatherapp.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ceydanursimsek.weatherapp.R;
import com.ceydanursimsek.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    //default username-gmail-password
    private static final String gmail = "cns.gmail.com";
    private static final String username = "ceyda";
    private static final String password = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        // Renkleri varsayılan hale döndürme
        binding.username.setTextColor(Color.BLACK);
        binding.password.setTextColor(Color.BLACK);
        binding.checkBox1.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        binding.checkBox2.setButtonTintList(ColorStateList.valueOf(Color.BLACK));

        // Kullanıcın girdiği bilgileri alma
        String input = binding.username.getText().toString();
        String Password = binding.password.getText().toString();
        boolean isCheckBox1Checked = binding.checkBox1.isChecked();
        boolean isCheckBox2Checked = binding.checkBox2.isChecked();

        boolean hasError = false;

        if (!(input.equals(gmail)) && !(input.equals(username)) || !(Password.equals(password))) {
            binding.username.setTextColor(Color.RED);
            binding.password.setTextColor(Color.RED);
            Toast.makeText(this, "username or password is wrong", Toast.LENGTH_SHORT).show();
            hasError = true;  // Hata var
        }

        if (!isCheckBox1Checked || !isCheckBox2Checked) {
            if (!isCheckBox1Checked) {
                binding.checkBox1.setButtonTintList(ColorStateList.valueOf(Color.RED));
            }
            if (!isCheckBox2Checked) {
                binding.checkBox2.setButtonTintList(ColorStateList.valueOf(Color.RED));
            }
            hasError = true;  // Hata var
        }

        if (!hasError) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }

        /*Kullanıcı girişini düzelttiğinde kırmızı rengin otomatik olarak normale dönmesi için, TextWatcher kullanarak EditText
        bileşenlerindeki metin değişikliklerini izleyebilir ve kullanıcı doğru girdiyi yazmaya başladığında metin rengini tekrar siyah
        yapabilirsiniz. */
        // TextWatcher ekleme: Kullanıcı metni değiştirdikçe renk normale dönsün
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.username.setTextColor(Color.BLACK);  // Rengi siyaha döndürme
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.password.setTextColor(Color.BLACK);  // Rengi siyaha döndürme
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}