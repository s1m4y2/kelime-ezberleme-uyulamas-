package com.example.uartek;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.uartek.adapter.PostAdapter_2;
import com.example.uartek.databinding.ActivityAnalysisBinding;
import com.example.uartek.model.Post_2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class AnalysisActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private int[] numbers, trueNumbers;
    private FirebaseFirestore firebaseFirestore;
    private String email;
    private ArrayList<Post_2> postArrayList;
    private ActivityAnalysisBinding binding;
    private PostAdapter_2 postAdapter_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalysisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        postArrayList = new ArrayList<>();
        numbers = new int[16];
        trueNumbers = new int[16];
        getData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter_2 = new PostAdapter_2(postArrayList);
        binding.recyclerView.setAdapter(postAdapter_2);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void getData() {
        firebaseFirestore.collection(email).addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(AnalysisActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            if (value != null) {
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    Map<String, Object> data = snapshot.getData();
                    String number_of_correct_guesses = (String) data.get("number_of_correct_guesses");
                    String word_subject = (String) data.get("word_subject");
                    change_number_true(number_of_correct_guesses, word_subject);
                }
                set_post_adapter();
            }
        });
    }

    private void change_number_true(String number_of_correct_guesses, String word_subject) {
        switch (word_subject) {
            case "Animals":
            case "Objects":
            case "Food and Drinks":
            case "Nature":
            case "Colors":
            case "People":
            case "Time":
            case "Places":
            case "Emotions":
            case "Family":
            case "Members":
            case "Sports":
            case "Vehicles":
            case "Clothing":
            case "Technology":
            case "Verbs":
                int index = getIndex(word_subject);
                numbers[index]++;
                trueNumbers[index] += Integer.parseInt(number_of_correct_guesses);
                break;
        }
    }

    private void set_post_adapter() {
        String[] categories = {"Animals", "Objects", "Food and Drinks", "Nature", "Colors", "People", "Time", "Places", "Emotions", "Family", "Members", "Sports", "Vehicles", "Clothing", "Technology", "Verbs"};
        for (String category : categories) {
            int index = getIndex(category);
            int number = numbers[index];
            if (number > 0) {
                int average = trueNumbers[index] / number;
                int word_number_percentage = 100 * average / 6;
                Post_2 post = new Post_2(category, String.valueOf(word_number_percentage), String.valueOf(number));
                postArrayList.add(post);
            }
        }
        postAdapter_2.notifyDataSetChanged();
    }

    private int getIndex(String category) {
        switch (category) {
            case "Animals":
                return 0;
            case "Objects":
                return 1;
            case "Food and Drinks":
                return 2;
            case "Nature":
                return 3;
            case "Colors":
                return 4;
            case "People":
                return 5;
            case "Time":
                return 6;
            case "Places":
                return 7;
            case "Emotions":
                return 8;
            case "Family":
                return 9;
            case "Members":
                return 10;
            case "Sports":
                return 11;
            case "Vehicles":
                return 12;
            case "Clothing":
                return 13;
            case "Technology":
                return 14;
            case "Verbs":
                return 15;
            default:
                return -1;
        }
    }

    public void go_back_click(View view) {
        Intent intent = new Intent(AnalysisActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void get_pdf(View view) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Analysis");
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Category");
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Correct Percentage");
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("Total word");

        int rowNum = 1;
        for (Post_2 post : postArrayList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(post.getCategory());
            row.createCell(1).setCellValue(post.getPercentage());
            row.createCell(2).setCellValue(post.getAttempts());
        }

        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Analysis.xlsx");
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            Toast.makeText(this, "Excel created successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
