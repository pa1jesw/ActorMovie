package com.pawan.actormovie;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PAWAN on 5/29/2017.
 */

public class ActorList extends ArrayAdapter<Actor> {
    private Activity context;
    private List<Actor> actList;

    public ActorList(Activity context, List<Actor> atList) {
        super(context,R.layout.layout_list,atList);
        this.context = context;
        this.actList = atList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inf = context.getLayoutInflater();
        View lvItem = inf.inflate(R.layout.layout_list,null,true);
        TextView tvAname = (TextView) lvItem.findViewById(R.id.tvAname);
        TextView tvMovie = (TextView) lvItem.findViewById(R.id.tvMovie);

        Actor act = actList.get(position);

        tvAname.setText(act.getaName());
        tvMovie.setText(act.getMovie());

        return lvItem;
    }
}
