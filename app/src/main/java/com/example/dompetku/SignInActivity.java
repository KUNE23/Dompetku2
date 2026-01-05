package com.example.dompetku;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class SignInActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etEmail, etPassword;
    Button btnLogin;
    ImageView ivShowPassword;
    boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigin);
        Button btnGoToSignUp = findViewById(R.id.btnGoToSignUp);
        btnGoToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
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
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            if(db.checkUser(email, pass)) {
                Cursor cursor = db.getUserData(email);
                String name = "";
                if (cursor.moveToFirst()) {
                    name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME));
                }
                cursor.close();

                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email atau Password Salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}