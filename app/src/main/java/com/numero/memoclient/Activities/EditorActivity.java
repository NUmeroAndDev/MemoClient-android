package com.numero.memoclient.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.numero.memoclient.MemoApiClient.ApiClientManager;
import com.numero.memoclient.MemoApiClient.ApiMemoParser;
import com.numero.memoclient.MemoApiClient.Memo;
import com.numero.memoclient.R;
import com.numero.memoclient.Utils.Constant;
import com.numero.memoclient.Views.LoadingProgressDialog;

public class EditorActivity extends AppCompatActivity {

    private LoadingProgressDialog loadingProgressDialog;

    private boolean isNewMemo = true;
    private Memo memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText memoEditText = (EditText) findViewById(R.id.memo_edit);
                String memoText = memoEditText.getText().toString();
                if (memoText.equals("")) {
                    return;
                }
                loadingProgressDialog.show();
                executeSave(memoText);
            }
        });

        loadingProgressDialog = new LoadingProgressDialog(this);

        String memoURLString = getIntent().getStringExtra(Constant.MEMO_URL);
        if (memoURLString != null) {
            isNewMemo = false;
            findViewById(R.id.bottom_layout).requestFocus();
            loadingProgressDialog.show();
            executeGetMemo(memoURLString);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void executeSave(String memoText) {
        ApiClientManager clientManager;
        if (isNewMemo) {
            String URLString = "http://192.168.10.16:3001/memos";
            clientManager = ApiClientManager.init(URLString).requestPost(memoText);
        } else {
            clientManager = ApiClientManager.init(memo.URLString).requestPut(memoText);
        }
        clientManager.execute(new ApiClientManager.Callback() {
            @Override
            public void onSuccess(String result) {
                loadingProgressDialog.dismiss();
                Toast.makeText(EditorActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(int responseCode) {
                loadingProgressDialog.dismiss();
                if (responseCode == ApiClientManager.RESPONSE_NOT_CONNECT) {
                    Toast.makeText(EditorActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                } else if (responseCode == ApiClientManager.RESPONSE_NOT_FIND){
                    Toast.makeText(EditorActivity.this, "Memo not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditorActivity.this, "Connect ERROR", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void executeGetMemo(String URLString) {
        ApiClientManager.init(URLString).requestGet().execute(new ApiClientManager.Callback() {
            @Override
            public void onSuccess(String result) {
                executeParse(result);
            }

            @Override
            public void onFailure(int responseCode) {
                loadingProgressDialog.dismiss();
                if (responseCode == ApiClientManager.RESPONSE_NOT_CONNECT) {
                    Toast.makeText(EditorActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                } else if (responseCode == ApiClientManager.RESPONSE_NOT_FIND) {
                    Toast.makeText(EditorActivity.this, "Memo not found", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditorActivity.this, "Connect ERROR", Toast.LENGTH_SHORT).show();
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void executeParse(String data) {
        ApiMemoParser.init(data).execute(new ApiMemoParser.Callback() {
            @Override
            public void onPostExecute(Memo memo) {
                loadingProgressDialog.dismiss();
                EditorActivity.this.memo = memo;
                EditText memoEditText = (EditText) findViewById(R.id.memo_edit);
                memoEditText.setText(memo.value);
            }
        });
    }
}
