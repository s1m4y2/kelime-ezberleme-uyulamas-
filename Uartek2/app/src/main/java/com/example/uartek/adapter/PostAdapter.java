package com.example.uartek.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uartek.databinding.RecyclerRowBinding;
import com.example.uartek.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<Post> postArrayList;

    public PostAdapter(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
    }

    class PostHolder extends RecyclerView.ViewHolder {
        RecyclerRowBinding recyclerRowBinding;

        public PostHolder(@NonNull RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;

        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.recyclerRowBinding.recyclerviewRowEnglishText.setText("English text: " + postArrayList.get(position).English_text);
        holder.recyclerRowBinding.recyclerviewRowSentencesText.setText("sentences: "+postArrayList.get(position).sentences);
        holder.recyclerRowBinding.recyclerviewRowSubjectText.setText("word subject: "+postArrayList.get(position).word_subject);
        holder.recyclerRowBinding.recyclerviewRowTurkishText.setText("Turkish text: "+postArrayList.get(position).Turkish_text);
        Picasso.get().load(postArrayList.get(position).image_url).into(holder.recyclerRowBinding.recyclerviewRowImageview);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }


}