package com.normurodov_nazar.movies;

import static com.normurodov_nazar.movies.Sources.Hey.print;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.normurodov_nazar.movies.Customizations.MovieDescription;
import com.normurodov_nazar.movies.Customizations.MovieDescriptionResult;
import com.normurodov_nazar.movies.Customizations.MovieList;
import com.normurodov_nazar.movies.Customizations.MovieListItem;
import com.normurodov_nazar.movies.Customizations.MovieRequest;
import com.normurodov_nazar.movies.Customizations.MoviesAdapter;
import com.normurodov_nazar.movies.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    MovieRequest movieRequest;
    Call<MovieList> call;
    Call<MovieDescriptionResult> descriptionCall;
    MoviesAdapter adapter;
    ActivityMainBinding b;

    boolean onDescriptionPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.search.setOnClickListener(c -> getMovieByTitle());
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMovieByTitle() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.collectapi.com/imdb/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieRequest = retrofit.create(MovieRequest.class);
        String text = b.editText.getText().toString();
        if (!text.isEmpty() && !text.replaceAll(" ", "").equals("")) {
            while (text.startsWith(" ")) text = text.replace(" ", "");
            while (text.endsWith(" ")) text = text.substring(0, text.length() - 1);
            call = movieRequest.getMoviesByTitle(text);
            showLoading();
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                    showResult(response);
                }

                @Override
                public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                    print("onFailure", t.getLocalizedMessage());
                    showErrorPage(t.getLocalizedMessage());
                }
            });
        } else Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (onDescriptionPage) {
            returnToResultPage();
        }else super.onBackPressed();
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
                adapter = new MoviesAdapter(MainActivity.this, movies, id -> {
                    descriptionCall = movieRequest.getMovieById(id);
                    showLoading();
                    descriptionCall.enqueue(new Callback<MovieDescriptionResult>() {
                        @Override
                        public void onResponse(@NonNull Call<MovieDescriptionResult> call, @NonNull Response<MovieDescriptionResult> response) {
                            showDescription(response);
                        }

                        @Override
                        public void onFailure(@NonNull Call<MovieDescriptionResult> call, @NonNull Throwable t) {
                            showErrorPage(t.getLocalizedMessage());
                        }
                    });
                });
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
        if (response.body()!=null){
            if (response.body().isSuccess()){
                MovieDescription i = response.body().getResult();
                if ("N/A".equals(i.getWebsite())) b.website.setVisibility(View.GONE); else {
                    b.website.setOnClickListener(v-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(i.getWebsite()))));
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
            }else {
                showErrorPage(getString(R.string.errorUnknown));
                returnToResultPage();
            }
        }else {
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
        b.errorText.setText(getString(R.string.error,text));
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