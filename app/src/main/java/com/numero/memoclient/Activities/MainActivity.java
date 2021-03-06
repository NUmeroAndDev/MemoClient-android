package com.numero.memoclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.numero.memoclient.Adapter.MemoItemAdapter;
import com.numero.memoclient.MemoApiClient.ApiClientManager;
import com.numero.memoclient.MemoApiClient.ApiMemoArrayParser;
import com.numero.memoclient.MemoApiClient.Memo;
import com.numero.memoclient.R;
import com.numero.memoclient.Utils.Constant;
import com.numero.memoclient.Views.AskDeleteDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList;
    private MemoItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivityForResult(intent, Constant.EDIT_REQUEST_CODE);
            }
        });

        memoList = new ArrayList<>();

        adapter = new MemoItemAdapter(memoList);
        adapter.setOnClickListener(new MemoItemAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(Constant.MEMO_URL, memoList.get(position).URLString);
                startActivityForResult(intent, Constant.EDIT_REQUEST_CODE);
            }

            @Override
            public void onLongClick(final int position) {
                AskDeleteDialog dialog = new AskDeleteDialog(MainActivity.this);
                dialog.setOnClickListener(new AskDeleteDialog.OnClickListener() {
                    @Override
                    public void onClickPositiveButton() {
                        executeDeleteMemo(position);
                    }
                });
                dialog.show();
            }
        });

        RecyclerView memoListRecycler = (RecyclerView) findViewById(R.id.memo_list_recycler);
        memoListRecycler.setLayoutManager(new LinearLayoutManager(this));
        memoListRecycler.setHasFixedSize(true);
        memoListRecycler.setAdapter(adapter);

        executeGetMemo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            executeGetMemo();
        }
    }

    private void executeGetMemo() {
        String URLString = "http://192.168.10.16:3001/memos.json";
        ApiClientManager.init(URLString).requestGet().execute(new ApiClientManager.Callback() {
            @Override
            public void onSuccess(String result) {
                executeParse(result);
            }

            @Override
            public void onFailure(int responseCode) {
                if (responseCode == ApiClientManager.RESPONSE_NOT_CONNECT) {
                    Toast.makeText(MainActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Connect ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void executeParse(String data) {
        memoList.clear();
        ApiMemoArrayParser.init(data).parse(memoList).execute(new ApiMemoArrayParser.Callback() {
            @Override
            public void onPostExecute() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void executeDeleteMemo(int position) {
        String URLString = memoList.get(position).URLString;
        ApiClientManager.init(URLString).requestDelete().execute(new ApiClientManager.Callback() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                executeGetMemo();
            }

            @Override
            public void onFailure(int responseCode) {
                if (responseCode == ApiClientManager.RESPONSE_NOT_CONNECT) {
                    Toast.makeText(MainActivity.this, "Offline", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Connect ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_reload:
                executeGetMemo();
                break;
            case R.id.menu_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
