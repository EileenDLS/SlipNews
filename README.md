# SlipNews
This is an andorid app which can provide daily news, user can bookmark whatever they like while browsing. 

This app integrates Retrofit and LiveData to pull the latest news data from a RESTFUL endpoint  (newsapi.org).

I designed the Instagram Flavor News app based on Google Component Architectural MVVM Pattern, and implemented the bottom bar & page navigation using JetPack navigation component. 
In the mean time, utilized 3rd party CardStackView(RecyclerView) to support swipe gestures for liking/disliking the news. 
Furthermore, I built the Room Database with LiveData & ViewModel to support local cache and offline model.
