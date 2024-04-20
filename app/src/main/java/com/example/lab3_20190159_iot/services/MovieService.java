    package com.example.lab3_20190159_iot.services;


    import com.example.lab3_20190159_iot.dto.Movie;
    import com.example.lab3_20190159_iot.dto.Number;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.http.GET;
    import retrofit2.http.Query;
    public interface MovieService {

        @GET("/?apikey=bf81d461")
        Call<Movie> getMovie(@Query("i") String imdb);



    }
