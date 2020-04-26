package com.example.retrofit_file_upload_test1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static java.lang.Boolean.FALSE;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText age;
    EditText subject;
    Button submit;
    TextView id;
    Button upload;
    Button choose;
    TextView location;
    EditText description_txt;
    Boolean account = false;
    Visbility visbility;
    Uri fileUri;
    PathUtils pathUtils;
    ServiceGenerator serviceGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.name_txt);
        email = (EditText) findViewById(R.id.email_txt);
        age = (EditText) findViewById(R.id.age_txt);
        subject = (EditText) findViewById(R.id.subject_txt);
        submit = (Button) findViewById(R.id.submit);
        id = (TextView) findViewById(R.id.id);
        upload = (Button) findViewById(R.id.upload);
        choose = (Button) findViewById(R.id.choose);
        location = (TextView) findViewById(R.id.location);
        description_txt = (EditText) findViewById(R.id.description);
        visbility = new Visbility();
        pathUtils = new PathUtils();
        serviceGenerator = new ServiceGenerator();


        visbility.makeGone(id, upload, choose, location,description_txt);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User(name.getText().toString(), email.getText().toString(), Integer.parseInt(age.getText().toString()), subject.getText().toString());
                sendNetworkRequest(user);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("==>>","Uploading File");
                uploadFile();

            }
        });


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(account == true){

                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)!=
                            PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);

                    }else{

                        pickPhoto();

                    }

                }

            }
        });

    }

    private void uploadFile() {
        String filepath = pathUtils.getAbosolutePath(MainActivity.this,fileUri);
        File file = new File(filepath);


        FileUploadService service =
                serviceGenerator.createService(FileUploadService.class);



        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture",file.getName(),requestFile);



        RequestBody description =
                RequestBody.create(MultipartBody.FORM,name.getText().toString());


        Call<ResponseBody> call = service.upload(description,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("==>>","Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.i("==>>","failure");

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        fileUri = data.getData();
        Log.i("==>>",pathUtils.getAbosolutePath(MainActivity.this,fileUri));
        location.setText(pathUtils.getAbosolutePath(MainActivity.this,fileUri));
        Visbility.makeVisible(location,description_txt,upload);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickPhoto();
        }
    }

    private void pickPhoto() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,1);

    }


    private void sendNetworkRequest(User user) {


        FileUploadService client = serviceGenerator.createService(FileUploadService.class);
        Call<User> call = client.createAccount(user);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i("==>>","Success "+response.body().getId());
                id.setText("ID : " + response.body().getId());
                account=true;
                visbility.makeVisible(id,choose);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("==>>","Failure");
            }
        });
    }
}
