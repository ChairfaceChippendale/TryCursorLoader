package com.ujujzk.trycursorloader.app;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MainActivity extends AppCompatActivity
        implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private static final int QUERY_WORD_RESULT_LIMIT = 100;
    private static final String QUERY_WORD_LIMIT_KEY = "limit";
    private static final String QUERY_WORD_KEY = "word";
    RecyclerView list;
    DataBaseDriver db;
    MyListCursorAdapter listCursorAdapter;
    android.support.v7.widget.SearchView searchView;
    LinearLayoutManager listManager;

    MyTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseDriver(this);
        db.open();

        list = (RecyclerView) findViewById(R.id.list);
        list.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        listManager = new LinearLayoutManager(this);
        list.setLayoutManager(listManager);
        listCursorAdapter = new MyListCursorAdapter(this, null);
        list.setAdapter(listCursorAdapter);
        list.setItemAnimator(new DefaultItemAnimator());

        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.search);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
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

    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("MyTAG", "start");
        Bundle args = new Bundle();
        args.putString(QUERY_WORD_KEY, query);
        args.putInt(QUERY_WORD_LIMIT_KEY, QUERY_WORD_RESULT_LIMIT);


        if(task != null && task.getStatus() == AsyncTask.Status.RUNNING){
            task.cancel(true);
        }
        task = new MyTask();
        task.execute(args);

        return true;
    }



    class MyTask extends AsyncTask<Bundle,Void,Cursor>{

        @Override
        protected Cursor doInBackground(Bundle... bndl) {

            if (bndl == null || bndl.length == 0){
                return CursorLoggerProxy.wrap(db.getAllData());
            }
            final String queryWord = bndl[0].getString(QUERY_WORD_KEY);
            final int limit = bndl[0].getInt(QUERY_WORD_LIMIT_KEY);

            if (queryWord == null || limit <= 0) {
                return CursorLoggerProxy.wrap(db.getAllData());
            }

            return CursorLoggerProxy.wrap(db.getLimitedDataByQueryWord(queryWord, limit));

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor != null){
                listCursorAdapter.changeCursor(cursor);
                listManager.scrollToPosition(0);
                Log.d("MyTAG", "finish");
            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO check if there is this word and
        //TODO go to WordArticleFragment
        return true;
    }


}
