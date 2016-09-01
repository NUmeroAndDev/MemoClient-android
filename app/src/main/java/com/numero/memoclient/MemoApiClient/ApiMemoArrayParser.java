package com.numero.memoclient.MemoApiClient;


import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiMemoArrayParser {

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

    public void execute(final Callback callback){
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                callback.onPostExecute();
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(Message.obtain());
            }
        });
        thread.start();
    }

    public interface Callback{
        void onPostExecute();
    }
}
