package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiGetManager extends AsyncTask<Void, Void, String> {

    private final static String RESULT_ERROR = "ERROR";

    private String URLString;
    private Callback callback;

    public ApiGetManager(String URLString){
        this.URLString = URLString;
    }

    public static ApiGetManager init(String URLString){
        return new ApiGetManager(URLString);
    }

    public void get(Callback callback){
        this.callback = callback;
        this.execute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URLString)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            response.close();

        } catch (IOException e) {
            e.printStackTrace();
            return ApiGetManager.RESULT_ERROR;
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (ApiGetManager.RESULT_ERROR.equals(result)){
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
