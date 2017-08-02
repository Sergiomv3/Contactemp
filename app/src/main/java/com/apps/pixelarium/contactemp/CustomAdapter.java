package com.apps.pixelarium.contactemp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by smarti42 on 01/08/2017.
 */

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private ArrayList<DataModel> dataSet;
    Context mContext;
    private int lastPosition = -1;


    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
        ImageView imageView;
    }
    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "HAS PULSADO UN ELEMENTO", Toast.LENGTH_LONG).show();
    }
    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag


        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.textview_name);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.textview_number);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.item_info);



            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getName());
        viewHolder.txtNumber.setText(dataModel.getNumber());
        /*TODO set image if contact is programmed*/
        if(dataModel.isProgrammed()){
            //viewHolder.imageView.setImageDrawable(R.drawable.on);
        }else{
            //viewHolder.imageView.setImageDrawable(R.drawable.off);
        }

        // Return the completed view to render on screen
        return convertView;
    }


}
