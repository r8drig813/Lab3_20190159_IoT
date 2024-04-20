package com.example.lab3_20190159_iot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lab3_20190159_iot.databinding.ActivityPrimeNumbersBinding;
import com.example.lab3_20190159_iot.dto.Movie;
import com.example.lab3_20190159_iot.dto.Number;
import com.example.lab3_20190159_iot.dto.viewmodel.ContadorViewModel;
import com.example.lab3_20190159_iot.services.PrimesService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        final boolean[] isPaused = {false};

        boolean isAscending1 = true;
        TextView estadoTextView = findViewById(R.id.ascenderodescender);

        primesService = new Retrofit.Builder()
                .baseUrl("https://prime-number-api.onrender.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PrimesService.class);



        AtomicBoolean isAscending = new AtomicBoolean(false);

        binding.ascender.setOnClickListener(view -> {
            binding.ascender.setText(isAscending.get() ? "Ascender" : "Descender");
            estadoTextView.setText(isAscending.get() ? "Actualmente el contador está descendiendo " : "Actualmente el contador está ascendiendo");

            executorService.execute(() -> {
                primesService.getNumber().enqueue(new Callback<List<Number>>() {
                    @Override
                    public void onResponse(Call<List<Number>> call, Response<List<Number>> response) {
                        if (response.isSuccessful()  ) {
                            List<Number> numberList = response.body();
                            int start = isAscending.get() ? 0 : numberList.size() - 1;
                            int end = isAscending.get() ? numberList.size() : -1;
                            int step = isAscending.get() ? 1 : -1;
                            for (int i = start; i != end; i += step) {
                                Number num = numberList.get(i);
                                final int finalI = i;
                                binding.showNumber.postDelayed(() -> {
                                    binding.showNumber.setText(String.valueOf(numberList.get(finalI).getNumber()));
                                }, Math.abs(start - i) * 1000); // 1000 milisegundos = 1 segundo
                            }
                        } else {
                            Log.d("msg-test", "Error en la respuesta del webservice");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Number>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            });

            isAscending.set(!isAscending.get());
        });










        Button searchNumber = findViewById(R.id.searchNumber);


        searchNumber.setOnClickListener(v -> {
            EditText editText = findViewById(R.id.numberSearch);
            String numberSearch = editText.getText().toString();
            int number = Integer.parseInt(numberSearch);

            primesService.getNumberOrder(number,1).enqueue(new Callback<List<Number>>() {
                @Override
                public void onResponse(Call<List<Number>> call, Response<List<Number>> response) {
                    if (response.isSuccessful()) {
                        List<Number> commentList = response.body();
                        for (Number c : commentList) {
                            binding.showNumber.setText(String.valueOf(c.getNumber()));

                            System.out.println("id: " + c.getNumber() + " | body: " + c.getOrder());
                        }
                    } else {
                        Log.d("msg-test", "error en la respuesta del webservice");
                    }
                }
                @Override
                public void onFailure(Call<List<Number>> call, Throwable t) {
                    t.printStackTrace();
                }


            });

        });


        Button pausarButton = findViewById(R.id.pausar);
        pausarButton.setOnClickListener(v -> {
            isPaused[0] = !isPaused[0];

            pausarButton.setText(isPaused[0] ? "Reiniciar" : "Pausar");

            estadoTextView.setText(isPaused[0] ? "Está en pausa" : (isAscending1 ? "Actualmente el contador esta ascendiendo" : "Está descendiendo"));

        });

    }

}