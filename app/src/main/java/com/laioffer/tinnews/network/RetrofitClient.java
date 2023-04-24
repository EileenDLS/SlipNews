package com.laioffer.tinnews.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 配置retrofit
public class RetrofitClient {
    // request中共同share的东西
    private static final String API_KEY = "806dcae7aa0640f99250906f83f1f56c";
    private static final String BASE_URL = "https://newsapi.org/v2/";

    // Singleton: 当被多次create时，这样可以减少成本
    public static Retrofit newInstance() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static class HeaderInterceptor implements Interceptor {

        // 作用：打断request，做一些操作后又继续
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original
                    .newBuilder()
                    .header("X-Api-Key", API_KEY)
                    .build();
            return chain.proceed(request);
        }
    }


}

