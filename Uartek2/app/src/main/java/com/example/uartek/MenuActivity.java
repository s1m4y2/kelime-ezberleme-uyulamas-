package com.example.uartek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MenuActivity extends AppCompatActivity {
    public String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void add_word_section(View view){
        Intent intent = new Intent(MenuActivity.this,AddWordActivity.class);
        startActivity(intent);
        finish();
    }

    public void words_button(View view){
        Intent intent = new Intent(MenuActivity.this,WordsActivity.class);
        startActivity(intent);
        finish();
    }
    public void enter_the_exam_button(View view){
        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
        startActivity(intent);
        finish();
    }

    public void settings(View view){
        Intent intent = new Intent(MenuActivity.this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void achievement_file(View view){
        Intent intent = new Intent(MenuActivity.this,AnalysisActivity.class);
        startActivity(intent);
        finish();
    }
}