package com.numero.memoclient.MemoApiClient;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiMemoParser extends AsyncTask<Void, Void, Void>{

    private Callback callback;
    private String data;
    private Memo memo;

    private ApiMemoParser(String data) {
        this.data = data;
    }

    public static ApiMemoParser init(String data) {
        return new ApiMemoParser(data);
    }

    public void execute(Callback callback){
        this.callback = callback;
        this.execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            JSONObject jsonObject = new JSONObject(data);

            memo = Memo.init()
                    .setID(jsonObject.getLong("id"))
                    .setValue(jsonObject.getString("value"))
                    .setCreated(jsonObject.getString("created_at"))
                    .setUpdate(jsonObject.getString("updated_at"))
                    .setURL(jsonObject.getString("url"));

        } catch (JSONException ignored) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        callback.onPostExecute(memo);
    }

    public interface Callback {
        void onPostExecute(Memo memo);
    }
}
