package com.danielr_shlomoc.ex3;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AndroidTaskAdapter extends ArrayAdapter<AndroidTaskItem> {


    public AndroidTaskAdapter(Activity context, ArrayList<AndroidTaskItem> androidTaskItem) {
        super(context, 0, androidTaskItem);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null)
        {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            convertView = View.inflate(getContext(), R.layout.list_item, null);
        }
        // Get the {@link AndroidFlavor} object located at this position in the list
        AndroidTaskItem currentAndroidTask = getItem(position);

        // Find the TextView in the list_item2.xmll layout with the ID version_name
        TextView txvTaskTitle = convertView.findViewById(R.id.titleTxvID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        txvTaskTitle.setText(currentAndroidTask.getTaskTitle());


        TextView date = convertView.findViewById(R.id.dateTxvID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        date.setText(currentAndroidTask.getDate());

        TextView decription = convertView.findViewById(R.id.descriptionTxvID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        decription.setText(currentAndroidTask.getDescription());

        TextView time = convertView.findViewById(R.id.timeTxvID);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        time.setText(currentAndroidTask.getTime());


        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return convertView;
    }
}
