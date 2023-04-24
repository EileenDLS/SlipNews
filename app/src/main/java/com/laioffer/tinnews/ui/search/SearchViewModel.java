package com.laioffer.tinnews.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.repository.NewsRepository;

public class SearchViewModel extends ViewModel {

    private final NewsRepository repository;

    // country -- state
    private final MutableLiveData<String> searchInput = new MutableLiveData<>();

    public SearchViewModel(NewsRepository repository) {
        this.repository = repository;
    }

    // event
    public void setSearchInput(String query) {
        searchInput.setValue(query);
    }

    // getter: get newsresponse
    public LiveData<NewsResponse> searchNews() {
        // country changed -> transform to news response
        return Transformations.switchMap(searchInput, repository::searchNews);
        // when countryinput changed, trigger switchmap, 变成repository::getTopHeadlines
        // repository::getTopHeadlines: 是一个function, java double colon
    }
}
