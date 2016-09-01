package com.numero.memoclient.MemoApiClient;

import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiMemoParser {

    private String data;
    private Memo memo;

    private ApiMemoParser(String data) {
        this.data = data;
    }

    public static ApiMemoParser init(String data) {
        return new ApiMemoParser(data);
    }

    public void execute(final Callback callback) {
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                callback.onPostExecute(memo);
                return false;
            }
        });
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(data);

                    memo = Memo.init()
                            .setID(jsonObject.getLong("id"))
                            .setValue(jsonObject.getString("value"))
                            .setCreated(jsonObject.getString("created_at"))
                            .setUpdate(jsonObject.getString("updated_at"))
                            .setURL(jsonObject.getString("url"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(Message.obtain());
            }
        });
        thread.start();
    }

    public interface Callback {
        void onPostExecute(Memo memo);
    }
}
