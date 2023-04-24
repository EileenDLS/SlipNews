package com.laioffer.tinnews.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.repository.NewsRepository;

// viewmodel interact with repository
public class HomeViewModel extends ViewModel {
    private final NewsRepository repository;

    // country -- state
    private final MutableLiveData<String> countryInput = new MutableLiveData<>();

    public HomeViewModel (NewsRepository repository) {
        this.repository = repository;
    }

    // event
    public void setCountryInput(String country){
        countryInput.setValue(country);
    }

    // getter: get newsresponse
    public LiveData<NewsResponse> getTopHeadlines() {
        // country changed -> transform to news response
        return Transformations.switchMap(countryInput, repository::getTopHeadlines);
        // when countryinput changed, trigger switchmap, 变成repository::getTopHeadlines
        // repository::getTopHeadlines: 是一个function, java double colon

    }

    public void setFavoriteArticleInput (Article article){
        repository.favoriteArticle(article);
    }

}
