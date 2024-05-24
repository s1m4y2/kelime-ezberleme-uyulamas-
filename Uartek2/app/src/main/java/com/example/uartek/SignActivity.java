package com.example.uartek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.uartek.databinding.ActivitySignBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class SignActivity extends AppCompatActivity {
    private ActivitySignBinding binding ;
    private FirebaseAuth auth ;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void created_user_database(){
        String  email = binding.editTextEmail.getText().toString();
        String  number_words_ask= "0";
        ArrayList<String> Turkish_text_list = new ArrayList<>();
        HashMap<String, Object> postData = new HashMap<>();
        postData.put("number_words_ask",number_words_ask);
        postData.put("Turkish_text_list",Turkish_text_list);
        firebaseFirestore.collection(email+"quiz").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SignActivity.this, "User created", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignActivity.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sign_up_button_click2(View view) {

        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();
        String verify_password = binding.editTextConfirmPassword.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(SignActivity.this, "Password and e-mail cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(verify_password)) {
            Toast.makeText(SignActivity.this, "Passwords do not match each other", Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Kullanıcı oluşturma başarılı
                            created_user_database();
                            Intent intent = new Intent(SignActivity.this,MenuActivity.class);
                            startActivity(intent);
                            finish();
                            // Başka bir eylem yapılabilir, örneğin oturum açma ekranına geri dönme
                        } else {
                            // Kullanıcı oluşturma başarısız oldu, hatayı göster
                            Toast.makeText(this, "Failed to create user: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void go_back_click(View view){
        Intent intent = new Intent(SignActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }
}