package com.example.dualingo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class GridViewForVocabLearning extends BaseAdapter {
    private Context context;
    private List<String> items;
    public GridViewForVocabLearning(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
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
        textView.setText(items.get(position));
        return convertView;
    }
}
