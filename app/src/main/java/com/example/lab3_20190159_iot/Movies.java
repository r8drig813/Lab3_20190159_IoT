package com.example.lab3_20190159_iot;

import android.content.Context;
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
            movieService.getMovie().enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    //aca estoy en el UI Thread
                    if(response.isSuccessful()){
                        Movie movie = response.body();
                        binding.titleMovie.setText(movie.getTitle());
                        binding.textView1.setText(movie.getYear());
                        binding.textView2.setText(movie.getRuntime());
                        binding.textView3.setText(movie.getGenre());
                        binding.textView4.setText(movie.getDirector());
                        binding.textView5.setText(movie.getActors());
                        binding.plotMovie.setText(movie.getPlot());
                        binding.rating.setText(movie.getRatings());




                        Log.d("msg-test-ws-profile","name: " + movie.getTitle());

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