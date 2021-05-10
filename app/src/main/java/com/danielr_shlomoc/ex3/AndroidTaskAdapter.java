package com.danielr_shlomoc.ex3;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AndroidTaskAdapter extends ArrayAdapter<Task> {


    public AndroidTaskAdapter(Activity context, ArrayList<Task> androidTaskItem) {
        super(context, 0, androidTaskItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (convertView == null) {

            convertView = View.inflate(getContext(), R.layout.list_item, null);
        }

        Task currentAndroidTask = getItem(position);


        TextView txvTaskTitle = convertView.findViewById(R.id.titleTxvID);
        txvTaskTitle.setText(currentAndroidTask.getTitle());


        TextView date = convertView.findViewById(R.id.dateTxvID);
        date.setText(currentAndroidTask.getDate());

        TextView description = convertView.findViewById(R.id.descriptionTxvID);
        description.setText(currentAndroidTask.getDescription());

        TextView time = convertView.findViewById(R.id.timeTxvID);
        time.setText(currentAndroidTask.getTime());


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }
}
