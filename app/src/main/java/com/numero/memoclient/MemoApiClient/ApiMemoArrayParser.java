package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiMemoArrayParser extends AsyncTask<Void, Void, Void> {

    private Callback callback;
    private ArrayList<Memo> memoList;
    private String data;

    private ApiMemoArrayParser(String data){
        this.data = data;
    }

    public static ApiMemoArrayParser init(String data){
        return new ApiMemoArrayParser(data);
    }

    public ApiMemoArrayParser parse(ArrayList<Memo> memoList){
        this.memoList = memoList;
        return this;
    }

    public void execute(Callback callback){
        this.callback = callback;
        this.execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                memoList.add(Memo.init()
                        .setID(jsonObject.getLong("id"))
                        .setValue(jsonObject.getString("value"))
                        .setCreated(jsonObject.getString("created_at"))
                        .setUpdate(jsonObject.getString("updated_at"))
                        .setURL(jsonObject.getString("url"))
                );
            }
        } catch (JSONException ignored) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        callback.onPostExecute();
    }

    public interface Callback{
        void onPostExecute();
    }
}
