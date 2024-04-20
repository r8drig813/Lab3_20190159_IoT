package com.example.lab3_20190159_iot.services;

import com.example.lab3_20190159_iot.dto.Number;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrimesService {

    @GET("/primeNumbers?len=999&order=1")
    Call<List<Number>> getNumber();

    @GET("/primeNumbers")
    Call<List<Number>> getNumberOrder(@Query("len") int order, @Query("order") int one);
}
