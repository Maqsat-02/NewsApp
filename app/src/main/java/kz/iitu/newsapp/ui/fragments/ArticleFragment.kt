package kz.iitu.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_article.*
import kz.iitu.newsapp.R
import kz.iitu.newsapp.models.Article
import kz.iitu.newsapp.ui.viewmodels.NewsViewModel

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {
    private val viewModel: NewsViewModel by viewModels()
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        setupView(article)

        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved",Snackbar.LENGTH_SHORT).show()
        }
        sh_btn.setOnClickListener{
            this.context?.let { it1 -> viewModel.shareArticle(it1,article) }
            Snackbar.make(view,"Article shared",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupView(article: Article){
        Glide.with(this).load(article.urlToImage).into(ArticleImage)
        ArticleTitle.text = article.title
        ArticleDescription.text = article.description
    }

}