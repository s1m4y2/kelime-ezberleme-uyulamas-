package com.example.uartek;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uartek.databinding.ActivitySettingBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingBinding binding ;
    private FirebaseAuth auth ;
    private String email;
    private FirebaseFirestore firebaseFirestore;
    private String number_words_ask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email= auth.getCurrentUser().getEmail();
        binding.mail.setText(email);
        getData();

    }


    public void sign_out(View view){
        auth.signOut();
        Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void change_password(View view){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(SettingsActivity.this, "Sıfırlama Maili Gönderildi!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(SettingsActivity.this, "hata"+ task.getException().toString(), Toast.LENGTH_SHORT).show();


                }

            }
        });

    }

    private ListenerRegistration listenerRegistration;

    private void getData() {
        String collectionName = email + "quiz";
        System.out.println(collectionName);


        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        listenerRegistration = firebaseFirestore.collection(collectionName)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(SettingsActivity.this, "Listen failed: " + e, Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : snapshots) {
                                // Burada document içindeki değerleri al
                                Map<String, Object> data = snapshot.getData();
                                assert data != null;
                                number_words_ask = (String) data.get("number_words_ask");
                                binding.sorusayi.setText(number_words_ask);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity yok edildiğinde dinleyiciyi kaldır
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    public void change_ask_number(){
        String collectionName = email + "quiz";
        firebaseFirestore.collection(collectionName)
                .limit(1) // Belirli bir koşulu belirtiyorsunuz
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Belgeyi güncelle
                                document.getReference().update("number_words_ask", binding.soruSayiGiris.getText().toString());
                            }
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public void soru_sayi_degistirme_click(View view) {
        String collectionName = email + "quiz";
        System.out.println(collectionName);
        int ask_number = Integer.parseInt(binding.soruSayiGiris.getText().toString());
        firebaseFirestore.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                if (data != null) {
                                    ArrayList<String> Turkish_text_list = (ArrayList<String>) data.get("Turkish_text_list");
                                    int word_number=Turkish_text_list.size();
                                    if (Turkish_text_list != null) {
                                        String sizeAsString = String.valueOf(word_number);
                                        if (word_number < ask_number){
                                            Toast.makeText(SettingsActivity.this, "Soru sayisi kelime sayisindan fazla"+ word_number +" adet kelime mevcut", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            change_ask_number();
                                        }
                                    } else {
                                        Toast.makeText(SettingsActivity.this, "Kayitli kelime bulunmamakta", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(SettingsActivity.this, "Kayitli kelime bulunmamakta", Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            System.out.println("Error getting documents: " + task.getException());
                            Toast.makeText(SettingsActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}

