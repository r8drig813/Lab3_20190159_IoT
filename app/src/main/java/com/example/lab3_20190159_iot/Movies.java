package com.example.lab3_20190159_iot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.example.lab3_20190159_iot.databinding.ActivityMoviesBinding;
import com.example.lab3_20190159_iot.dto.Movie;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.lab3_20190159_iot.dto.Number;
import com.example.lab3_20190159_iot.dto.Ratings;
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


            movieService.getMovieRatings(imdb).enqueue(new Callback<List<Ratings>>() {
                @Override
                public void onResponse(Call<List<Ratings>> call, Response<List<Ratings>> response) {
                    if(response.isSuccessful()){
                        List<Ratings>  comments= response.body();
                        for (Ratings c : comments) {
                            binding.rating1.setText(String.valueOf(c.getSource()));
                            binding.rating1.setText(String.valueOf(c.getValue()));

                        }
                    } else {
                        Log.d("msg-test", "error en la respuesta del webservice");
                    }
                }

                @Override
                public void onFailure(Call<List<Ratings>> call, Throwable t) {
                    t.printStackTrace();
                }
            });

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
                        ArrayList<Ratings> listaRatings = movie.getRatings();
                        StringBuilder sourceText = new StringBuilder();
                        StringBuilder valueText = new StringBuilder();
                        /*Aca me ayudo chatgpt para hacer la iteracion y todo se mostrara en en los textvView*/
                        for (Ratings c : listaRatings) {
                            String source = c.getSource();
                            String value = c.getValue();
                            sourceText.append(source).append("\n");
                            valueText.append(value).append("\n");
                        }

                        binding.rating1.setText(sourceText.toString());
                        binding.rating2.setText(valueText.toString());

                        Log.d("msg-test-ws-profile","name: " + movie.getImdbID());

                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        }




            CheckBox checkBox = findViewById(R.id.checkBox);
            Button returnButton = findViewById(R.id.returnMain);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    returnButton.setVisibility(View.VISIBLE);
                } else {
                    returnButton.setVisibility(View.GONE);
                }
            });


        returnButton.setOnClickListener(v -> {
                Intent intent = new Intent(Movies.this, MainActivity.class);
                startActivity(intent);
            });




    }






    public boolean tengoInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean tieneInternet = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        Log.d("msg-test-internet", "Internet: " + tieneInternet);

        return tieneInternet;
    }


}