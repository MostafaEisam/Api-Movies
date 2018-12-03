package com.example.mostafaeisam.movieschallenge.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.security.ProviderInstaller;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ServiceFactory {

    public static void getData(final Context context, final String url ,final int id, final RequestListener listener) {

        final Observable<String> getDataObservable = Observable.create
                (new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                        try {

                            final OkHttpClient okHttpClient = new OkHttpClient();
                            String a = url;
                            final Request request = new Request.Builder()
                                    .url(a)
                                    .build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    emitter.onError(e.getCause());
                                }

                                @Override
                                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                                    String body = response.body().string();
                                    call.cancel();
                                    emitter.onNext(body);
                                }
                            });

                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                });


        getDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        listener.onSuccess(s,id);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFailure(1000,id);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void post(Context context, final String url, final int value, final RequestListener listener) {
        final Observable<String> getDataObservable = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws
                            Exception {

                        try {
                            final Request.Builder request = new Request.Builder();


                            FormBody formBody = new FormBody.Builder()
                                    .add("value", String.valueOf(value))
                                    .build();

                            request.url(url)
                                    .addHeader("Content-Type", "application/json;charset=utf-8")
                                    .post(formBody)
                                    .build();


                            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                            OkHttpClient client = new OkHttpClient.Builder()
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .cache(null)//clear cache
                                    .addInterceptor(logging)
                                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                                    .build();

                            client.newCall(request.build()).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    emitter.onError(e.getCause());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    emitter.onNext(response.body().string());
                                }
                            });

                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                });


        getDataObservable
                .subscribeOn(Schedulers.io())
                .

                        observeOn(AndroidSchedulers.mainThread())
                .

                        subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(String s) {
                                listener.onSuccess(s,10);
                            }


                            @Override
                            public void onError(Throwable e) {
                                listener.onFailure(100,10);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
    }




}
