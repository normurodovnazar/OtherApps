package com.normurodov_nazar.sample;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.normurodov_nazar.sample.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit;
    GetUniversityService service;
    Call<List<University>> call;
    SharedPreferences preferences;
    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        preferences = getPreferences(MODE_PRIVATE);
        loadData(null);
        b.reload.setOnClickListener(c -> loadData(null));
        b.searchButton.setOnClickListener(c->{
            String text = b.searchText.getText().toString();
            loadData(text);
        });
    }

    private void loadData(String text) {

        Canvas canvas = new Canvas();

        canvas.drawLine(0,0,5,5,new Paint());
        showLoading();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://universities.hipolabs.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GetUniversityService.class);
        call = text==null ? service.getUniversities() : service.getUniversities(text);
        call.enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(@NonNull Call<List<University>> call, @NonNull Response<List<University>> response) {
                Log.e("onResponse Call", call.toString());
                Log.e("onResponse response", response.toString());
                switch (response.code()) {
                    case 200:
                        showList(response);
                        break;
                    case 404:
                        showError(getString(R.string.noData));
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<University>> call, @NonNull Throwable t) {
                showError(t.getLocalizedMessage());
            }
        });
    }

    private void showLoading(){
        Log.e("state","showLoading");
        b.recycler.setVisibility(View.GONE);
        b.errorPage.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.VISIBLE);
        b.searchBar.setVisibility(View.GONE);
    }

    private void showList(@NonNull Response<List<University>> response) {
        Log.e("state","showList");
        b.recycler.setVisibility(View.VISIBLE);
        b.errorPage.setVisibility(View.GONE);
        b.progressBar.setVisibility(View.GONE);
        b.searchBar.setVisibility(View.VISIBLE);
        UniversityAdapter adapter = new UniversityAdapter(MainActivity.this, response.body());
        b.recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        b.recycler.setAdapter(adapter);
    }

    private void showError(String errorMessage) {
        b.searchBar.setVisibility(View.VISIBLE);
        Log.e("state","showError");
        b.recycler.setVisibility(View.GONE);
        b.errorPage.setVisibility(View.VISIBLE);
        b.progressBar.setVisibility(View.GONE);
        Log.e("errorMessage", errorMessage);
        b.errorText.setText(getString(R.string.error, errorMessage.contains("No address associated with hostname") ? getString(R.string.errorNetwork) : errorMessage));
    }
}