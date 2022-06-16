package com.normurodov_nazar.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.normurodov_nazar.movies.Customizations.MovieDescription;
import com.normurodov_nazar.movies.Customizations.MovieDescriptionResult;
import com.normurodov_nazar.movies.Customizations.MovieList;
import com.normurodov_nazar.movies.Customizations.MovieListItem;
import com.normurodov_nazar.movies.Customizations.MoviesAdapter;
import com.normurodov_nazar.movies.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MoviesAdapter adapter;
    ActivityMainBinding b;
    MainActivityModel model;

    boolean onDescriptionPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(MainActivityModel.class);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.search.setOnClickListener(c -> getMovieByTitle());
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMovieByTitle() {
        model.getMovieListByTitle(b.editText.getText().toString()).observe(this, message -> {
            switch (message.getType()) {
                case loading:
                    showLoading();
                    break;
                case error:
                    showErrorPage(message.getError());
                    break;
                case movies:
                    showResult(message.getMovieList());
                    break;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (onDescriptionPage) {
            returnToResultPage();
        } else super.onBackPressed();
    }

    private void returnToResultPage() {
        b.descriptionPage.setVisibility(View.GONE);
        b.searchBar.setVisibility(View.VISIBLE);
        b.recycler.setVisibility(View.VISIBLE);
        onDescriptionPage = false;
    }

    void showResult(Response<MovieList> response) {
        if (response.body() != null) {
            if (response.body().isSuccess()) {
                List<MovieListItem> movies = response.body().getMovieListItems();
                adapter = new MoviesAdapter(MainActivity.this, movies, id ->
                        model.getMovieById(id).observe(MainActivity.this, message -> {
                            switch (message.getType()) {
                                case loading:
                                    showLoading();
                                    break;
                                case error:
                                    showErrorPage(message.getError());
                                    break;
                                case description:
                                    showDescription(message.getDescription());
                                    break;
                            }
                        }));
                b.recycler.setAdapter(adapter);
            } else showErrorPage(getString(R.string.errorUnknown));
        } else showErrorPage(getString(R.string.errorUnknown));
        b.searchBar.setVisibility(View.VISIBLE);
        b.recycler.setVisibility(View.VISIBLE);
        b.progressBar.setVisibility(View.GONE);
        b.descriptionPage.setVisibility(View.GONE);
        b.errorText.setVisibility(View.GONE);
    }

    private void showDescription(Response<MovieDescriptionResult> response) {
        onDescriptionPage = true;
        if (response.body() != null) {
            if (response.body().isSuccess()) {
                MovieDescription i = response.body().getResult();
                if ("N/A".equals(i.getWebsite())) b.website.setVisibility(View.GONE);
                else {
                    b.website.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(i.getWebsite()))));
                }
                Glide.with(this).load(i.getImageUrl()).placeholder(R.drawable.ic_movie).into(b.movieImage);
                b.details.setText(getString(R.string.info,
                        i.getTitle(),
                        i.getReleased(),
                        i.getRuntime(),
                        i.getGenre(),
                        i.getDirector(),
                        i.getWriter(),
                        i.getActors(),
                        i.getDetials(),
                        i.getLanguage(),
                        i.getCountry(),
                        i.getAwards(),
                        i.getProduction(),
                        i.getType(),
                        i.getRating(),
                        i.getVotes(),
                        i.getMoney()));
            } else {
                showErrorPage(getString(R.string.errorUnknown));
                returnToResultPage();
            }
        } else {
            showErrorPage(getString(R.string.errorUnknown));
            returnToResultPage();
        }
        b.searchBar.setVisibility(View.GONE);
        b.recycler.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.GONE);
        b.errorText.setVisibility(View.GONE);
        b.descriptionPage.setVisibility(View.VISIBLE);
    }

    void showErrorPage(String text) {
        b.descriptionPage.setVisibility(View.GONE);
        b.searchBar.setVisibility(View.VISIBLE);
        b.recycler.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.GONE);
        b.errorText.setText(getString(R.string.error, text));
        b.errorText.setVisibility(View.VISIBLE);
    }

    void showLoading() {
        b.descriptionPage.setVisibility(View.GONE);
        b.searchBar.setVisibility(View.GONE);
        b.recycler.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.VISIBLE);
        b.errorText.setVisibility(View.GONE);
    }
}