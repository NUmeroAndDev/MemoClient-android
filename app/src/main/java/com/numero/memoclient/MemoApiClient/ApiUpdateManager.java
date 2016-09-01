package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUpdateManager extends AsyncTask<Void, Void, Integer> {

    private final static int RESULT_SUCCESS = 0;
    private final static int RESULT_ERROR = -1;

    private String URLString;
    private String postParam;
    private Callback callback;

    public ApiUpdateManager(String URLString){
        this.URLString = URLString;
    }

    public static ApiUpdateManager init(String URLString){
//        URL example http://10.1.30.15:3000/memos/4
        return new ApiUpdateManager(URLString);
    }

    public ApiUpdateManager setPostParam(String postParam){
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
                .put(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ApiUpdateManager.RESULT_ERROR;
        }

        return ApiUpdateManager.RESULT_SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (result == ApiUpdateManager.RESULT_ERROR){
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
