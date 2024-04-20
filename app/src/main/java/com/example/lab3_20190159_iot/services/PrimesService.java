package com.example.lab3_20190159_iot.services;

import com.example.lab3_20190159_iot.dto.Number;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PrimesService {

    @GET("/primeNumbers?len=999&order=1")
    Call<Number> getNumber();
}
