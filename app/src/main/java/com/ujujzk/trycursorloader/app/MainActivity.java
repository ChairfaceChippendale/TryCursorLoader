package com.ujujzk.trycursorloader.app;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, android.support.v7.widget.SearchView.OnQueryTextListener {

    private static final int CM_DELETE_ID = 1;
    RecyclerView list;
    DataBaseDriver db;
    MyListCursorAdapter listCursorAdapter;
    android.support.v7.widget.SearchView searchView;
    LinearLayoutManager listManager;

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
        //listCursorAdapter = new MyListCursorAdapter(this, db.getAllData());
        listCursorAdapter = new MyListCursorAdapter(this, null);
        list.setAdapter(listCursorAdapter);
        list.setItemAnimator(new DefaultItemAnimator());

        getSupportLoaderManager().initLoader(0, null, this);


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
        int position = listCursorAdapter.getPositionOf(query);
        if (position > 0 && position < listCursorAdapter.getItemCount()) {
            listManager.scrollToPositionWithOffset(
                    position,
                    2
            );
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //TODO check if there is this word and
        //TODO go to WordArticleFragment
        return true;
    }






    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new MyCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        listCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {

        DataBaseDriver db;

        public MyCursorLoader(Context context, DataBaseDriver db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();

            return cursor;
        }

    }
}
