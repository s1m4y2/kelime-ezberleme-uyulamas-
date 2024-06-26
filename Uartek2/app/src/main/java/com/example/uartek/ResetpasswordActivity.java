package com.example.uartek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.uartek.databinding.ActivityResetBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetpasswordActivity extends AppCompatActivity {
    private ActivityResetBinding binding ;
    private FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
    }

    public void reset_button(View view) {


        if(binding.editTextText.getText().toString().isEmpty()){

            Toast.makeText(ResetpasswordActivity.this,"Bir Mail Adresi Giriniz!",Toast.LENGTH_SHORT).show();

        }else {

            auth.sendPasswordResetEmail(binding.editTextText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(ResetpasswordActivity.this, "Sıfırlama Maili Gönderildi!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetpasswordActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        Log.e("hata", task.getException().toString());

                    }

                }
            });

        }}

    public void go_back_click(View view){
        Intent intent = new Intent(ResetpasswordActivity.this,MainActivity.class);
        startActivity(intent);
        finish();


    }

}