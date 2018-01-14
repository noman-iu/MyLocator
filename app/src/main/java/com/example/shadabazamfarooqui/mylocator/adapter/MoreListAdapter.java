package com.example.shadabazamfarooqui.mylocator.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shadabazamfarooqui.mylocator.R;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Shadab Azam Farooqui on 14-Jan-18.
 */

public class MoreListAdapter extends BaseAdapter{

    Context context;
    List<String> listName;
    List<Integer> listImage;
    public MoreListAdapter(Context context, List<String> listName,List<Integer> listImage){
        this.context=context;
        this.listName =listName;
        this.listImage=listImage;
    }
    @Override
    public int getCount() {
        return listName.size();
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
//        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_more_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.more_text);
            holder.image = (ImageView) convertView.findViewById(R.id.more_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText("" + listName.get(i));
        holder.image.setImageResource(listImage.get(i));
        return convertView;
    }

    static class ViewHolder {
        TextView name;
        ImageView image;
    }

}
