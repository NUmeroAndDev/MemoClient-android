package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClientManager extends AsyncTask<Void, Void, String> {
    private final static String RESULT_ERROR = "ERROR";

    private String URLString;
    private Callback callback;
    private Request request;

    public ApiClientManager(String URLString){
        this.URLString = URLString;
    }

    public static ApiClientManager init(String URLString){
        return new ApiClientManager(URLString);
    }

    public ApiClientManager requestGet(){
        request = new Request.Builder()
                .url(URLString)
                .get()
                .build();
        return this;
    }

    public ApiClientManager requestPost(String param){
        RequestBody body = new FormBody.Builder()
                .add("memo[value]", param)
                .build();

        request = new Request.Builder()
                .url(URLString)
                .post(body)
                .build();
        return this;
    }

    public ApiClientManager requestPut(String param){
        RequestBody body = new FormBody.Builder()
                .add("memo[value]", param)
                .build();

        request = new Request.Builder()
                .url(URLString)
                .put(body)
                .build();
        return this;
    }

    public ApiClientManager requestDelete(){
        request = new Request.Builder()
                .url(URLString)
                .delete()
                .build();
        return this;
    }

    public void execute(Callback callback){
        this.callback = callback;
        this.execute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;
        OkHttpClient client = new OkHttpClient();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            response.close();
//            ToDo: response処理

        } catch (IOException e) {
            e.printStackTrace();
            return ApiClientManager.RESULT_ERROR;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (ApiClientManager.RESULT_ERROR.equals(result)){
            callback.onFailure();
            return;
        }
        callback.onSuccess(result);
    }


    public interface Callback{
        void onSuccess(String result);

        void onFailure();
    }
}
