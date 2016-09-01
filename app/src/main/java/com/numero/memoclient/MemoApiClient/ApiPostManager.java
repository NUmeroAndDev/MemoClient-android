package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiPostManager extends AsyncTask<Void, Void, Integer> {

    private final static int RESULT_SUCCESS = 0;
    private final static int RESULT_ERROR = -1;

    private String URLString;
    private String postParam;
    private Callback callback;

    public ApiPostManager(String URLString){
        this.URLString = URLString;
    }

    public static ApiPostManager init(String URLString){
        return new ApiPostManager(URLString);
    }

    public ApiPostManager setPostParam(String postParam){
        this.postParam = postParam;
        return this;
    }

    public void post(Callback callback){
        this.callback = callback;
        this.execute();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("memo[value]", postParam)
                .build();

        Request request = new Request.Builder()
                .url(URLString)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ApiPostManager.RESULT_ERROR;
        }

        return ApiPostManager.RESULT_SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == ApiPostManager.RESULT_ERROR){
            callback.onFailure();
            return;
        }
        callback.onSuccess();
    }


    public interface Callback{
        void onSuccess();

        void onFailure();
    }
}
