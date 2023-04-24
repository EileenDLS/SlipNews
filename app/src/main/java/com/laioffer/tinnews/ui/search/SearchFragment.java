package com.laioffer.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentSearchBinding;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.NewsViewModelFactory;
import com.laioffer.tinnews.ui.home.HomeViewModel;

public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private FragmentSearchBinding binding; //根据对应的xml生成的 (这里是根据fragment_search.xml生成的)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
//        return inflater.inflate(R.layout.fragment_search, container, false); //生成一个view object,将一个xml file转化成一个tree结构
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        SearchNewsAdapter newsAdapter = new SearchNewsAdapter();
        newsAdapter.setItemCallback(article -> {
            SearchFragmentDirections.ActionNavigationSearchToNavigationDetails
                    direction = SearchFragmentDirections.actionNavigationSearchToNavigationDetails(article);
            NavHostFragment.findNavController(SearchFragment.this).navigate(direction);
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
        binding.newsResultsRecycleView.setLayoutManager(gridLayoutManager);
        binding.newsResultsRecycleView.setAdapter(newsAdapter);

        binding.newsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()){
                    viewModel.setSearchInput(query);
                }
                binding.newsSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        NewsRepository newsRepository = new NewsRepository();

        // should be system variable, only this, when configuration change, it cannot be destroy and recreate
//        viewModel = new  HomeViewModel(newsRepository);
        viewModel =
                new ViewModelProvider(this, new NewsViewModelFactory(newsRepository)).get(SearchViewModel.class);

        // Send event to viewmodel
//        viewModel.setSearchInput("Covid-19");

        // get NewsResponse LiveData
        // observe (lifecycleOwner, observer)
        // observer observe LiveData<NewsResponse>
        viewModel.searchNews().observe(getViewLifecycleOwner(), newsResponse -> {
                    if (newsRepository != null){
                        Log.d("SF", newsResponse.toString());
                        newsAdapter.setArticles(newsResponse.articles);
                    }
                }

        );

    }

}