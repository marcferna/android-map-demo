package com.codepath.marcferna.mapexercise;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codepath.marcferna.mapexercise.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by marc on 10/13/14.
 */
class CustomWindowAdapter implements GoogleMap.InfoWindowAdapter {
  LayoutInflater mInflater;

  public CustomWindowAdapter(LayoutInflater i){
    mInflater = i;
  }

  @Override
  public View getInfoContents(Marker marker) {
    // Getting view from the layout file
    View v = mInflater.inflate(R.layout.custom_info_window, null);

    TextView title = (TextView) v.findViewById(R.id.tv_info_window_title);
    title.setText(marker.getTitle());

    TextView description = (TextView) v.findViewById(R.id.tv_info_window_description);
    description.setText(marker.getSnippet());

    return v;
  }

  @Override
  public View getInfoWindow(Marker marker) {
    // TODO Auto-generated method stub
    return null;
  }
}