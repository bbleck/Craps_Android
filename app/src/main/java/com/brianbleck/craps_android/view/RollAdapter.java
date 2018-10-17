package com.brianbleck.craps_android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.brianbleck.craps_android.R;
import com.brianbleck.craps_android.model.Game.Roll;
import com.brianbleck.craps_android.model.Game.State;

public class RollAdapter extends ArrayAdapter<Roll> {

  private Drawable[] faces;


  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    Roll roll = getItem(position);
    int[] dice = roll.getDice();
    if(convertView==null){
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_roll, parent, false);
    }

    ImageView die0 = convertView.findViewById(R.id.die_0);
    ImageView die1 = convertView.findViewById(R.id.die_1);
    TextView total = convertView.findViewById(R.id.dice_total);


      die0.setImageDrawable(faces[dice[0]-1]);
      die1.setImageDrawable(faces[dice[1]-1]);
      total.setText(getContext().getString(R.string.dice_total_format, dice[0] + dice[1]));



    if(position==0 && this.getCount()>1){
      convertView.setBackgroundColor(Color.GRAY);
    }else if(roll.getState()== State.WIN){
      convertView.setBackgroundColor(Color.GREEN);
    }else if(roll.getState()==State.LOSS){
      convertView.setBackgroundColor(Color.RED);
    }else{
      convertView.setBackgroundColor(Color.TRANSPARENT);
    }

    return convertView;
  }

  public RollAdapter(@NonNull Context context) {
    super(context, R.layout.item_roll);
    Resources res = context.getResources();
    String pkg = context.getPackageName();
    faces = new Drawable[6];
    for (int i = 0; i < 6; i++) {
      faces[i] = ContextCompat.getDrawable(
          context, res.getIdentifier("face_" + (i+1), "drawable", pkg));
    }
  }
}
