package com.example.uartek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uartek.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding ;
    private FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void sign_up_button_click(View view){
        Intent intent = new Intent(MainActivity.this,SignActivity.class);
        startActivity(intent);
        finish();

    }
    public void login_button_click(View view){
        String password=binding.editTextPassword.getText().toString();
        String email=binding.editTextmail.getText().toString();

        if (password.equals("") || email.equals("")){
            Toast.makeText(MainActivity.this, "Password and e-mail cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Kullanıcı oluşturma başarılı
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                        startActivity(intent);
                        finish();
                        // Başka bir eylem yapılabilir, örneğin oturum açma ekranına geri dönme
                    } else {
                        // Kullanıcı oluşturma başarısız oldu, hatayı göster
                        Toast.makeText(this, "login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                }}

    public void forgot_password_button(View view){
        Intent intent = new Intent(MainActivity.this,ResetpasswordActivity.class);
        startActivity(intent);
        finish();
    }
    }
