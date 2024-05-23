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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {
    private ActivityQuizBinding binding;
    private FirebaseAuth auth;
    private String email;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<String> Turkish_text_list = new ArrayList<>();
    private ArrayList<String> reference_list = new ArrayList<>();
    private int word_number;
    private int ask_number;
    private int counter;
    private int ask_number_counter=0;
    private DocumentSnapshot currentSnapshot;
    int exam_true_option=0;
    String number_of_correct_guesses_update;
    int True_question=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = auth.getCurrentUser().getEmail();

        // Sayfa açıldığında bir kez starting_quiz metodunu çağır
        starting_quiz();

        // Her butona tıklanışında get_Data_word metodunu çağır
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_Data_word();
            }
        });
    }

    public void go_back_click(View view) {
        Intent intent = new Intent(QuizActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void starting_quiz() {
        String collectionName = email + "quiz";
        firebaseFirestore.collection(collectionName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = document.getData();
                            if (data != null) {
                                Turkish_text_list = (ArrayList<String>) data.get("Turkish_text_list");
                                ask_number = Integer.parseInt((String) data.get("number_words_ask"));
                                word_number = Turkish_text_list != null ? Turkish_text_list.size() : 0;

                                if (word_number > 0) {
                                    get_Data_word();
                                } else {
                                    Toast.makeText(QuizActivity.this, "Kayitli kelime bulunmamakta", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(QuizActivity.this, "Kayitli kelime bulunmamakta", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error getting documents: " + e.getMessage());
                        Toast.makeText(QuizActivity.this, "Error getting documents: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void get_Data_word() {
        System.out.println("TDD");
        counter = 1;
        firebaseFirestore.collection(email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean wordFound = false;
                        int attemptCounter = 0;
                        while (!wordFound && attemptCounter < word_number) {
                            int number_word_order = get_random_number(word_number);
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                if (number_word_order == counter) {
                                    Map<String, Object> data = snapshot.getData();
                                    if (data != null) {
                                        String a = snapshot.getReference().toString();
                                        if (reference_list.contains(a)) {
                                            System.out.println("a");
                                        } else {
                                            Timestamp date = (Timestamp) data.get("date_of_correct_guesses");
                                            String number_of_correct_guesses = (String) data.get("number_of_correct_guesses");
                                            int can_word_asked = Can_word_asked(date, number_of_correct_guesses);

                                            if (can_word_asked == 1) {
                                                int option_control = control_option(exam_true_option);
                                                if (option_control==1){
                                                    True_question = True_question +1;
                                                    String updatedNumberOfCorrectGuesses = String.valueOf(Integer.parseInt(number_of_correct_guesses_update) + 1);
                                                    currentSnapshot.getReference().update("date_of_correct_guesses", FieldValue.serverTimestamp());
                                                    currentSnapshot.getReference().update("number_of_correct_guesses", updatedNumberOfCorrectGuesses);
                                                }
                                                else if (option_control==0){
                                                    binding.textView2.setText("FALSE");
                                                }
                                                reference_list.add(a);
                                                snapshot.getReference().update("English_text", "123");
                                                String English_text = (String) data.get("English_text");
                                                String Turkish_text = (String) data.get("Turkish_text");
                                                String image_url = (String) data.get("image_url");
                                                Picasso.get().load(image_url).into(binding.imageView2);
                                                ask_number_counter = ask_number_counter + 1;
                                                binding.textView7.setText(True_question+" True/"+ask_number_counter+" question");
                                                binding.textView.setText(String.valueOf(ask_number_counter)+".  Soru");
                                                binding.textView2.setText(English_text);
                                                exam_true_option = get_random_number(2);
                                                if (exam_true_option==1){
                                                    binding.option1RadioButton.setText(Turkish_text);
                                                    while (true) {
                                                        int correct_option = get_random_number(word_number);
                                                        if ((Turkish_text_list.get(correct_option)).equals(Turkish_text)){
                                                            continue;
                                                        }
                                                        else {
                                                            binding.option2RadioButton.setText(Turkish_text_list.get(correct_option));
                                                            break;
                                                        }
                                                    }
                                                }

                                                else {
                                                    binding.option2RadioButton.setText(Turkish_text);
                                                    while (true) {
                                                        int correct_option = get_random_number(word_number);
                                                        if ((Turkish_text_list.get(correct_option-1)).equals(Turkish_text)){
                                                            continue;
                                                        }
                                                        else {
                                                            binding.option1RadioButton.setText(Turkish_text_list.get(correct_option));
                                                            break;
                                                        }
                                                    }
                                                }

                                                wordFound = true;
                                                currentSnapshot = snapshot;
                                                number_of_correct_guesses_update = (String) data.get("number_of_correct_guesses");
                                                if (ask_number_counter == ask_number){
                                                    Intent intent = new Intent(QuizActivity.this, MenuActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                                counter++;
                                if (counter > word_number) {
                                    counter = 1;
                                }
                            }
                            attemptCounter++;
                        }
                        if (!wordFound) {
                            Toast.makeText(QuizActivity.this, "Uygun kelime bulunamadı soru sayısını değiştirin", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizActivity.this, "Error getting documents: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public int get_random_number(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        Random random = new Random();
        return random.nextInt(bound) + 1;
    }

    public int getDaysDifference(Timestamp timestamp) {
        if (timestamp==null){
            return 0;
        }
        Date givenDate = timestamp.toDate();
        Date currentDate = new Date();
        long diffInMillis = currentDate.getTime() - givenDate.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return (int) diffInDays;
    }


    public int Can_word_asked(Timestamp date, String number_of_correct_guesses) {
        int daysPassed = getDaysDifference(date);

        switch (number_of_correct_guesses) {
            case "0":
                return 1;
            case "1":
                return daysPassed > 1 ? 1 : 0;
            case "2":
                return daysPassed > 7 ? 1 : 0;
            case "3":
                return daysPassed > 30 ? 1 : 0;
            case "4":
                return daysPassed > 90 ? 1 : 0;
            case "5":
                return daysPassed > 180 ? 1 : 0;
            case "6":
                return daysPassed > 360 ? 1 : 0;
            default:
                return 0;
        }
    }

    public int control_option(int exam_true_option){
        if (exam_true_option==0){
            return 10;
        }

        else if (exam_true_option==1){
            if(binding.option1RadioButton.isChecked()){
                return  1;

            }

            else {
                return 0;
            }
        }

        else if (exam_true_option==2){
            if(binding.option2RadioButton.isChecked()){
                return  1;

            }

            else {
                return 0;
            }
        }

        else {
            return 0;
        }
    }
}
