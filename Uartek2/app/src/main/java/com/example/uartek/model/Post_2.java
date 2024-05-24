package com.example.uartek.model;

public class Post_2 {

    public String subject;
    public String word_number;
    public String word_number_percentage;


    public Post_2(String subject, String word_number_percentage, String word_number) {
        this.subject = subject;
        this.word_number = word_number;
        this.word_number_percentage = word_number_percentage;

    }
    public String getCategory() {
        return subject;
    }

    public String getPercentage() {
        return word_number_percentage;
    }

    public String getAttempts() {
        return word_number;
    }
}
