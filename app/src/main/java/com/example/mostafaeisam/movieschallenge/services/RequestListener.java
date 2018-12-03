package com.example.mostafaeisam.movieschallenge.services;

public interface RequestListener {
    void onSuccess(Object object,int idRequest);
    void  onFailure(int errorCode,int idRequest);
}
