package com.android.ukmproject.utils;
import com.android.ukmproject.R;
import com.android.ukmproject.model.Item;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;
import com.android.ukmproject.volley.AppController;


public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> itemItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Item> itemItems) {
        this.activity = activity;
        this.itemItems = itemItems;
    }

    @Override
    public int getCount() {
        return itemItems.size();
    }

    @Override
    public Object getItem(int location) {
        return itemItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = inflater.inflate(R.layout.rest_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView name = (TextView) convertView.findViewById(R.id.restname);
        TextView mobile = (TextView) convertView.findViewById(R.id.mobile);


        Item m = itemItems.get(position);

        id.setText(m.getId());
        thumbNail.setImageUrl(Configuration.SERVER_IP + "uploads/" + m.getPhotopath(), imageLoader);
        name.setText(m.getName());
        mobile.setText("Phone: " + m.getMobile());


        return convertView;
    }



}