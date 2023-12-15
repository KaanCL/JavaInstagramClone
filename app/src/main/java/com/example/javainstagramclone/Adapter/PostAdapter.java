package com.example.javainstagramclone.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javainstagramclone.Model.Posts;
import com.example.javainstagramclone.databinding.RecyclerRowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<Posts> postsArrayList;

public PostAdapter(ArrayList<Posts> postsArrayList){
    this.postsArrayList = postsArrayList;


}


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.recyclerRowBinding.recyclerViewUserEmailText.setText(postsArrayList.get(position).email);
        holder.recyclerRowBinding.recyclerViewCommentText.setText(postsArrayList.get(position).comment);
        Picasso.get().load(postsArrayList.get(position).dowloadUrl).into(holder.recyclerRowBinding.recyclerViewImageView);


    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }
    class PostHolder extends  RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public PostHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
           this. recyclerRowBinding = recyclerRowBinding;
        }
    }
}
