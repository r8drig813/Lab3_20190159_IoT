package com.example.lab3_20190159_iot;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3_20190159_iot.databinding.ActivityPrimeNumbersBinding;
import com.example.lab3_20190159_iot.dto.viewmodel.ContadorViewModel;
import com.example.lab3_20190159_iot.services.PrimesService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrimeNumbers extends AppCompatActivity {


    ActivityPrimeNumbersBinding binding;
    PrimesService primesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimeNumbersBinding. inflate(getLayoutInflater()) ;
        setContentView( binding.getRoot()) ;

        ApplicationThreads application = (ApplicationThreads) getApplication();
        ExecutorService executorService = application.executorService;

        ContadorViewModel contadorViewModel =
                new ViewModelProvider(PrimeNumbers.this).get(ContadorViewModel.class);

        contadorViewModel.getContador().observe(this, contador -> {
            //aquí o2
            binding.showNumber.setText(String.valueOf(contador));
        });


        binding.ascender.setOnClickListener(view -> {
            executorService.execute(() -> {
                List<Integer> primeNumbers = generatePrimeNumbers(999);
                for (Integer prime : primeNumbers) {
                    contadorViewModel.getContador().postValue(prime);
                    Log.d("msg-test", "prime: " + prime);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });


        primesService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PrimesService.class);

    }


    private List<Integer> generatePrimeNumbers(int count) {
        List<Integer> primes = new ArrayList<>();
        int num = 2;
        while (primes.size() < count) {
            if (isPrime(num)) {
                primes.add(num);
            }
            num++;
        }
        return primes;
    }

    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}