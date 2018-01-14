package com.example.shadabazamfarooqui.mylocator.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.network.request.GetRequest;
import com.squareup.picasso.Picasso;

/**
 * Created by Shadab Azam Farooqui on 24-Dec-17.
 */

public class MosqueAdapter extends BaseAdapter {


    GetRequest response;
    Context context;

    public MosqueAdapter(Context context, GetRequest response) {
        this.response = response;
        this.context = context;
    }

    @Override
    public int getCount() {
        return response.getResults().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
       // LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // well set up the ViewHolder
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_list, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.rating = (TextView) convertView.findViewById(R.id.rating);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.image = (ImageView) convertView.findViewById(R.id.img);
            holder.mainLayout=(LinearLayout) convertView.findViewById(R.id.mainLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText("" + response.getResults().get(i).getName());
        holder.address.setText("" + response.getResults().get(i).getVicinity());
        holder.rating.setText("Rating  " + response.getResults().get(i).getRating());
        Picasso.with(context)
                .load(response.getResults().get(i).getIcon())
                .into(holder.image);
        
        return convertView;
    }

    static class ViewHolder {
        TextView name, address, rating;
        ImageView image;
        LinearLayout mainLayout;
    }


   /* @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row_list, null);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView distance = (TextView) view.findViewById(R.id.address);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        ImageView image=(ImageView)view.findViewById(R.id.img);
        name.setText(""+response.getResults().get(i).getName());
        distance.setText(""+response.getResults().get(i).getVicinity());
        rating.setText("Rating  "+response.getResults().get(i).getRating());
        Picasso.with(context)
                .load(response.getResults().get(i).getIcon())
                .into(image);
        return view;
    }*/
}
