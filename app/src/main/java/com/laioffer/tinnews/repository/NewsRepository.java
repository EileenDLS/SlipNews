package com.laioffer.tinnews.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laioffer.tinnews.TinNewsApplication;
import com.laioffer.tinnews.database.TinNewsDatabase;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {
    // webservice
    private final NewsApi newsApi;

    static final String TAG = "SDL";

    private final TinNewsDatabase database;

    //db

    public NewsRepository() {
        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines(String country){
        Call<NewsResponse> call = newsApi.getTopHeadlines(country);
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        //异步执行
        // 异步结束后，执行callback里的onResponse和onFailure
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) { // 200, 202
                    Log.d(TAG, response.body().toString());
                    // set state
                    topHeadlinesLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    topHeadlinesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                topHeadlinesLiveData.setValue(null);

            }
        });

        return topHeadlinesLiveData; // 单向数据流：对数据改变有严格控制，所以返回是immutable，而内部是mutable
    }

    public LiveData<NewsResponse> searchNews(String query) {
        Call<NewsResponse> call = newsApi.getEverything(query, 40);
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        //异步执行
        // 异步结束后，执行callback里的onResponse和onFailure
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) { // 200, 202
                    Log.d(TAG, response.body().toString());
                    // set state
                    topHeadlinesLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, response.toString());
                    topHeadlinesLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                topHeadlinesLiveData.setValue(null);

            }
        });

        return topHeadlinesLiveData; // 单向数据流：对数据改变有严格控制，所以返回是immutable，而内部是mutable
    }

    public LiveData<Boolean> favoriteArticle (Article article){
        MutableLiveData<Boolean> saveResultLiveData = new MutableLiveData<>();

        // call
        FavoriteAsyncTask favoriteTask = new FavoriteAsyncTask(database, saveResultLiveData);
        favoriteTask.execute(article);   // AsyncTask.execute() = Call.enqueue()
        return saveResultLiveData;
    }

    public LiveData<List<Article>> getAllSavedArticles() {
        return database.articleDao().getAllArticles();
    }

    public void deleteSavedArticle(Article article) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                database.articleDao().deleteArticle(article);
//            }
//        });

        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
    }

    // AsyncTask<parameter, progress, return>
    private static class FavoriteAsyncTask extends AsyncTask<Article, Void, Boolean>{

        private final TinNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        public FavoriteAsyncTask(TinNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }

        // background thread: any thread not main thread
        @Override
        protected Boolean doInBackground(Article... articles) {
            Article article = articles[0];
            try {
                // time consuming database operation, so it is in background thread
                database.articleDao().saveArticle(article);
            } catch (Exception e){
                return false;
            }
            return true;
        }

        // override: ctrl + o
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            liveData.setValue(aBoolean);
        }
    }

}
