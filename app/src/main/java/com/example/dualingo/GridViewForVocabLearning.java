package com.example.dualingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewForVocabLearning extends BaseAdapter {
    private Context context;
    private String[] items;
    public GridViewForVocabLearning(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_vocab_item, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.txtResult);
        textView.setText(items[position]);
        return convertView;
    }
}
