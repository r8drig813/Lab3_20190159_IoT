package com.example.lab3_20190159_iot;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab3_20190159_iot.databinding.ActivityMainBinding;
import com.example.lab3_20190159_iot.databinding.ActivityMoviesBinding;
import com.example.lab3_20190159_iot.dto.Movie;
import com.example.lab3_20190159_iot.services.MovieService;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    MovieService movieService;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button primeButton = findViewById(R.id.primeButton);
        Button searchMovie = findViewById(R.id.searchMovie);


        primeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PrimeNumbers.class);
            startActivity(intent);
        });


        searchMovie.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.moviesSearch);
            String imdb = editText.getText().toString();
            Intent intent = new Intent(MainActivity.this, Movies.class);
            intent.putExtra("imdb", imdb);
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