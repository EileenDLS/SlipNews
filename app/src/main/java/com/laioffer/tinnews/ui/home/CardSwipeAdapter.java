package com.laioffer.tinnews.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.SwipeNewsCardBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.ui.search.SearchNewsAdapter;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;
import java.util.List;

public class CardSwipeAdapter extends RecyclerView.Adapter<CardSwipeAdapter.CardSwipeViewHolder>{
    // 1. Supporting data:
    // TODO
    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newsList){
        articles.clear();
        articles.addAll(newsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardSwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_news_card, parent, false);
//        Log.d("tim test", searchNewsViewHolder.toString() + "onCreate" );
        return new CardSwipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSwipeViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleTextView.setText(article.title);
        holder.descriptionTextView.setText(article.description);
        if (article.urlToImage != null) {
            Picasso.get().load(article.urlToImage).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


    public static class CardSwipeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public CardSwipeViewHolder(@NonNull View itemView){
            super(itemView);
            SwipeNewsCardBinding binding = SwipeNewsCardBinding.bind(itemView);
            imageView = binding.swipeCardImageView;
            titleTextView = binding.swipeCardTitle;
            descriptionTextView = binding.swipeCardDescription;
        }

    }

    // 2. CardSwipeViewHolder:
    // TODO

    // 3. Adapter overrides:
    // TODO

    public List<Article> getArticles() {
        return articles;
    }
}
