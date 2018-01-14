package com.example.shadabazamfarooqui.mylocator.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.shadabazamfarooqui.mylocator.R;
import com.example.shadabazamfarooqui.mylocator.network.request.GetRequest;

public class MosqueExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private GetRequest listParent;

    public MosqueExpandableListAdapter(Context context, GetRequest  listHeaderName) {
        this._context = context;
        this.listParent = listHeaderName;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listParent.getResults().size();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.address);
        txtListChild.setText(listParent.getResults().get(groupPosition).getVicinity());


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listParent.getResults().get(groupPosition).getName().toString();
    }

    @Override
    public int getGroupCount() {
        return this.listParent.getResults().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).toString();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_layout, null);
        }
        TextView groupName = (TextView) convertView.findViewById(R.id.group_name);
        groupName.setTypeface(null, Typeface.BOLD);
        groupName.setText(headerTitle);
        ImageView imageview=(ImageView)convertView.findViewById(R.id.group_image);
        if(isExpanded){
            imageview.setImageResource(R.drawable.up_arrow);
        }
        else{
            imageview.setImageResource(R.drawable.down_arrow);
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}