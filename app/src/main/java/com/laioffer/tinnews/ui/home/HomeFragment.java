package com.laioffer.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentHomeBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.NewsViewModelFactory;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;


public class HomeFragment extends Fragment implements CardStackListener {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding; //根据对应的xml生成的 (这里是根据fragment_home.xml生成的)
    private CardStackLayoutManager layoutManager;
    private CardSwipeAdapter swipeAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        swipeAdapter = new CardSwipeAdapter();
        layoutManager = new CardStackLayoutManager(requireContext(), this);
        layoutManager.setStackFrom(StackFrom.Top); //card叠加头部效果
        binding.homeCardStackView.setLayoutManager(layoutManager);
        binding.homeCardStackView.setAdapter(swipeAdapter);

        // 喜欢右边，不喜欢左边
        binding.homeLikeButton.setOnClickListener(v -> {
            swipeCard(Direction.Right);
        });
//        binding.homeLikeButton.setOnClickListener(new View.OnClickListener() {
//                                                      @Override
//                                                      public void onClick(View v) {
//                                                          swipeCard(Direction.Right);
//                                                      }
//                                                  });

                binding.homeUnlikeButton.setOnClickListener(v -> {
                    swipeCard(Direction.Left);
                });


        NewsRepository newsRepository = new NewsRepository();

        // should be system variable, only this, when configuration change, it cannot be destroy and recreate
//        viewModel = new  HomeViewModel(newsRepository);
        viewModel =
                new ViewModelProvider(this, new NewsViewModelFactory(newsRepository)).get(HomeViewModel.class);

        // Send event to viewmodel
        viewModel.setCountryInput("us");

        // get NewsResponse LiveData
        // observe (lifecycleOwner, observer)
        // observer observe LiveData<NewsResponse>
        viewModel.getTopHeadlines().observe(getViewLifecycleOwner(), newsResponse -> {
            if (newsRepository != null){
                Log.d("HF", newsResponse.toString());
                swipeAdapter.setArticles(newsResponse.articles);
            }
                }

        );

    }

    private void swipeCard(Direction direction) {
        SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                .setDirection(direction)
                .setDuration(Duration.Normal.duration)
                .build();
        layoutManager.setSwipeAnimationSetting(setting);
        binding.homeCardStackView.swipe();
    }


    @Override
    public void onCardDragging(Direction direction, float v) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Left) {
            Log.d("CardStackView", "Unliked " + layoutManager.getTopPosition());
        } else if (direction == Direction.Right) {
            Log.d("CardStackView", "Liked "  + layoutManager.getTopPosition());
            // 1. get swiped article
            Article article = swipeAdapter.getArticles().get(layoutManager.getTopPosition() - 1);
//            Log.d("CardStackView", "articles "  + article.toString());
            // 2. repo.favorite(article) to save to db
            viewModel.setFavoriteArticleInput(article);
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int i) {

    }

    @Override
    public void onCardDisappeared(View view, int i) {

    }
}