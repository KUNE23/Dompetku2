package com.example.dompetku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etName, etEmail, etPassword;
    Button btnSignUp;
    ImageView ivShowPassword;
    boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btnGoToSignIn = findViewById(R.id.btnGoToSignIn);
        btnGoToSignIn.setOnClickListener(v -> {
            finish();
        });
        ivShowPassword = findViewById(R.id.ivShowPassword);


        ivShowPassword.setOnClickListener(v -> {
            if (isPasswordVisible) {

                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivShowPassword.setAlpha(0.5f);
                isPasswordVisible = false;
            } else {

                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivShowPassword.setAlpha(1.0f);
                isPasswordVisible = true;
            }


            etPassword.setSelection(etPassword.getText().length());
        });
        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            if(name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Mohon isi semua bidang", Toast.LENGTH_SHORT).show();
            } else {
                if(db.insertUser(name, email, pass)) {
                    Toast.makeText(this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke halaman login
                } else {
                    Toast.makeText(this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}