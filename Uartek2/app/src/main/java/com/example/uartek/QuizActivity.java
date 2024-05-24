package com.example.uartek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.uartek.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {
    private ActivityQuizBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> turkishTextList = new ArrayList<>();
    private ArrayList<String> referenceList = new ArrayList<>();
    private String email;
    private DocumentSnapshot currentSnapshot;
    private int wordNumber, askNumber, askNumberCounter = 0, trueQuestion = 0, examTrueOption = 0;
    private String numberOfCorrectGuessesUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = auth.getCurrentUser().getEmail();

        startingQuiz();

        binding.button8.setOnClickListener(v -> getDataWord());
    }

    public void go_back_click(View view) {
        startActivity(new Intent(QuizActivity.this, MenuActivity.class));
        finish();
    }

    private void startingQuiz() {
        firebaseFirestore.collection(email + "quiz")
                .get()
                .addOnSuccessListener(this::initializeQuiz)
                .addOnFailureListener(e -> showToast("Error getting documents: " + e.getMessage()));
    }

    private void initializeQuiz(QuerySnapshot queryDocumentSnapshots) {
        for (DocumentSnapshot document : queryDocumentSnapshots) {
            Map<String, Object> data = document.getData();
            if (data != null) {
                turkishTextList = (ArrayList<String>) data.get("Turkish_text_list");
                askNumber = Integer.parseInt((String) data.get("number_words_ask"));
                wordNumber = turkishTextList != null ? turkishTextList.size() : 0;

                if (wordNumber > 0) {
                    getDataWord();
                } else {
                    showToast("Kayitli kelime bulunmamakta");
                }
            } else {
                showToast("Kayitli kelime bulunmamakta");
            }
        }
    }

    private void getDataWord() {
        if (currentSnapshot != null) {
            processCurrentSnapshot();
        }

        firebaseFirestore.collection(email)
                .get()
                .addOnSuccessListener(this::processDataWord)
                .addOnFailureListener(e -> showToast("Error getting documents: " + e.getMessage()));
    }

    private void processDataWord(QuerySnapshot queryDocumentSnapshots) {
        boolean wordFound = false;
        int attemptCounter = 0;
        while (!wordFound && attemptCounter < wordNumber) {
            int numberWordOrder = getRandomNumber(wordNumber);
            int counter = 1;
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                if (numberWordOrder == counter) {
                    Map<String, Object> data = snapshot.getData();
                    if (data != null) {
                        String reference = snapshot.getReference().toString();
                        if (!referenceList.contains(reference) && canWordBeAsked(data)) {
                            wordFound = true;
                            setQuizData(snapshot, data);
                            break;
                        }
                    }
                }
                counter = (counter % wordNumber) + 1;
            }
            attemptCounter++;
        }
        if (!wordFound) {
            showToast("No suitable word found, you are directed to the main menu, add a new word");
            goBackToMenu();
        }
    }

    private void setQuizData(DocumentSnapshot snapshot, Map<String, Object> data) {
        currentSnapshot = snapshot;
        numberOfCorrectGuessesUpdate = (String) data.get("number_of_correct_guesses");

        String englishText = (String) data.get("English_text");
        String turkishText = (String) data.get("Turkish_text");
        String imageUrl = (String) data.get("image_url");

        Picasso.get().load(imageUrl).into(binding.imageView2);
        binding.textView2.setText(englishText);
        referenceList.add(snapshot.getReference().toString());

        setOptions(turkishText);

        binding.textView7.setText(trueQuestion + " True/" + askNumberCounter + " question");
        binding.textView.setText((askNumberCounter + 1) + ".  Soru");
        askNumberCounter++;
        if (askNumberCounter > askNumber) {
            finishQuiz();
        }
    }

    private void setOptions(String turkishText) {
        examTrueOption = getRandomNumber(2);
        if (examTrueOption == 1) {
            binding.option1RadioButton.setText(turkishText);
            binding.option2RadioButton.setText(getRandomTurkishText(turkishText));
        } else {
            binding.option2RadioButton.setText(turkishText);
            binding.option1RadioButton.setText(getRandomTurkishText(turkishText));
        }
    }

    private String getRandomTurkishText(String exclude) {
        String text;
        do {
            text = turkishTextList.get(getRandomNumber(wordNumber) - 1);
        } while (text.equals(exclude));
        return text;
    }

    private boolean canWordBeAsked(Map<String, Object> data) {
        Timestamp date = (Timestamp) data.get("date_of_correct_guesses");
        String correctGuesses = (String) data.get("number_of_correct_guesses");
        int daysPassed = getDaysDifference(date);

        switch (correctGuesses) {
            case "0":
                return true;
            case "1":
                return daysPassed > 1;
            case "2":
                return daysPassed > 7;
            case "3":
                return daysPassed > 30;
            case "4":
                return daysPassed > 90;
            case "5":
                return daysPassed > 180;
            case "6":
                return daysPassed > 360;
            default:
                return false;
        }
    }

    private int getRandomNumber(int bound) {
        return new Random().nextInt(bound) + 1;
    }

    private int getDaysDifference(Timestamp timestamp) {
        if (timestamp == null) return 0;
        long diffInMillis = new Date().getTime() - timestamp.toDate().getTime();
        return (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    private void processCurrentSnapshot() {
        if (controlOption() == 1) {
            trueQuestion++;
            currentSnapshot.getReference().update("date_of_correct_guesses", FieldValue.serverTimestamp());
            currentSnapshot.getReference().update("number_of_correct_guesses", String.valueOf(Integer.parseInt(numberOfCorrectGuessesUpdate) + 1));
        } else {
            showToast("Yanlış cevap! Doğru cevap: " + getCorrectAnswer());
        }
    }

    private String getCorrectAnswer() {
        String correctAnswer = "";
        if (examTrueOption == 1) {
            correctAnswer = binding.option1RadioButton.getText().toString();
        } else if (examTrueOption == 2) {
            correctAnswer = binding.option2RadioButton.getText().toString();
        }
        return correctAnswer;
    }


    private int controlOption() {
        if (examTrueOption == 1 && binding.option1RadioButton.isChecked()) {
            return 1;
        } else if (examTrueOption == 2 && binding.option2RadioButton.isChecked()) {
            return 1;
        }
        return 0;
    }

    private void showToast(String message) {
        Toast.makeText(QuizActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void finishQuiz() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        intent.putExtra("correctAnswers", trueQuestion);
        intent.putExtra("totalQuestions", askNumberCounter - 1);
        startActivity(intent);
        finish();
    }

    private void goBackToMenu() {
        startActivity(new Intent(QuizActivity.this, MenuActivity.class));
        finish();
    }
}
