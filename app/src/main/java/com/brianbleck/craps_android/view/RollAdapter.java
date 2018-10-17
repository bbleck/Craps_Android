package com.brianbleck.craps_android.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.brianbleck.craps_android.R;
import com.brianbleck.craps_android.model.Game.Roll;

public class RollAdapter extends ArrayAdapter<Roll> {


  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    Roll roll = getItem(position);
    View theView = LayoutInflater.from(getContext()).inflate(R.layout.item_roll, parent, false);
    return theView;
  }

  public RollAdapter(@NonNull Context context,
      int resource) {
    super(context, resource);
    //todo: load dice images into a drawable array
  }
}
