package com.example.uartek;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultTextView = findViewById(R.id.resultTextView);
        Button goToMenuButton = findViewById(R.id.goToMenuButton);

        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("correctAnswers", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);

        String resultText = "Doğru: " + correctAnswers + "\nYanlış: " + (totalQuestions - correctAnswers);
        resultTextView.setText(resultText);

        goToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MenuActivity.class));
                finish();
            }
        });
    }
}
