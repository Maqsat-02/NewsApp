package kz.iitu.newsapp.ui.viewmodels

import android.content.Context
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import kz.iitu.newsapp.db.NewsRepository
import kz.iitu.newsapp.models.Article
import kz.iitu.newsapp.models.NewsResponse
import kz.iitu.newsapp.util.Resource
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) : ViewModel() {
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

     fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response =
                newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))

        }
    }

    fun searchNews(searchQuery: String) {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response =
                newsRepository.getSearchResults(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }
    }

    private fun handleBreakingNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = it
                } else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = it
                } else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = it.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.insert(article)
        }
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }
    fun shareArticle(context: Context, article: Article){
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Android Studio Pro")
        intent.putExtra(
            Intent.EXTRA_TEXT,
            article.url
        )
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent,  "Share using....")
        context?.startActivity(chooser)
    }
}