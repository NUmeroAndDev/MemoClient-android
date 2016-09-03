package com.numero.memoclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.numero.memoclient.Adapter.MemoItemAdapter;
import com.numero.memoclient.MemoApiClient.ApiClientManager;
import com.numero.memoclient.MemoApiClient.ApiMemoArrayParser;
import com.numero.memoclient.MemoApiClient.Memo;
import com.numero.memoclient.R;
import com.numero.memoclient.Utils.Constant;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList;
    private MemoItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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
                Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
                startActivity(intent);
            }
        });

        memoList = new ArrayList<>();

        adapter = new MemoItemAdapter(memoList);
        adapter.setOnClickListener(new MemoItemAdapter.OnClickListener() {
            @Override
            public void OnClick(int position) {
                Intent intent = new Intent(MainActivity.this, MemoEditActivity.class);
                intent.putExtra(Constant.MEMO_URL, memoList.get(position).URLString);
                startActivity(intent);
            }
        });

        RecyclerView memoListRecycler = (RecyclerView) findViewById(R.id.memo_list_recycler);
        memoListRecycler.setLayoutManager(new LinearLayoutManager(this));
        memoListRecycler.setHasFixedSize(true);
        memoListRecycler.setAdapter(adapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.refresh_layout_scheme));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeGetMemo();
            }
        });

        executeGetMemo();
    }

    private void executeGetMemo(){
        memoList.clear();
        String URLString = "http://192.168.10.16:3001/memos.json";
        ApiClientManager.init(URLString).requestGet().execute(new ApiClientManager.Callback() {
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
        ApiMemoArrayParser.init(data).parse(memoList).execute(new ApiMemoArrayParser.Callback() {
            @Override
            public void onPostExecute() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
