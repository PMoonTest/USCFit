package com.example.fred.uscfit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AddActivityAdapter extends BaseAdapter {
    List<String> userData;
    Context context;


    public AddActivityAdapter(Context context, List<String> userSport) {
        this.context = context;
        this.userData = userSport;
    }

    @Override
    public int getCount() {
        return userData.size();
    }

    @Override
    public Object getItem(int position) {
        return userData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_sports,
                    parent, false);
        }
        if(userData == null){
            convertView = inflater.inflate(R.layout.sports_loading,
                    parent, false);
            return convertView;
        }
        else {
            TextView sportView = convertView.findViewById(R.id.sport_name);
            String sportName = userData.get(position);
            sportView.setText(userData.get(position));

            ImageView imageView = convertView.findViewById(R.id.sport_icon);

            if(sportName.toLowerCase().equals("running")) {
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_running));
            }
            else if(sportName.toLowerCase().equals("boxing")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_boxing));
            }
            else if(sportName.toLowerCase().equals("soccer")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_soccer));
            }
            else if(sportName.toLowerCase().equals("climbing")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_climbing));
            }
            else if(sportName.toLowerCase().equals("running")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_running));
            }
            else if(sportName.toLowerCase().equals("basketball")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_baseball));
            }
            else if(sportName.toLowerCase().equals("bowling")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_bowling));
            }
            else if(sportName.toLowerCase().equals("cycling")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_cycling));
            }
            else{
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_default));
            }
            return convertView;
        }

    }
}
