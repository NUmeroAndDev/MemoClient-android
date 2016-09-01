package com.numero.memoclient.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.numero.memoclient.MemoApiClient.ApiGetManager;
import com.numero.memoclient.MemoApiClient.ApiMemoParser;
import com.numero.memoclient.MemoApiClient.ApiPostManager;
import com.numero.memoclient.MemoApiClient.ApiUpdateManager;
import com.numero.memoclient.MemoApiClient.Memo;
import com.numero.memoclient.R;
import com.numero.memoclient.Utils.Constant;

public class MemoEditActivity extends AppCompatActivity{

    private boolean isNewMemo = true;
    private Memo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button postButton = (Button) findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText memoEditText = (EditText) findViewById(R.id.memo_edit);
                String memoText = memoEditText.getText().toString();
                if (memoText.equals("")){
                    return;
                }
                if (isNewMemo) {
                    executePostMemo(memoText);
                } else {
                    executeUpdateMemo(memoText);
                }
            }
        });

        String memoURLString = getIntent().getStringExtra(Constant.MEMO_URL);
        if (memoURLString != null){
            isNewMemo = false;
            executeGetMemo(memoURLString);
        }

    }

    private void executePostMemo(String memoText){
        String URLString = "http://192.168.10.16:3001/memos";
        ApiPostManager.init(URLString).setPostParam(memoText).post(new ApiPostManager.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MemoEditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MemoEditActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeUpdateMemo(String memoText){
        String URLString = memo.URLString;
        ApiUpdateManager.init(URLString).setPostParam(memoText).post(new ApiUpdateManager.Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MemoEditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MemoEditActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void executeGetMemo(String memoURLString){
        ApiGetManager.init(memoURLString).get(new ApiGetManager.Callback() {
            @Override
            public void onSuccess(String result) {
                executeParse(result);
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void executeParse(String data){
        ApiMemoParser.init(data).execute(new ApiMemoParser.Callback() {
            @Override
            public void onPostExecute(Memo memo) {
                MemoEditActivity.this.memo = memo;
                EditText memoEditText = (EditText) findViewById(R.id.memo_edit);
                memoEditText.setText(memo.value);
            }
        });
    }
}
