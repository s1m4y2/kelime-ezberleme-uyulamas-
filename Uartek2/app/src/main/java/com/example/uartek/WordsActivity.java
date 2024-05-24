package com.example.uartek;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uartek.adapter.PostAdapter;
import com.example.uartek.adapter.PostAdapter_2;
import com.example.uartek.databinding.ActivityWordsBinding;
import com.example.uartek.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class WordsActivity extends AppCompatActivity {
    private FirebaseAuth auth ;
    private FirebaseFirestore firebaseFirestore;
    private  String email;
    ArrayList<Post> postArrayList;
    private ActivityWordsBinding binding;
    PostAdapter postAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWordsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        postArrayList = new ArrayList<Post>();
        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.recyclerView.setAdapter(postAdapter);
    }

    private void getData(){
        firebaseFirestore.collection(email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(WordsActivity.this, error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        // burada document içindekı değerlerı al
                        Map<String,Object> data = snapshot.getData();
                        String English_text = (String) data.get("English_text");
                        String Turkish_text = (String) data.get("Turkish_text");
                        String number_of_correct_guesses = (String) data.get("number_of_correct_guesses");
                        String sentences = (String) data.get("sentences");
                        String word_subject = (String) data.get("word_subject");
                        String image_url = (String) data.get("image_url");
                        Post post = new Post(English_text,Turkish_text,number_of_correct_guesses,sentences,word_subject,image_url);
                        postArrayList.add(post);

                    }
                    postAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    public void go_back_click(View view){
        Intent intent = new Intent(WordsActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();


    }
}