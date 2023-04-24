package com.laioffer.tinnews.network;

import com.laioffer.tinnews.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {

    // https://newsapi.org/v2/top-headlines?country=us&category=business
    @GET("top-headlines")
    Call<NewsResponse> getTopHeadlines(@Query("country") String country);


    // https://newsapi.org/v2/everything?q=tesla&from=2022-11-03&sortBy=publishedAt
    @GET("everything")
    Call<NewsResponse> getEverything(
            @Query("q") String query, @Query("pageSize") int pageSize);
    // pagesize：一页回来多少内容

}

