package com.example.retrofit_file_upload_test1;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    OkHttpClient.Builder okkhttpClientBuilder;
    HttpLoggingInterceptor loggingInterceptor;
    Retrofit.Builder builder;
    private final String BASE_URL;

    public ServiceGenerator() {

        BASE_URL = "http://192.168.43.145:5000/test/";

        okkhttpClientBuilder = new OkHttpClient.Builder();
        loggingInterceptor = new HttpLoggingInterceptor();

        okkhttpClientBuilder.addInterceptor(loggingInterceptor);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okkhttpClientBuilder.build());


    }

    public  <S> S createService(Class<S> serviceClass) {

        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
