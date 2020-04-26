package com.example.retrofit_file_upload_test1;

import android.view.View;

public class Visbility {


    public static void makeVisible(View... views){
        for(View view : views){
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void makeGone(View... views){
        for(View view : views){
            view.setVisibility(View.GONE);
        }

    }

}
