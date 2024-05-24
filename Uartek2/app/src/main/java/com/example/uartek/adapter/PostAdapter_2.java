package com.example.uartek.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uartek.databinding.RecyclerRow2Binding;
import com.example.uartek.model.Post_2;

import java.util.ArrayList;

public class PostAdapter_2 extends RecyclerView.Adapter<PostAdapter_2.PostHolder> {

    private ArrayList<Post_2> postArrayList;

    public PostAdapter_2(ArrayList<Post_2> postArrayList) {
        this.postArrayList = postArrayList;
    }

    class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRow2Binding recyclerRow2Binding;

        public PostHolder(@NonNull RecyclerRow2Binding recyclerRow2Binding) {
            super(recyclerRow2Binding.getRoot());
            this.recyclerRow2Binding = recyclerRow2Binding;

        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRow2Binding recyclerRowBinding = RecyclerRow2Binding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.recyclerRow2Binding.recyclerviewRowSubjectText.setText("Subject: " + postArrayList.get(position).subject);
        holder.recyclerRow2Binding.recyclerviewRowWordNumberText.setText("Word Number: "+postArrayList.get(position).word_number);
        holder.recyclerRow2Binding.recyclerviewRowWordNumberPercentageText.setText("Learning Percentage: %  "+postArrayList.get(position).word_number_percentage);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


}