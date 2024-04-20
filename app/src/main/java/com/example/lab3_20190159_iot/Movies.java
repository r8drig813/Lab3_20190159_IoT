package com.example.lab3_20190159_iot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.example.lab3_20190159_iot.databinding.ActivityMoviesBinding;
import com.example.lab3_20190159_iot.dto.Movie;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import com.example.lab3_20190159_iot.services.MovieService;

public class Movies extends AppCompatActivity {

    MovieService movieService;

    ActivityMoviesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        binding = ActivityMoviesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        movieService = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);

        if(tengoInternet()){
            Intent intent = getIntent();
            String imdb = intent.getStringExtra("imdb");

            movieService.getMovie(imdb).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    if(response.isSuccessful()){
                        Movie movie = response.body();
                        binding.titleMovie.setText(movie.getTitle());
                        binding.textView11.setText(movie.getYear());
                        binding.textView22.setText(movie.getRuntime());
                        binding.textView33.setText(movie.getGenre());
                        binding.textView44.setText(movie.getDirector());
                        binding.textView55.setText(movie.getActors());
                        binding.plotMovie.setText(movie.getPlot());




                        Log.d("msg-test-ws-profile","name: " + movie.getImdbID());

                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }



    }






    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.d("msg-test-internet", "Internet: " + tieneInternet);

        return tieneInternet;
    }


}