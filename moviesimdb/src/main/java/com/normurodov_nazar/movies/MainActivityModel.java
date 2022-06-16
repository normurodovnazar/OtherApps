package com.normurodov_nazar.movies;

import static com.normurodov_nazar.movies.Sources.Hey.print;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.normurodov_nazar.movies.Customizations.Message;
import com.normurodov_nazar.movies.Customizations.MovieDescriptionResult;
import com.normurodov_nazar.movies.Customizations.MovieList;
import com.normurodov_nazar.movies.Customizations.MovieRequest;
import com.normurodov_nazar.movies.Customizations.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityModel extends ViewModel {
    private MutableLiveData<Message> movieLiveData;
    Call<MovieList> call;
    Call<MovieDescriptionResult> descriptionCall;
    MovieRequest movieRequest;
    Retrofit retrofit;

    public void initialize(){
        if (movieLiveData==null) movieLiveData = new MutableLiveData<>();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.collectapi.com/imdb/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        movieRequest = retrofit.create(MovieRequest.class);
    }

    public LiveData<Message> getMovieListByTitle(String text){
        if (retrofit==null) initialize();
        if (!text.isEmpty() && !text.replaceAll(" ", "").equals("")) {
            while (text.startsWith(" ")) text = text.replace(" ", "");
            while (text.endsWith(" ")) text = text.substring(0, text.length() - 1);
            call = movieRequest.getMoviesByTitle(text);
            loading();
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                    movieList(response);
                }

                @Override
                public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                    print("onFailure", t.getLocalizedMessage());
                    error(t.getLocalizedMessage());
                }
            });
        } else error("");
        return movieLiveData;
    }

    public LiveData<Message> getMovieById(String id){
        initialize();
        descriptionCall = movieRequest.getMovieById(id);
        loading();
        descriptionCall.enqueue(new Callback<MovieDescriptionResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieDescriptionResult> call, @NonNull Response<MovieDescriptionResult> response) {
                description(response);
            }

            @Override
            public void onFailure(@NonNull Call<MovieDescriptionResult> call, @NonNull Throwable t) {
                error(t.getLocalizedMessage());
            }
        });
        return movieLiveData;
    }

    void loading(){
        movieLiveData.postValue(new Message(Type.loading));
    }
    void movieList(Response<MovieList> movieList){
        movieLiveData.postValue(new Message(Type.movies,movieList,""));
    }
    void error(String error){
        movieLiveData.postValue(new Message(Type.error,error));
    }
    void description(Response<MovieDescriptionResult> description){
        movieLiveData.postValue(new Message(Type.description,description));
    }
}
