package com.example.lab3_20190159_iot.dto.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContadorViewModel  extends ViewModel {

    private final MutableLiveData<Integer> contador = new MutableLiveData<>();

    public MutableLiveData<Integer> getContador() {
        return contador;
    }
}
