package com.ujujzk.trycursorloader.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {

    private Cursor searchCursor;

    public MyListCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        String word = cursor.getString(cursor.getColumnIndex("txt"));

        viewHolder.mTextView.setText(word);
    }

    int getPositionOf (String queryWord) {

        queryWord = queryWord.toLowerCase();

        if (searchCursor == null){
            searchCursor = getCursor();
        }
        int wordColumnIndex = searchCursor.getColumnIndex("txt");

        if (searchCursor.moveToFirst()){
            int step = 0;
            do {
                if (searchCursor.getString(wordColumnIndex).toLowerCase().startsWith(queryWord)){
                    return searchCursor.getPosition();
                }
                step++;
            } while (searchCursor.moveToNext()); //TODO можно добавить ограничение количества шагов, но тогда список обязательно должен быть отсортирован
        }

        return 0;
    }
}
