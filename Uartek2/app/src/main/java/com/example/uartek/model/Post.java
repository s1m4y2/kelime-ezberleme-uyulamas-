package com.example.uartek.model;

public class Post {

    public String English_text;
    public String Turkish_text;
    public String number_of_correct_guesses;
    public  String sentences;
    public  String word_subject;
    public  String image_url;

    public Post(String English_text, String Turkish_text, String number_of_correct_guesses,String sentences,String word_subject,String image_url) {
        this.English_text = English_text;
        this.Turkish_text = Turkish_text;
        this.number_of_correct_guesses = number_of_correct_guesses;
        this.sentences = sentences;
        this.word_subject = word_subject;
        this.image_url = image_url;
    }
}
