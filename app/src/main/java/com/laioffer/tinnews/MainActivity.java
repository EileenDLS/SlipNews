package com.laioffer.tinnews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

//    static final String TAG = "SDL";

    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // get host
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController(); //get controller

        NavigationUI.setupWithNavController(navView, navController);
        // 最上面的显示
        NavigationUI.setupActionBarWithNavController(this,navController);

//        navController.navigate(R.id.navigation_home);

        // create instance
        NewsApi api = RetrofitClient.newInstance().create(NewsApi.class);
//        Call<NewsResponse> call = api.getTopHeadlines("us");
//        //异步执行
//        // 异步结束后，执行callback里的onResponse和onFailure
//        call.enqueue(new Callback<NewsResponse>() {
//            @Override
//            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
//                if (response.isSuccessful()) { // 200, 202
//                    Log.d(TAG, response.body().toString());
//                } else {
//                    Log.e(TAG,response.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewsResponse> call, Throwable t) {
//
//            }
//        });

    }

    // 向上层返回 （回到home）
    // mainActivity自带，点返回就会trigger
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }
}